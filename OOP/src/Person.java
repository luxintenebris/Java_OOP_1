public class Person {
    private String name;
    private String mail;
    private String department; //отдел, к которому человек относится

    public String Name() {
        return name;
    }
    public String Mail() {
        return mail;
    }
    public String Department() {
        return department;
    }
    public String MailPerson(){
        return mail + " (" + name + ") ";
    }
    public Person(String n, String m, String d){
        this.name = n;
        this.mail = m;
        this.department = d;
    }
    public Person(){};
}
