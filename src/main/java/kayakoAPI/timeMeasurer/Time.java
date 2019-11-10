package kayakoAPI.timeMeasurer;

public class Time {

    public static final long MILLI_IN_SECONDS = 1000;
    public static final long SECONDS_IN_MINUTE = 60;

    private long minutes;
    private long seconds;

    public Time(long minutes, long seconds){
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Time(){

    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }
}
