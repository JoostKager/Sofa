package team2.sofa.sofa.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account {

    public static final int OWNER_INDEX = 0;

    @Id
    @GeneratedValue (generator = "ACC_SEQ")
    private int id;
    @Column(unique = true)
    private String iban;
    private BigDecimal balance;
    private String pin;
    @ManyToMany (fetch = FetchType.EAGER)
    private List<Client> owners;

    @ManyToMany (fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    private boolean isBusinessAccount;

    public Account(int id, String iban, BigDecimal balance, List<Client> owners, List<Transaction> transactions, boolean isBusinessAccount, String pin) {
        this.id = id;
        this.iban = iban;
        this.balance = balance;
        this.owners = owners;
        this.transactions = transactions;
        this.isBusinessAccount = isBusinessAccount;
    }

    public Account(int id, String iban, BigDecimal balance, String pin, List<Client> owners, List<Transaction> transactions, boolean isBusinessAccount) {
        this.id = id;
        this.iban = iban;
        this.balance = balance;
        this.pin = pin;
        this.owners = owners;
        this.transactions = transactions;
        this.isBusinessAccount = isBusinessAccount;
    }

    public Account(String iban, BigDecimal balance, String pin, List<Transaction> transactions){
        this(0, iban, balance, pin, null, transactions, false);
    }

    public Account (String iban, BigDecimal balance){
        this(0, iban, balance, "", new ArrayList<>(),new ArrayList<>(),false);
    }

    public Account(){
        this(0,"", null, "", new ArrayList<>(), new ArrayList<>(), false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Client> getOwners() {
        return owners;
    }

    public void setOwners(List<Client> owners) {
        this.owners = owners;
    }

    public void addClient(Client client){
        this.owners.add(client);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }

    public void lowerBalance(BigDecimal amount){
        BigDecimal balance = this.balance;
        BigDecimal lower = balance.subtract(amount);
        this.balance = lower;
    }
    public void raiseBalance(BigDecimal amount){
        BigDecimal balance = this.balance;
        BigDecimal higher = balance.add(amount);
        this.balance = higher;
    }

    public boolean isBusinessAccount() {
        return isBusinessAccount;
    }

    public void setBusinessAccount(boolean businessAccount) {
        isBusinessAccount = businessAccount;
    }

    public boolean getIsBusinessAccount(){
        return isBusinessAccount;
    }

    public String getFullNameAccountOwners(){
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < owners.size(); i++) {
            name.append(owners.get(i).getFullNameUser());
            if (i + 1 < owners.size()) name.append(" / ");
            }
        return name.toString();
    }

    public String getFullNameAccountOwnersExceptFirst() {
        StringBuilder name = new StringBuilder();
        if (getIsBusinessAccount()) name.append(owners.get(OWNER_INDEX).getFullNameUser());
        if (owners.size() > 1)  {
            name.append(" / ");
            for (int i = 1; i < owners.size(); i++) {
            name.append(owners.get(i).getFullNameUser());
            if (i + 1 < owners.size()) name.append(" / ");
        }}
        if (name.toString().isEmpty()) {
            return "geen";
        }
        else {return name.toString();}
    }

    public String getNameOwner() {
        if (getIsBusinessAccount()) return ((BusinessAccount) this).getBusiness().getBusinessName();
        return owners.get(OWNER_INDEX).getFullNameUser();
    }

    public List<Transaction> getTransactionsbyDateAsc(){
        Collections.sort(transactions);
        Collections.reverse(transactions);
        return transactions;
    }
}
