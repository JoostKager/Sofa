package team2.sofa.sofa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Employee extends User {

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private EmployeeRole role;


    public Employee() {
        this(0, "", "", "", null, "", "",
                "", null, "", "", "");
    }

    public Employee(int id, String firstName, String prefix, String lastName, Address address,
                    String SSN, String email, String telephoneNr, String birthday, String gender,
                    String username, String password) {
        super(id, firstName, prefix, lastName, address, SSN, email, telephoneNr, birthday, gender);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }
}
