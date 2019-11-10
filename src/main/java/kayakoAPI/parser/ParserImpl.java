package kayakoAPI.parser;

import kayakoAPI.dates.DateAnalyzer;
import kayakoAPI.mappers.universalMappers.UniversalMapper;
import kayakoAPI.pojos.Agent;
import kayakoAPI.pojos.Conversation;
import kayakoAPI.pojos.User;
import kayakoAPI.urls.URLS;
import org.json.JSONArray;
import org.json.JSONObject;
import pojos.conversationStatus.Status;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class ParserImpl implements Parser{

    private static BlockingQueue<JSONObject> queue = new ArrayBlockingQueue<>(500);
    private volatile AtomicInteger pageCount = new AtomicInteger();

    private JsonGetter jsonGetter;
    private DateAnalyzer dateAnalyzer;

    private Logger logger = Logger.getLogger(ParserImpl.class.getName());

    private class Producer implements Runnable{

        Predicate<JSONObject> jsonObjectPredicate;

        Producer(Predicate<JSONObject> jsonObjectPredicate){
            this.jsonObjectPredicate = jsonObjectPredicate;
        }

        @Override
        public void run() {
            try {
                produce(this.jsonObjectPredicate);
            } catch (InterruptedException e) {
                logger.severe(e.getMessage());
            }
        }

        private void produce(Predicate<JSONObject> jsonObjectPredicate) throws InterruptedException{
            String currentUrl = URLS.CASES.getUrl();
            String jsonPage;
            JSONArray jsonObjects;
            JSONObject jsonObject;

            boolean stop = false;
            do{
                jsonPage = jsonGetter.getJsonFromUrl(currentUrl);
                jsonObjects = new JSONObject(jsonPage).getJSONArray("data");

                for(Object object : jsonObjects){
                    jsonObject = (JSONObject)object;
                    if(jsonObjectPredicate.test(jsonObject)){
                        jsonObject.put("stop","true");
                        stop = true;
                    }
                    queue.put(jsonObject);
                }
                pageCount.incrementAndGet();

                String status = new JSONObject(jsonPage).get("status").toString();
                logger.info("Page " + pageCount + " — " + ((status != null) ? status : "(NULL)") );

                Thread.sleep(500);

                currentUrl = getNextUrl(currentUrl);
            }while (!stop);
        }
    }
    private class Consumer implements Runnable{

        List<Conversation> conversations;
        Consumer(List<Conversation> conversations){
            this.conversations = conversations;
        }

        @Override
        public void run() {
            try {
                consume();
            } catch (InterruptedException | IllegalAccessException
                    | IntrospectionException | InvocationTargetException e) {
                logger.severe(e.getMessage());
            }
        }

        private synchronized void consume() throws InterruptedException, IllegalAccessException, IntrospectionException, InvocationTargetException {
            JSONObject data;
            Conversation conversation;
            String userData;

            UniversalMapper<Conversation> conversationMapper = new UniversalMapper<>();
            UniversalMapper<User> userMapper = new UniversalMapper<>();

            AtomicInteger conversationCount = new AtomicInteger(0);
            while (true){
                data = queue.take();
                if(!data.has("stop")){
                    conversation = conversationMapper.mapObject(data, new Conversation());

                    conversation.setConversationLink(URLS.CONVERSATION.getUrl() + conversation.getConversationId().toString());
                    //Requester
                    userData = jsonGetter.getJsonFromUrl(URLS.USER.getUrl() + conversation.getRequesterId());
                    User user = userMapper.mapObject(new JSONObject(userData), new User());
                    user.initializeUserId();
                    conversation.setRequester(user);
                    conversation.setStatus(Status.fromInt(conversation.getStatusId()));

                    Integer assigneeId = conversation.getAssignee_id();
                    if(assigneeId != null){
                        conversation.setAssignedAgent(Agent.fromId(assigneeId));
                    }

                    conversations.add(conversation);

                    conversationCount.incrementAndGet();
                    logger.info(conversationCount + " conversations are completed");
                    Thread.sleep(100);
                }else {
                    logger.info("All conversations are extracted");
                    return;
                }
            }
        }
    }

    public <T> List<Conversation> extract(Predicate<JSONObject> jsonObjectPredicate){
        List<Conversation> conversations = new ArrayList<>();
        Thread producer = new Thread(new Producer(jsonObjectPredicate));
        Thread consumer = new Thread(new Consumer(conversations));
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return conversations;
    }

    @Deprecated
    public <T> List<Conversation> extractConversations(Predicate<Conversation> predicate){
        long start = System.currentTimeMillis();

        //logger.info("Starting exporting conversations until the date "+ until);

        UniversalMapper<Conversation> conversationMapper = new UniversalMapper<>();
        UniversalMapper<User> userMapper = new UniversalMapper<>();
        String currentUrl = URLS.CASES.getUrl();
        List<Conversation> extractedConversations = new ArrayList<>();

        JSONArray conversations;
        Conversation conversation;
        String user;
        Date currentDate;

        int conversationCounter = 1;
        int page = 1;
        boolean stop = false;

        do{
            conversations = getData(currentUrl);

            for(Object data : conversations){
                try{
                    conversation = conversationMapper.mapObject((JSONObject)data, new Conversation());

                    //Requester
                    user = jsonGetter.getJsonFromUrl(URLS.USER.getUrl() + conversation.getRequesterId());
                    conversation.setRequester(userMapper.mapObject(new JSONObject(user), new User()));
                    conversation.setStatus(Status.fromInt(conversation.getStatusId()));

                    currentDate = dateAnalyzer.getDateAndTimeFromISO(conversation.getUpdatedAt());
                    System.out.println(currentDate);

                   if(predicate.test(conversation)){
                       stop = true;
                       break;
                   }
                    extractedConversations.add(conversation);
                    conversationCounter++;
                }catch (Exception e){
                    logger.severe(e.getMessage());
                }
            }
            currentUrl = getNextUrl(currentUrl);

            logger.info("Page "+ page + " is finished.");
            page++;
        }while (!stop);

        logger.info(conversationCounter + " conversations has been extracted.");
        long finish = System.currentTimeMillis();
        double timeElapsed = ((finish - start)/1000)/60;
        logger.info("Extraction took: " + timeElapsed + " minutes");

        return extractedConversations;
    }

    private JSONArray getData(String currentUrl){
        JSONObject conversationJsonPage = new JSONObject(jsonGetter.getJsonFromUrl(currentUrl));
        System.out.println(conversationJsonPage.get("status"));
        return conversationJsonPage.getJSONArray("data");
    }

    private String getNextUrl(String currentUrl){
        JSONObject conversationJsonPage = new JSONObject(jsonGetter.getJsonFromUrl(currentUrl));
        return conversationJsonPage.getString("next_url");
    }

    public JsonGetter getJsonGetter() {
        return jsonGetter;
    }

    public void setJsonGetter(JsonGetter jsonGetter) {
        this.jsonGetter = jsonGetter;
    }

    public DateAnalyzer getDateAnalyzer() {
        return dateAnalyzer;
    }

    public void setDateAnalyzer(DateAnalyzer dateAnalyzer) {
        this.dateAnalyzer = dateAnalyzer;
    }

}
