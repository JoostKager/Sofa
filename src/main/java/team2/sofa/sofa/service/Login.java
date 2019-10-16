package team2.sofa.sofa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;
import team2.sofa.sofa.model.*;
import team2.sofa.sofa.model.dao.AccountDao;
import team2.sofa.sofa.model.dao.ClientDao;
import team2.sofa.sofa.model.dao.ConnectorDao;
import team2.sofa.sofa.model.dao.EmployeeDao;

import java.util.ArrayList;



@Service
@SessionAttributes({"nrBusiness", "nrPrivate", "sessionemployee"})
public class Login {

    @Autowired
    AccountDao accountDao;
    @Autowired
    ClientDao clientDao;
    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    TopTenHighestBalanceFinder topTenHighestBalanceFinder;
    @Autowired
    TopTenMostActiveClient topTenMostActiveClient;
    @Autowired
    SectorAnalyzer sectorAnalyzer;
    @Autowired
    ConnectorDao connectorDao;
    
    public Login() {
        super();
    }

    public void checkAndLoadConnector(Client loggedInClient, Model model){
        if (connectorDao.existsConnectorByUsername(loggedInClient.getUsername())) {
            model.addAttribute("connect", connectorDao.findConnectorsByUsername(loggedInClient.getUsername()));
        } else{
            model.addAttribute("connect", new ArrayList<>());
        }
    }

    public int countPrivateAccounts(Client client){
        int count = 0;
        for (Account account: client.getAccounts()) {
            if (!account.isBusinessAccount()){
                count++;
            }
        }
        return count;
    }

    public int countBusinessAccounts(Client client){
        int count = 0;
        for (Account account: client.getAccounts()) {
            if (account.isBusinessAccount()){
                count++;
            }
        }
        return count;
    }

    static void splitPrivateAndBusiness(Client loggedInClient, Model model) {
        ArrayList<Account> listPrivateAccounts = new ArrayList<>();
        ArrayList<Account> listBusinessAccounts = new ArrayList<>();
        for (Account a : loggedInClient.getAccounts()
        ) {
            if (a.isBusinessAccount()) {
                listBusinessAccounts.add(a);
            } else {
                listPrivateAccounts.add(a);
            }
        }
        model.addAttribute("listPrivateAccounts", listPrivateAccounts);
        model.addAttribute("listBusinessAccounts", listBusinessAccounts);
    }

    public Employee employeeLogin(Employee employee, Model model) {
        Employee currentEmployee = employeeDao.findEmployeeByUsername(employee.getUsername());
        model.addAttribute("sessionemployee", currentEmployee);
        return currentEmployee;
    }
}
