package kayakoAPI;

import kayakoAPI.dates.DateAnalyzer;
import kayakoAPI.fileWriter.ExcelWriter;
import kayakoAPI.parser.JsonGetter;
import kayakoAPI.parser.ParserImpl;
import kayakoAPI.pojos.Conversation;
import kayakoAPI.timeMeasurer.Time;
import kayakoAPI.timeMeasurer.TimeMeasurer;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String ... args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your login: ");
        String login = scanner.nextLine();
        System.out.println("Enter the password: ");
        String password = scanner.nextLine();

        //
        JsonGetter getter = new JsonGetter(login, password);
        DateAnalyzer dateAnalyzer = new DateAnalyzer();

        Logger logger = Logger.getLogger(Main.class.getName());

        try{
            ParserImpl parserImpl = new ParserImpl();
            parserImpl.setJsonGetter(getter);
            parserImpl.setDateAnalyzer(dateAnalyzer);

            ExcelWriter excelWriter = new ExcelWriter("conversations");

            Date requiredDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-10");

            TimeMeasurer timeMeasurer = new TimeMeasurer();
            Time time = timeMeasurer.measureTime(() -> {
                List<Conversation> conversations = parserImpl.extract((e) -> {
                    Date currentDate = dateAnalyzer.getDateAndTimeFromISO(e.get("updated_at").toString());
                    return currentDate.compareTo(requiredDate) <= 0;
                });
                List<Conversation> activeConversations = conversations.stream().filter(c -> !c.getState().equals("TRASH")).collect(Collectors.toList());
                try {
                    excelWriter.writeObjects(activeConversations,Conversation.class);
                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException | IOException e) {
                    logger.severe(e.getMessage());
                }
            });

            logger.info("Time spent: " + time.getMinutes() + " minutes and " + time.getSeconds() + " seconds");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
