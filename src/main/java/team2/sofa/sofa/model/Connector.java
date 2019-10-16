package team2.sofa.sofa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Connector {
    @Id
    @GeneratedValue(generator = "CON_SEQ")
    private int id;
    private String username;
    private String securityCode;
    private String iban;

    public Connector(int id, String username, String securityCode, String iban) {
        this.id = id;
        this.username = username;
        this.securityCode = securityCode;
        this.iban = iban;
    }
    public Connector(String username, String securityCode, String iban) {
        this.id = 0;
        this.username = username;
        this.securityCode = securityCode;
        this.iban = iban;
    }

    public Connector() {
        this("","","");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
