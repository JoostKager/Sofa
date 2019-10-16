package team2.sofa.sofa.service;

public abstract class PdqResponseRecord {
    private boolean approved;

    public PdqResponseRecord() {
        super();
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
