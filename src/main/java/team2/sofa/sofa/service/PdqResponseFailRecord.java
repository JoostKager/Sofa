package team2.sofa.sofa.service;

public class PdqResponseFailRecord extends PdqResponseRecord{

    private String reasonNotApproved;

    public PdqResponseFailRecord(){
        super();
        super.setApproved(false);
    }

    public PdqResponseFailRecord(String reasonNotApproved) {
        this();
        this.reasonNotApproved = reasonNotApproved;
    }

    public PdqResponseFailRecord(boolean approved, String reasonNotApproved) {
        super();
        this.reasonNotApproved = reasonNotApproved;
    }

    public String getReasonNotApproved() {
        return reasonNotApproved;
    }

    public void setReasonNotApproved(String reasonNotApproved) {
        this.reasonNotApproved = reasonNotApproved;
    }
}
