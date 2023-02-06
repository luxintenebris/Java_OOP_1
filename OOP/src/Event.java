import java.util.Vector;

public class Event {
    private String place;
    private TimeOfEv beginOfEv;
    private TimeOfEv endOfEv;
    private int priority;
    private  int numberOfPeople;
    private Vector<Person> namesOfPeople;
    private int id;
    private boolean mailStatus = false;
    private TimeOfEv timeSent = new TimeOfEv();
    private int mailCount = 3;
    private void SetTimeSent(TimeOfEv timeExp){
        timeSent = timeSent.MinInTime(beginOfEv.TimeInMin(beginOfEv));
        timeSent.setDay(timeSent.Day() - mailCount);
        if (timeSent.TimeInMin(timeSent)<0){
            timeSent = timeSent.MinInTime(0);
        }
        if (priority >=2 ){
            mailCount = 0;
        }
        if (priority == 1){
            int day = beginOfEv.Day()-timeExp.Day();
            if (mailCount > day){
                mailCount = day;
            }
            mailCount--;
        }
    }

    public void SetBeginOfEv(TimeOfEv beg){
        beginOfEv = beg;
    }
    public void SetEndOfEv(TimeOfEv end){
        endOfEv = end;
    }
    public void setPriority(int pr){
        priority = pr;
    }
    public void setNumberOfPeople(int n){
        numberOfPeople = n;
    }
    public void SetVectorPeople(Vector<Person> names){
        namesOfPeople = names;
    }
    public void setPlace(String pl){
        place = pl;
    }
    public String StrPeople(){
        String s1 = "";
        for (int i = 0; i < namesOfPeople.size(); i++){
            s1+= namesOfPeople.get(i).Name()+" ";
        }
        return s1;
    }
    public int GetNumberOfPeople(){return numberOfPeople;}
    public Vector<Person> GetPeople(){return namesOfPeople;}
    public TimeOfEv GetTimeSent(){return timeSent;}
    public String Info(boolean withTime){
        String s1 = "";
        s1 += "Place: " + place;
        if (withTime){
            s1 += " time: " + beginOfEv.Day() + ", " + beginOfEv.Hour() + ":" + beginOfEv.Minutes() + " - "
                            + endOfEv.Day() + ", " + endOfEv.Hour() + ":" + endOfEv.Minutes();
        }
        s1 += " Priority: " + priority +
              " Number of people: " + numberOfPeople +
              " People: " + StrPeople();
        return s1;
    }
    public String ShortInfo(){ //для ленты происходящего
        return "[ " + priority + "] " + place +" { " + numberOfPeople + " } ";
    }
    public String MailInfo(){//отправление письма - лента отправки
        String mailSent = beginOfEv.PrintTime(beginOfEv) + " in " + place + " sent to ";
        for (int i = 0; i < namesOfPeople.size(); i++){
            mailSent += namesOfPeople.get(i).MailPerson() + "; ";
        }
        return mailSent;
    }
    public boolean GetMailStatus(){return mailStatus;}
    public void SetMailStatus(int TimeExp){
        TimeOfEv timeExp = new TimeOfEv();
        timeExp = timeExp.MinInTime(TimeExp);
        if (mailCount <= 0) mailStatus = true;
        else SetTimeSent(timeExp);
    }
    public TimeOfEv GetTimeBegin(){return beginOfEv;}
    public int GetPriority(){return priority;}
    public int GetID(){return id;}
    public String GetPlace(){return place;}
    public TimeOfEv GetTimeEnd(){return endOfEv;}
    public EventStatus Status(TimeOfEv timeExp){//статус события - прошло, происходит, произойдет в будущем
        if (endOfEv.FirstLessSecond(timeExp) && beginOfEv.FirstLessSecond(timeExp)) return EventStatus.after;
        else if (timeExp.FirstLessEqSecond(endOfEv) && beginOfEv.FirstLessEqSecond(timeExp)) return EventStatus.now;
        else return EventStatus.before;
    }
    public EventStatus Status(int timeExp){
        TimeOfEv time = new TimeOfEv();
        time = time.MinInTime(timeExp);
        return Status(time);
    }
    public IntersectConflict Intersects(Event another){
        if (endOfEv.FirstLessSecond(beginOfEv) || beginOfEv.SecondLessFirst(another.endOfEv)){
            return IntersectConflict.NONE;
        }
        else{
            for (int i = 0; i < namesOfPeople.size(); i++){
                for (int j = 0; j < another.namesOfPeople.size(); j++){
                    if (another.namesOfPeople.get(j).Name().equals(namesOfPeople.get(i).Name())){
                        return IntersectConflict.person;
                    }
                }
            }
            if (place == another.place){
                return IntersectConflict.place;
            }
            return IntersectConflict.NONE;
        }
    }

    public Event(){};
    public Event(TimeOfEv begin, TimeOfEv end, int prior, int num, String pl, Vector<Person> names, int id, int time){
        place = pl;
        beginOfEv = begin;
        endOfEv = end;
        priority = prior;
        numberOfPeople = num;
        namesOfPeople = names;
        this.id = id;
        SetMailStatus(time);
    }
    public Event(int begin, int end){
        beginOfEv = beginOfEv.MinInTime(begin);
        endOfEv = endOfEv.MinInTime(end);
        place = "" ;
        priority = 0;
        numberOfPeople = 0;
        namesOfPeople = new Vector<Person>();
        id = 0;
    }
    public boolean FirstLessSec(Event rhs){
        if (beginOfEv.TimeInMin(beginOfEv) != rhs.beginOfEv.TimeInMin(beginOfEv)){
            return beginOfEv.TimeInMin(beginOfEv) < rhs.beginOfEv.TimeInMin(beginOfEv);
        }
        else{
            return id < rhs.id;
        }
    }

}
