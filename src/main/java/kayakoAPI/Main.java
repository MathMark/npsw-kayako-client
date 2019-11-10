package kayakoAPI;

import kayakoAPI.config.AppConfig;
import kayakoAPI.dates.DateAnalyzer;
import kayakoAPI.fileWriter.TableExporter;
import kayakoAPI.parser.ParserImpl;
import kayakoAPI.parser.kayakoClient.KayakoClient;
import kayakoAPI.pojos.Conversation;
import kayakoAPI.timeMeasurer.Time;
import kayakoAPI.timeMeasurer.TimeMeasurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Logger logger = Logger.getLogger(Main.class.getName());

        try{
            TableExporter excelWriter = (TableExporter) context.getBean("excelWriter");
            KayakoClient getter = (KayakoClient) context.getBean("kayakoClient");
            getter.setUser(login);
            getter.setPassword(password);
            DateAnalyzer dateAnalyzer = (DateAnalyzer) context.getBean("dateAnalyzer");
            ParserImpl parserImpl = (ParserImpl) context.getBean("parser");

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
