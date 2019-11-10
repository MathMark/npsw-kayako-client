package kayakoAPI.config;

import kayakoAPI.dates.DateAnalyzer;
import kayakoAPI.dates.DateSplitter;
import kayakoAPI.fileWriter.ExcelWriter;
import kayakoAPI.fileWriter.TableExporter;
import kayakoAPI.parser.Parser;
import kayakoAPI.parser.ParserImpl;
import kayakoAPI.parser.kayakoClient.KayakoClient;
import kayakoAPI.parser.kayakoClient.KayakoClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("application.properties")
@Configuration
public class AppConfig {

    @Bean(name = "excelWriter")
    public TableExporter tableExporter(){
        return new ExcelWriter();
    }

    @Bean(name = "kayakoClient")
    public KayakoClient kayakoClient(){
        return new KayakoClientImpl();
    }

    @Bean(name = "parser")
    public Parser parser(){
        return new ParserImpl();
    }

    @Bean(name = "dateAnalyzer")
    public DateSplitter dateSplitter(){
        return new DateAnalyzer();
    }
}
