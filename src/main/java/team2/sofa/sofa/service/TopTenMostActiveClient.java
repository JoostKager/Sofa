
package team2.sofa.sofa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.dao.ClientDao;

import java.util.*;

@Service
public class TopTenMostActiveClient {

    @Autowired
    ClientDao clientDao;

    /**
     * methode die de tien meest actieve klanten (hoogste totaal aantal transacties uit de database haalt
     *
     * @return een geordende lijst met objecten van het type Client.
     */

    public List<Client> getTopTenMostActiveClients() {
        return clientDao.findTop10ByOrderByTotalNumberOfTransactionsDesc();
    }

    /**
     * Deze methode wordt aangeroepen in EmployeeViewController, in de methode TenMostActiveHandler,
     * zodra er in employee_view_mkb.html op een naam in de lijst van meest actieve klanten wordt geklikt.
     * Ze zorgt ervoor dat de gegevens van de juiste klant worden geleden,
     * en dat de lijst met Accounts gesplitst wordt in Private en Business,
     * wat nodig is voor de functionaliteit van client_view_for_employee.html
     *
     * @param id, model, deze worden meegegeven bij het klikken op de naam.
     *@return client_view_for_employee - een nieuwe pagina wordt geladen adhv de klant die is gekoppeld aan de id.*/

    public String mostActiveClientBuilder(int id, Model model) {
        Client chosenClient = clientDao.findClientById(id);
        Login.splitPrivateAndBusiness(chosenClient, model);
        model.addAttribute("client", chosenClient);
        return "client_view_for_employee";
    }

}

