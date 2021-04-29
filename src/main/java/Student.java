import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person implements Specializable {

    public static final String TABLE_NAME = "student";

    @Override
    public String getSpecialization() {
        return favoriteSubject;
    }

    public static class Fields extends Person.Fields {
        public static final String GROUP = "group_name";
    }
    public static class Queries {
        public static final String SAVE_QUERY = "INSERT INTO student (last_name, age, group_name, course, favorite_subject) VALUES ('%s', %d, '%s', %d, '%s')";
        public static final String FILTERED_QUERY = "select * from " + TABLE_NAME + " where 1=1 ";
    }

    protected String group;
    protected int course;
    protected String favoriteSubject;

    //вызов конструктора Student
    public Student() {
        super();
    }

    //обработка ошибочных данных
    public Student(String[] info) {
        // считывание из person
        super(info);
        //Проверка
        if (info.length <= 5) {
            throw new IllegalArgumentException("Not enough parameters for student");
        } else {
            group = info[3];
            course = Integer.parseInt(info[4]);
            favoriteSubject = info[5];
        }
    }

    //Возвращение значения заданного поля
    public String getGroup() {
        return group;
    }

    //Передача аргумента в качестве строки и присвоение значения поля
    public void setGroup(String group) {
        this.group = group;
    }

    //Возвращение значения заданного поля
    public int getCourse() {
        return course;
    }

    //Передача аргумента в качестве строки и присвоение значения поля
    public void setCourse(int course) {
        this.course = course;
    }

    //Возвращение значения заданного поля
    public String favoriteSubject() {
        return favoriteSubject;
    }

    //Передача аргумента в качестве строки и присвоение значения поля
    public void favoriteSubject(String favoriteSubject) {
        this.favoriteSubject = favoriteSubject;
    }

    public void saveToDB(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.executeUpdate(String.format(Queries.SAVE_QUERY,
                lastname, age, group, course, favoriteSubject
        ));
        statement.close();
    }

    public static List<Student> getAllStudents(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(Queries.FILTERED_QUERY);
        List<Student> students = new ArrayList<>();
        while (resultSet.next()){
            Student student = new Student();
            student.setCourse(resultSet.getInt("course"));
            student.setLastname(resultSet.getString("last_name"));
            /*student.setCourse(resultSet.getInt("course"));
            student.setCourse(resultSet.getInt("course"));*/
            students.add(student);
        }
        return students;
    }


    public static List<Student> getFilteredStudentsByLastName(Connection con, String lastName) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(addLikeRestriction(Queries.FILTERED_QUERY, Fields.LAST_NAME,lastName));
        List<Student> students = new ArrayList<>();
        while (resultSet.next()){
            Student student = new Student();
            student.setCourse(resultSet.getInt(Fields.LAST_NAME));
            student.setLastname(resultSet.getString(Fields.LAST_NAME));
            /*student.setCourse(resultSet.getInt("course"));
            student.setCourse(resultSet.getInt("course"));*/
            students.add(student);
        }
        return students;
    }


    @Override
    public String toString() {
        return super.toString() + "This person is a teacher with a " + group + " years of experience in " + course + "//rjvv"
                + favoriteSubject + "dsd";
    }
}