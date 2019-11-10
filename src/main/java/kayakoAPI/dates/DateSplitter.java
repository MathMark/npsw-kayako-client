package kayakoAPI.dates;

import java.text.ParseException;
import java.util.Date;

public interface DateSplitter {
    Date getDateAndTimeFromISO(String date);
    Date getDateFromISO(String date) throws ParseException;
}
