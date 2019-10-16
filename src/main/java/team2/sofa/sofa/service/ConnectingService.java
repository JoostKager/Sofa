package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.Connector;
import team2.sofa.sofa.model.dao.AccountDao;
import team2.sofa.sofa.model.dao.ClientDao;
import team2.sofa.sofa.model.dao.ConnectorDao;

import java.util.List;
import java.util.Map;

@Service
public class ConnectingService {

    @Autowired
    AccountDao accountDao;
    @Autowired
    ClientDao clientDao;
    @Autowired
    ConnectorDao connectorDao;

    public Connector getConnection(int id){
        return connectorDao.findById(id).get();
    }
    public List<Connector> getConnectionUsername(String username){return connectorDao.findConnectorsByUsername(username);}
    public Account getAccount(int id) {
        return accountDao.findAccountById(id);
    }
    public Account getAccountbyIBAN(String iban){return accountDao.findAccountByIban(iban);}
    public Account saveCoupling(Map body){
        String iban = body.get("bankaccount").toString();
        String user = body.get("newuser").toString();
        String accescode = body.get("accesscode").toString();
        Connector connector = new Connector(user,accescode,iban);
        connectorDao.save(connector);
        return accountDao.findAccountByIban(iban);
    }
    public void processCoupling(Connector connector){
        Account account = accountDao.findAccountByIban(connector.getIban());
        Client client = clientDao.findClientByUsername(connector.getUsername());
        account.addClient(client);
        client.addAccount(account);
        clientDao.save(client);
        accountDao.save(account);
        connectorDao.delete(connector);
    }

    public boolean checkUserName(Map<String, String> body){
        //check of user bestaat
        if (clientDao.findClientByUsername(body.get("newuser")) == null) return false;

        //check op eigen usernaam
        List<Client> owners = accountDao.findAccountByIban(body.get("bankaccount")).getOwners();
        for (Client client: owners){
            if (client.getUsername().equals(body.get("newuser"))) return false;
        }
        return true;
    }
    public void changeUsername(String oldUsername, String newUsername){
        List<Connector> list = connectorDao.findConnectorsByUsername(oldUsername);
        for (Connector connection : list) {
            connection.setUsername(newUsername);
            connectorDao.save(connection);
        }
    }
}
