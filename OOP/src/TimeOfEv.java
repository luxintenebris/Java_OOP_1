public class TimeOfEv {
    private int hour;
    private int minutes;
    private int day;
    public TimeOfEv(int d, int h, int m){
        this.day = d;
        this.hour = h;
        this.minutes = m;
    }
    public TimeOfEv(){};
    public int Day(){
        return day;
    }
    public int Minutes(){
        return minutes;
    }
    public int Hour(){
        return hour;
    }
    public void setDay(int d){
        day = d;
    }

    public int TimeInMin(TimeOfEv t){
        return  24 * 60 * t.day + 60 * t.hour + t.minutes;
    }
    public TimeOfEv MinInTime(int m){
        int d = m/60/24;
        m = m-d*60*24;
        int h = m/60;
        m =  m - h * 60;
        return new TimeOfEv(d,h,m);
    }

    public String PrintTime(TimeOfEv t){
        return t.day + "." + t.hour + "." + t.minutes;
    }
    public boolean FirstLessSecond(TimeOfEv two){
        return (this.TimeInMin(this) < two.TimeInMin(two));
    }
    public boolean SecondLessFirst(TimeOfEv two){
        return (this.TimeInMin(this) > two.TimeInMin(two));
    }
    public boolean FirstLessEqSecond(TimeOfEv two){
        return (this.TimeInMin(this) <= two.TimeInMin(two));
    }
    public boolean SecondLessEqFirst(TimeOfEv two){
        return (this.TimeInMin(this) >= two.TimeInMin(two));
    }


    public TimeOfEv StringToTime(String s) {
        String[] date = s.split("\\.");
        TimeOfEv time = new TimeOfEv(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        return time;
    }
}
