package team2.sofa.sofa.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PdqResponseOkRecord extends PdqResponseRecord {
    private BigDecimal amount;
    private String debetAccount;
    private String creditAccount;
    private LocalDate date;
    private int transactionId;


    public PdqResponseOkRecord(){
        super();
        super.setApproved(true);
    }

    public PdqResponseOkRecord(BigDecimal amount, String debetAccount,
                               String creditAccount, LocalDate date, int transactionId) {
        this();
        this.amount = amount;
        this.debetAccount = debetAccount;
        this.creditAccount = creditAccount;
        this.date = date;
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDebetAccount() {
        return debetAccount;
    }

    public void setDebetAccount(String debetAccount) {
        this.debetAccount = debetAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
