package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import team2.sofa.sofa.model.*;
import team2.sofa.sofa.model.dao.AccountDao;
import team2.sofa.sofa.model.dao.ClientDao;
import team2.sofa.sofa.model.dao.TransactionDao;

import java.math.BigDecimal;
import java.util.List;

@Service
public class    FundTransfer {

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    ClientDao clientDao;

    public FundTransfer() {
        super();
    }

    /**
     * wordt gebruikt om form in Money_transfer voor te bereiden
     *
//     * @param id
     * @param model
     * @return
     */
    public Model readyTransaction(@ModelAttribute Account account, Model model) {
        TransactionForm t = new TransactionForm();
        t.setDebetAccount(account.getIban());
        model.addAttribute("transactionForm", t);
        model.addAttribute("account", account);
        return model;
    }
    public Model returnToTransaction(TransactionForm transactionForm, Model model) {
        String iban = transactionForm.getDebetAccount();
        Account a = accountDao.findAccountByIban(iban);
        transactionForm.setDebetAccount(a.getIban());
        model.addAttribute("transactionForm", transactionForm);
        model.addAttribute("account", a);
        return model;
    }

    public Model prepareConfirmation(TransactionForm transactionForm, Model model){
        Account account = accountDao.findAccountByIban(transactionForm.getDebetAccount());
        model.addAttribute("account", account);
        model.addAttribute("transaction", transactionForm);
        return model;
    }

    /**
     * helper methode om juiste data in te laden in model voor dashboard na uitvoeren van betaling
     *
     * @param transactionForm
     * @return
     */
    public Account readyDashboard(TransactionForm transactionForm) {
        Account a = accountDao.findAccountByIban(transactionForm.getDebetAccount());
        return a;
    }

    /**
     * verwerkt transactie nadat alles is gecontroleerd op juiste input
     *
     * @param transactionForm
     */
    public void procesTransaction(TransactionForm transactionForm) {
        BigDecimal amount = transactionForm.getAmount();
        Account debit = accountDao.findAccountByIban(transactionForm.getDebetAccount());
        Account credit = accountDao.findAccountByIban(transactionForm.getCreditAccount());
        Transaction transaction = new Transaction(amount, transactionForm.getDescription(), credit, debit);
        storeTransaction(debit, credit, transaction);
    }

    /**
     * helper methode die wordt gebruikt door procesTransaction() om de Transactie daadwerkelijk in DB op te slaan
     * en de balance in debit en credit rekening aan te passen en te updaten in DB
     *
     * @param debit
     * @param credit
     * @param transaction
     */
    public int storeTransaction(Account debit, Account credit, Transaction transaction) {
        debit.addTransaction(transaction);
        credit.addTransaction(transaction);
        debit.lowerBalance(transaction.getAmount());
        credit.raiseBalance(transaction.getAmount());
        transactionDao.save(transaction);
        accountDao.save(debit);
        accountDao.save(credit);
        return transaction.getId();
    }

    /**
     * methode die vanuit controller wordt aangeroepen voor controle op voldoende saldo op debit rekening
     *
     * @param transactionForm afkomstig vanuit controller PaymentController
     * @return
     */
    public boolean insufficientBalance(TransactionForm transactionForm) {
        BigDecimal amount = transactionForm.getAmount();
        Account account = accountDao.findAccountByIban(transactionForm.getDebetAccount());
        return insufficientBalance(amount, account);
    }

    /**
     * methode die daadwerkelijk controleert of er voldoende saldo is op debitrekening
     *
     * @param amount  bedrag dat is ingevoerd in formulier money_transfer
     * @param account het debitAccount dat gebruikt is in het formulier money_transfer
     * @return
     */
    public boolean insufficientBalance(BigDecimal amount, Account account) {
        BigDecimal balance = account.getBalance();
        BigDecimal change = balance.subtract(amount);
        double check = change.doubleValue();
        return (check < 0);
    }

    /**
     * controle of ingevoerd IBAN creditAccount een bestaand IBAN is
     *
     * @param creditIban het IBAN dat is ingevoerd in money_transfer form in veld CreditAccount
     * @return
     */
    public boolean checkToAccount(String creditIban) {
        return accountDao.existsAccountByIban(creditIban);
    }

    public boolean nameCheckIban(String lastName, String creditIban) {
        Account creditAccount = accountDao.findAccountByIban(creditIban);
        if (creditAccount.isBusinessAccount()){
            if (creditAccount.getNameOwner().equals(lastName)){
                return true;
            }
        }
        List<Client> owners = creditAccount.getOwners();
        for (Client owner : owners) {
            if (owner.getLastName().equals(lastName)) {
                return true;
            }
        }
        return false;
    }

}
