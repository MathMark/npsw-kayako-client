package kayakoAPI.timeMeasurer;

public class TimeMeasurer {

    public Time measureTime(Executer executer){
        long start = System.currentTimeMillis();
        executer.process();
        long end = System.currentTimeMillis();

        long seconds = (end - start)/Time.MILLI_IN_SECONDS;
        long minutes = seconds/Time.SECONDS_IN_MINUTE;
        seconds %= Time.SECONDS_IN_MINUTE;

        return new Time(minutes, seconds);
    }
}
