package kayakoAPI.dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateAnalyzer {

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

    public Date getDateFromISO(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(splitDate(date)[0]);
    }

    private String[] splitDate(String date){
        return date.split("T");
    }

    public String getLocalTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-mm-yyyy_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public Date addToMonth(Date date, int offset){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, offset);
        return cal.getTime();
    }

    public Date addToDay(Date date, int offset){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        return cal.getTime();
    }
}
