package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import team2.sofa.sofa.model.*;
import team2.sofa.sofa.model.dao.*;

import java.math.BigDecimal;

@Service
public class Clientview {

    @Autowired
    TransactionDao transactionDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    ClientDao clientDao;
    @Autowired
    BusinessDao businessDao;
    @Autowired
    Login login;
    @Autowired
    ConnectorDao connectorDao;

    public Clientview() {
        super();
    }

    public Client findClientById(int id) {
        return clientDao.findClientById(id);
    }

    public Client findClientByUsername(String userName) {
        return clientDao.findClientByUsername(userName);
    }

    public Account FindAccountById(int id) {
        Account chosenAccount = accountDao.findAccountById(id);
        return chosenAccount;
    }

    public boolean connectingIban(String iban){
        return connectorDao.existsConnectorByIban(iban);
    }

    //evt exception inbouwen voor als we nog andersoortige rekeningen krijgen.
    public Account FindPrivateOrBusinessAccountById(int id, boolean isBusiness) {
        Account chosenAccount = new Account();
        if (isBusiness) {
            chosenAccount = accountDao.findAccountById(id);
        }
        if (!isBusiness) {
            chosenAccount = accountDao.findAccountById(id);
        }
        return chosenAccount;
    }

    public void createNewPrivate(int id) {
        Client c = clientDao.findClientById(id);
        Account a = makeAccount(c);
        c.addAccount(a);
        clientDao.save(c);
        accountDao.save(a);
    }

    public Account makeAccount(Client client) {
        IBANGenerator ibanGenerator = new IBANGenerator();
        String iban = ibanGenerator.ibanGenerator();
        Account a = new PrivateAccount(iban, new BigDecimal(0.00));
        a.addClient(client);
        return a;
    }

    public Account procesNewBusinessAccount(Business business) {
        IBANGenerator ibanGenerator = new IBANGenerator();
        String iban = ibanGenerator.ibanGenerator();
        businessDao.save(business);
        Client c = business.getOwner();
        BusinessAccount a = new BusinessAccount();
        a.addClient(c);
        a.setIban(iban);
        a.setBusiness(business);
        a.setBalance(new BigDecimal(0.00));
        c.addAccount(a);
        clientDao.save(c);
        accountDao.save(a);
        return a;
    }
}


