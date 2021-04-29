import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Teacher extends Person  implements Specializable {

    private static final String TABLE_NAME = "teacher";

    public static class Queries {
        public static final String SAVE_QUERY = "INSERT INTO teacher (last_name, age, work_experience, name_of_subject) VALUES ('%s', %d, %d, '%s')";
        public static final String FILTERED_QUERY = "select * from " + Teacher.TABLE_NAME + " where 1=1 ";
    }

    protected String nameOfSubject;
    protected int workExperience;

    //вызов конструктора Teacher
    public Teacher() {
        super();
    }

    public Teacher(String[] info) {
        super(info);
        //Проверка
        if (info.length > 4) {
            nameOfSubject = info[3];
            workExperience = Integer.parseInt(info[4]);
        } else {
            //исключение
            throw new IllegalArgumentException("Not enough parameters for teacher");
        }
    }
    //Возвращение значения заданного поля
    public String getNameOfSubject() {
        return nameOfSubject;
    }
    //Передача аргумента в качестве строки и присвоение значения поля
    public void setNameOfSubject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }
    //Возвращение значения заданного поля
    public int getWorkExperience() {
        return workExperience;
    }
    //Передача аргумента в качестве строки и присвоение значения поля
    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }

    public void saveToDB(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.executeUpdate(String.format(Queries.SAVE_QUERY,
                lastname, age, workExperience, nameOfSubject
        ));
        statement.close();
    }

    /*@Override
    //Загрузка сохраненных полей
    public Object deserialize(String path) throws Throwable {
        // Вызов метода родительского класса
        Object deserialize = super.deserialize(path);
        if (deserialize == null || !(deserialize instanceof Teacher))
            return null;
        Teacher teacher = (Teacher) deserialize;
        this.nameOfSubject = teacher.nameOfSubject;
        this.workExperience = teacher.workExperience;
        return teacher;
    }*/
    //
    @Override
    public String getSpecialization() {
        return nameOfSubject;
    }
    //
    @Override
    public String toString() {
        return super.toString() + "This person is a teacher with a " + workExperience +" years of experience in " + nameOfSubject + ".";
    }
}
