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
        public static final String COURSE = "course";
        public static final String FAVORITE_SUBJECT = "favorite_subject";
    }
    public static class Queries {
        public static final String SAVE_QUERY = "INSERT INTO student ( last_name, age, group_name, course, favorite_subject) VALUES ('%s', %d, '%s', %d, '%s')";
        public static final String FILTERED_QUERY = "select * from " + TABLE_NAME + " where 1=1 ";
    }

    protected String group;
    protected int course;
    protected String favoriteSubject;

    public Student() {
        super();
    }

    public Student(String[] info) {
        super(info);
        if (info.length <= 5) {
            throw new IllegalArgumentException("Not enough parameters for student");
        } else {
            group = info[3];
            course = Integer.parseInt(info[4]);
            favoriteSubject = info[5];
        }
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    public int getCourse() {
        return course;
    }
    public void setCourse(int course) {
        this.course = course;
    }

    public String getFavoriteSubject() {
        return favoriteSubject;
    }
    public void setFavoriteSubject(String favoriteSubject) {
        this.favoriteSubject = favoriteSubject;
    }

    public void saveToDB(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.executeUpdate(String.format(Queries.SAVE_QUERY,
                lastName, age, group, course, favoriteSubject
        ));
        statement.close();
    }

    public static List<Student> getAllStudents(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(Queries.FILTERED_QUERY);
        List<Student> students = new ArrayList<>();
        while (resultSet.next()){
            students.add(initializeStudent(resultSet));
        }
        return students;
    }

    public static List<Student> getFilteredStudents(Connection con, String restrictions) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(Queries.FILTERED_QUERY + restrictions);
        List<Student> students = new ArrayList<>();
        while (resultSet.next()){
            students.add(initializeStudent(resultSet));
        }
        return students;
    }

    /*public static List<Student> getFilteredStudentsByLastName(Connection con, String lastName) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(addLikeRestriction(Queries.FILTERED_QUERY, Fields.LAST_NAME,lastName));
        List<Student> students = new ArrayList<>();
        while (resultSet.next()){
            students.add(initializeStudent(resultSet));
        }
        return students;
    }*/

    private static Student initializeStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setCourse(resultSet.getInt(Fields.COURSE));
        student.setLastName(resultSet.getString(Fields.LAST_NAME));
        student.setAge(resultSet.getInt(Fields.AGE));
        student.setGroup(resultSet.getString(Fields.GROUP));
        student.setFavoriteSubject(resultSet.getString(Fields.FAVORITE_SUBJECT));
        student.setId(resultSet.getInt(Fields.ID));
        return student;
    }


    @Override
    public String toString() {
        return super.toString() + "This person is a student with a " + group + " course " + course + " favorite sibject is "
                + favoriteSubject ;
    }
}