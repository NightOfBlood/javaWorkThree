import com.opencsv.CSVReader;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

public class Main {
    public static String PATH;// = "E:\\Дгту\\Современные технологии программирования\\";


    private static final String url = "jdbc:mysql://localhost:3306/university";
    private static final String user = "root";
    private static final String password = "";
    public static final Scanner sc = new Scanner(System.in);

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        java.sql.DatabaseMetaData db = connection.getMetaData();
        if (db.supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE)) {
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
            ArrayList<Teacher> teachers = new ArrayList<>();
            ArrayList<Student> students = new ArrayList<>();
            readFile(PATH, new Class[]{Student.class, Teacher.class},
                    new ArrayList[]{students, teachers}, writer);

            con = getConnection();
            mainMenu(con);

            /*teachers.forEach(teacher -> {
                try {
                    teacher.saveToDB(con);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });*/

        } catch (Throwable e) {
            //
            writer.write("\n" + e.getClass().toString() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            writer.close();
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            //try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            //try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
            System.out.println("Done");
        }
    }

    public static void mainMenu(Connection connection) throws SQLException {
        while (true) {
            System.out.println("Choose next operation:");
            System.out.println("0) Exit");
            System.out.println("1) Get All Students");
            System.out.println("2) Get All Teachers");
            System.out.println("3) Get Filtered Students");
            System.out.println("4) Get Filtered Teachers");
            System.out.println("5) Add new Student");
            int res = readInt();
            switch (res) {
                case 0:
                    return;
                case 1:
                    List<Student> allStudents = Student.getAllStudents(connection);
                    allStudents.forEach(System.out::println);
                    break;
                case 2:
                    List<Teacher> allTeachers = Teacher.getAllTeachers(connection);
                    allTeachers.forEach(System.out::println);
                    break;
                case 3:
                    filterStudents(connection);
                case 5:
                    addStudent(connection);
                default:
            }
        }
    }

    private static void addStudent(Connection connection) throws SQLException {
        Student student = new Student();
        System.out.println("Last name:");
        student.setLastName(readString());
        System.out.println("Age:");
        student.setAge(sc.nextInt());
        student.saveToDB(connection);
        System.out.println("student = " + student + "successfully saved");
    }

    private static void filterStudents(Connection connection) throws SQLException {
        StringBuilder restriction = new StringBuilder();
        System.out.println("Filter student by last name?");
        if (checkAnswer()) {
            System.out.println("Enter a part of last name?");
            String nextLine = readString();
            Person.addLikeRestriction(restriction, Student.Fields.LAST_NAME, nextLine);
        }
        System.out.println("Filter student by age?");
        if (checkAnswer()) {
            System.out.println("Enter an age");
            int nextLine = readInt();
            Person.addIntEqualRestriction(restriction, Student.Fields.AGE, nextLine);
        }
        List<Student> allStudents = Student.getFilteredStudents(connection, restriction.toString());
        allStudents.forEach(System.out::println);
    }

    private static int readInt(){
        while(true){
            try {
                String str = sc.nextLine();
                return Integer.parseInt(str);
            } catch (NumberFormatException ex){
                System.out.println("Please, enter a correct integer");
            }
        }
    }
    private static String readString(){
        while(true){
            String s = sc.nextLine();
            if(!s.equals("")){
                return s;
            }
        }
    }
    private static boolean checkAnswer() {
        while (true) {
            String s = readString();
            if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes")) {
                return true;
            } else if (s.equalsIgnoreCase("n") || s.equalsIgnoreCase("no")) {
                return false;
            } else {
                System.out.println("Wrong answer. Say yes or no (y/n)");
            }
        }
    }

    private static <T extends Person> void readFile(String path,
                                                    Class<T>[] classes,
                                                    ArrayList[] lists,
                                                    FileWriter writer) throws Throwable {
        try {
            writer.write("Start read file: " + path + "\n");

            CSVReader reader = null;
            reader = new CSVReader(new InputStreamReader(new FileInputStream(path), Charset.forName("windows-1251")));
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

            writer.write("Catch exception when read file " + path);
            throw e instanceof InvocationTargetException ? e.getCause() : e;
        }
        //
        writer.write("Success read file: " + path + "\n");
    }

    public static <T extends Person> ArrayList<T> readFile(String path, Class<T> tClass, FileWriter writer)
            throws Throwable {
        ArrayList<T> persons = new ArrayList<>();

        return persons;
    }

    public static void serialize(Object obj, String path, FileWriter writer) throws IOException {
        FileOutputStream outputStream = null;
        writer.write(obj + " start seriallize to path: " + path + "\n");
        try {
            outputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
            writer.write(" Seriallized success; \n");

        } catch (Exception e) {
            writer.write(obj + " seriallized failed; \n");
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
            throw e;
        }
    }
}
