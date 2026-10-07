package sample.model;

public class Student {

    private final String name;
    private final String ssn;
    private final String mail;
    private final String password;

    public Student(
            String name,
            String ssn,
            String mail,
            String password
    ) {
        this.name = name;
        this.ssn = ssn;
        this.mail = mail;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}