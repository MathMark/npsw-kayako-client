package kayakoAPI.dates;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateAnalyzer implements DateSplitter {

    @Override
    public Date getDateAndTimeFromISO(String date){
        String[]dateTime = splitDate(date);
        String newDate = dateTime[0] + " " + dateTime[1];

        Date result = null;
        try {
             result = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Date getDateFromISO(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(splitDate(date)[0]);
    }

    private String[] splitDate(String date){
        return date.split("T");
    }

}
