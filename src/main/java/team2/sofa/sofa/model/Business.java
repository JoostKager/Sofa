package team2.sofa.sofa.model;

import javax.persistence.*;

@Entity
public class Business {
    @Id
    @GeneratedValue(generator = "BUS_SEQ")
    private int id;
    private String businessName;
    @Enumerated(EnumType.STRING)
    private BusinessSector sector;
    //We chose at this time not to include a list of associated accounts here, as not current User-stories require it.
    //Connections run via account <-> client
    @OneToOne
    private Client owner;

    public Business(){
        this(0,"", BusinessSector.AGRARISCH, null);
    }

    public Business(int id, String businessName, BusinessSector sector, Client owner) {
        this.id = id;
        this.businessName = businessName;
        this.sector = sector;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public BusinessSector getSector() {
        return sector;
    }

    public void setSector(BusinessSector sector) {
        this.sector = sector;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
}
