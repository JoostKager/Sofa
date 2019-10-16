package team2.sofa.sofa.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.model.Business;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.service.Clientview;
import team2.sofa.sofa.service.Login;


@Controller
@SessionAttributes({"sessionclient", "connect", "nrBusiness", "nrPrivate", "newaccountid", "account", "business"})
public class ClientViewController {

    @Autowired
    Clientview clientview;
    @Autowired
    Login login;

    /**
     * methode om elementen voor client_view.html te vullen.
     *
     * @param username
     * @param model
     */
    public void fillClientView(String username, Model model) {
        Client loggedInClient = clientview.findClientByUsername(username);
        model.addAttribute("sessionclient", loggedInClient);
        login.checkAndLoadConnector(loggedInClient, model);
        model.addAttribute("nrBusiness", login.countBusinessAccounts(loggedInClient));
        model.addAttribute("nrPrivate", login.countPrivateAccounts(loggedInClient));
        Hibernate.initialize(loggedInClient.getAccounts());
    }

    @GetMapping(value = "rekeningenoverzicht")
    public String loadClientView(@ModelAttribute("clientUsername") String username,
                                 @ModelAttribute("sessionclient") Client client,
                                 Model model) {
        if (!username.equals("")) {
            fillClientView(username, model);
            return "client_view";
        }
        fillClientView(client.getUsername(), model);
        return "client_view";
    }

    @PostMapping(value = "accountListHandler")
    public String accountListHandler(@RequestParam int id, RedirectAttributes redirectAttributes, Model model) {
        Account account = clientview.FindAccountById(id);
        model.addAttribute("account", new Account());
        redirectAttributes.addFlashAttribute("acc", account);
        Hibernate.initialize(account.getTransactions());
        return "redirect:/rekeningdetails";
    }

    @PostMapping(value = "addNewAccountHandler")
    public String addNewAccount(@RequestParam int id, RedirectAttributes redirectAttributes) {
        clientview.createNewPrivate(id);
        Client c = clientview.findClientById(id);
        redirectAttributes.addFlashAttribute("clientUsername", c.getUsername());
        return "redirect:/rekeningenoverzicht";
    }

    @PostMapping(value = "addNewBusinessAccountHandler")
    public String addNewBusinessAccountHandler(@RequestParam int id, Model model) {
        Client client = clientview.findClientById(id);
        Business business = new Business();
        business.setOwner(client);
        model.addAttribute("business", business);
        return "redirect:/nieuweZakelijkeRekening";
    }

    @GetMapping(value = "nieuweZakelijkeRekening")
    public String addNewBussiness() {
        return "add_business_account";
    }

    @PostMapping(value = "newBusiness")
    public String newBAccount(Business business, RedirectAttributes redirectAttributes) {
        clientview.procesNewBusinessAccount(business);
        Client c = business.getOwner();
        redirectAttributes.addFlashAttribute("clientUsername", c.getUsername());
        return "redirect:/rekeningenoverzicht";
    }
}
