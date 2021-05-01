import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person  implements Specializable {

    private static final String TABLE_NAME = "teacher";

    public static class Fields extends Person.Fields {
        public static final String NAME_OF_SUBJECT = "name_of_subject";
        public static final String WORK_EXPERIENCE = "work_experience";
    }
    public static class Queries {
        public static final String SAVE_QUERY = "INSERT INTO teacher (last_name, age, work_experience, name_of_subject) VALUES ('%s', %d, %d, '%s')";
        public static final String FILTERED_QUERY = "select * from " + Teacher.TABLE_NAME + " where 1=1 ";
    }

    protected String nameOfSubject;
    protected int workExperience;

    public Teacher() {
        super();
    }

    public Teacher(String[] info) {
        super(info);
        if (info.length > 4) {
            nameOfSubject = info[3];
            workExperience = Integer.parseInt(info[4]);
        } else {
            throw new IllegalArgumentException("Not enough parameters for teacher");
        }
    }

    public String getNameOfSubject() {
        return nameOfSubject;
    }
    public void setNameOfSubject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }

    public int getWorkExperience() {
        return workExperience;
    }
    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }

    public void saveToDB(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.executeUpdate(String.format(Queries.SAVE_QUERY,
                lastName, age, workExperience, nameOfSubject
        ));
        statement.close();
    }

    @Override
    public String getSpecialization() {
        return nameOfSubject;
    }

    public static List<Teacher> getAllTeachers(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(Teacher.Queries.FILTERED_QUERY);
        List<Teacher> teachers = new ArrayList<>();
        while (resultSet.next()){
            Teacher teacher = new Teacher();
            teacher.setWorkExperience(resultSet.getInt(Fields.WORK_EXPERIENCE));
            teacher.setLastName(resultSet.getString(Teacher.Fields.LAST_NAME));
            teacher.setAge(resultSet.getInt(Teacher.Fields.AGE));
            teacher.setNameOfSubject(resultSet.getString(Fields.NAME_OF_SUBJECT));
            teacher.setId(resultSet.getInt(Teacher.Fields.ID));
            teachers.add(teacher);
        }
        return teachers;
    }

    @Override
    public String toString() {
        return super.toString() + "This person is a teacher with a " + workExperience +" years of experience in " + nameOfSubject + ".";
    }
}
