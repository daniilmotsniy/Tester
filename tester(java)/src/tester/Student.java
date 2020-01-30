package tester;


public class Student  {

    private String name;
    private String group;
    private String start_test;
    private String finish_test;

    Student(String name, String group, String start_test){
        this.name = name;
        this.group = group;
        this.start_test = start_test;
    }

    public String getGroup() {
        return group;
    }

    public String getName(){
        return name;
    }

}
