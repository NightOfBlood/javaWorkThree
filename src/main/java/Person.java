public class Person   {
    protected int id;
    protected String lastName;
    protected int age;
    protected String dateOfBirth;

    public Person(){

    }

    public Person(String[] info) {
        if (info.length > 2) {
            lastName = info[0];
            age = Integer.parseInt(info[1]);
            dateOfBirth = info[2];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "This person lastname is " + lastName + "; age is " + age + "; dateofbirth is " + dateOfBirth + "; ";
    }

    public static StringBuilder addIntEqualRestriction(StringBuilder restriction, String field, int value) {
        return restriction.append(" And ").append(field).append(" = ").append(value).append(" ");
    }
    public static StringBuilder addLikeRestriction(StringBuilder restriction, String field, String value) {
        return restriction.append(" And ").append(field).append(" like '%").append(value).append("%' ");
    }

    public static class Fields {
        public static final String LAST_NAME = "last_name";
        public static final String AGE = "age";
        public static final String ID = "id";
    }
}
