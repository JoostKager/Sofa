package team2.sofa.sofa.model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class PrivateAccount extends Account {

    public PrivateAccount() {
        super();
        super.setBusinessAccount(false);
    }

    public PrivateAccount(String iban, BigDecimal balance){
        super(iban,balance);
    }

}
