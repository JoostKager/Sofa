package team2.sofa.sofa.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BusinessAccount extends Account {

    @OneToMany
    private List<Pdq> coupledPdqMachines;
    @ManyToOne
    private Business business;

    public BusinessAccount() {
        super();
        this.coupledPdqMachines = new ArrayList<>();
        this.business = null;
        super.setBusinessAccount(true);
    }

    public BusinessAccount(String iban, BigDecimal balance) {
        super(iban, balance);
        super.setBusinessAccount(true);
    }

    public BusinessAccount(List<Pdq> coupledPdqMachines, Business business) {
        this();
        this.coupledPdqMachines = coupledPdqMachines;
        this.business = business;
        super.setBusinessAccount(true);
    }

    public List<Pdq> getCoupledPdqMachines() {
        return coupledPdqMachines;
    }

    public void setCoupledPdqMachines(List<Pdq> coupledPdqMachines) {
        this.coupledPdqMachines = coupledPdqMachines;
    }

    public void addPdqMachine(Pdq pdqMachine) {
        this.coupledPdqMachines.add(pdqMachine);
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

}
