import com.opencsv.CSVReader;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

public class Main {
    public static String PATH;// = "E:\\Дгту\\Современные технологии программирования\\";


    private static final String url = "jdbc:mysql://localhost:3306/university";
    private static final String user = "root";
    private static final String password = "";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        java.sql.DatabaseMetaData db = connection.getMetaData();
        if (db.supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE))
        {
            connection.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
        }
        return connection;
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            System.out.println("More than 0 args");
            PATH = args[0];
        } else {
            System.out.println("0 args");
            PATH = "E:\\Дгту\\Современные технологии программирования\\data.csv";
        }
        FileWriter writer = new FileWriter(PATH.substring(0, PATH.lastIndexOf('\\') + 1) + "log.txt", true);
        try {

            //Считывание данных из файла
            ArrayList<Teacher> teachers = new ArrayList<>();
            ArrayList<Student> students = new ArrayList<>();
            readFile(PATH, new Class[]{Student.class, Teacher.class},
                    new ArrayList[]{students, teachers}, writer);

            // opening database connection to MySQL server
            con = getConnection();
            System.out.println("Enter a lastName");
            Scanner in = new Scanner(System.in);
            List<Student> filteredStudentsByLastName = Student.getFilteredStudentsByLastName(con, in.nextLine());
            filteredStudentsByLastName.forEach(s-> System.out.println("s.lastname = " + s.lastname));
            List<Student> dbStudents = Student.getAllStudents(con);
            System.out.println("dbStudents.size() = " + dbStudents.size());
            /*teachers.forEach(teacher -> {
                try {
                    teacher.saveToDB(con);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });*/

            /*// getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            String query = " SELECT ";
            rs = stmt.executeQuery(query);*/


        } catch (Throwable e) {
            //
            writer.write("\n" + e.getClass().toString() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            writer.close();

            //close connection ,stmt and resultset here
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            //try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            //try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
            System.out.println("Done");
        }
    }

    private static <T extends Person> void readFile(String path,
                                                    Class<T>[] classes,
                                                    ArrayList[] lists,
                                                    FileWriter writer) throws Throwable {
        try {
            //Запись содержимого в файл
            writer.write("Start read file: " + path + "\n");
            //
            CSVReader reader = null;
            //Установка кодировки
            reader = new CSVReader(new InputStreamReader(new FileInputStream(path), Charset.forName("windows-1251")));
            //считываем все строки
            List<String[]> allRows = reader.readAll();
            int i = 0;
            int indexOfClass = 0;
            while (i < allRows.size()) {
                Class<T> aClass = classes[indexOfClass];
                Constructor<T> constructor = aClass.getConstructor(String[].class);

                while (i < allRows.size() && allRows.get(i).length > 1) {
                    String[] row = allRows.get(i);
                    T t = constructor.newInstance((Object) row);
                    lists[indexOfClass].add(t);
                    writer.write(t.toString() + "\n");
                    i++;
                }

                i++;
                indexOfClass++;
            }

        } catch (Exception e) {
            //вывод ошибки в файл
            writer.write("Catch exception when read file " + path);
            throw e instanceof InvocationTargetException ? e.getCause() : e;
        }
        //
        writer.write("Success read file: " + path + "\n");
    }

    //дженерикс
    public static <T extends Person> ArrayList<T> readFile(String path, Class<T> tClass, FileWriter writer)
            throws Throwable {
        ArrayList<T> persons = new ArrayList<>();

        return persons;
    }

    //сериализация
    public static void serialize(Object obj, String path, FileWriter writer) throws IOException {
        FileOutputStream outputStream = null;
        writer.write(obj + " start seriallize to path: " + path + "\n");
        try {
            //создание 2-ух потоков и сохраняем их в файл
            outputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            //сохранение
            objectOutputStream.writeObject(obj);
            //Закрытие потока и освобождение ресурсов
            objectOutputStream.close();
            writer.write(" Seriallized success; \n");

        } catch (Exception e) {
            writer.write(obj + " seriallized failed; \n");
            //0шибка уходит
            throw e;
        }
    }

    //Дессириализация
    public static <T> T deserialize(String path, FileWriter writer) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        try {
            writer.write(" start deseriallize to path: " + path + "\n");
            fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            writer.write(" Deseriallized success; \n");
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            writer.write(path + " Deseriallized failed; \n");
            //0шибка уходит
            throw e;
        }
    }
}
