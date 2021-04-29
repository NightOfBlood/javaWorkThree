import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class Person   {
    protected int id;
    protected String lastname;
    protected int age;
    protected String dateofbirth;

    //вызов конструктора Person
    public Person(){

    }
    public Person(String[] info) {
        //проверка
        if (info.length > 2) {
            lastname = info[0];
            age = Integer.parseInt(info[1]);
            dateofbirth = info[2];
        } else {
            throw new IllegalArgumentException();
        }
    }

    //Возвращение значения заданного поля
    public String getLastname() {
        return lastname;
    }
    //Передача аргумента в качестве строки и присвоение значения поля
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    //Возвращение значения заданного поля
    public int getAge() {
        return age;
    }
    //Передача аргумента в качестве строки и присвоение значения поля
    public void setAge(int age) {
        this.age = age;
    }
    //Возвращение значения заданного поля
    public String getDateofbirth() {
        return dateofbirth;
    }
    //Передача аргумента в качестве строки и присвоение значения поля
    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    @Override
    public String toString() {
        return "This person lastname is " + lastname + "; age is " + age + "; dateofbirth is " + dateofbirth + "; ";
    }

    public static String addLikeRestriction(String query, String field, String value) {
        return query + "And " + field + " like '%" + value + "%'";
    }

    public static class Fields {
        public static final String LAST_NAME = "last_name";
    }
}
