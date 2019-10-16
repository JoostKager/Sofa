package team2.sofa.sofa.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.Connector;
import team2.sofa.sofa.service.Clientview;

@Controller
@SessionAttributes({"sessionclient", "connect", "nrBusiness", "nrPrivate", "account"})
public class DashboardClientController {

    @Autowired
    ClientViewController clientViewController;
    @Autowired
    Clientview clientview;

    @PostMapping(value = "transferHandler")
    public String transfer(@RequestParam int id, Model model) {
        Account account = clientview.FindAccountById(id);
        model.addAttribute("account", account);
        return "redirect:/startTransfer";
    }


    @PostMapping(value = "backToClientView")
    public String backToClientView(@RequestParam int id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("clientUsername", clientview.findClientById(id).getUsername());
        return "redirect:/rekeningenoverzicht";
    }

    @GetMapping(value = "rekeningdetails")
    @Transactional
    public String loadDashboardClient(@ModelAttribute("acc") Account account,
                                      @ModelAttribute("account") Account sessionaccount,
                                      Model model) {
        if (account.getId() != 0) {
            model.addAttribute("account", account);
            model.addAttribute("coupling", clientview.connectingIban(account.getIban()));

        }
        if (account.getId() != sessionaccount.getId() && sessionaccount.getId() != 0) {
            sessionaccount = clientview.FindAccountById(sessionaccount.getId());
            model.addAttribute("coupling", clientview.connectingIban(sessionaccount.getIban()));
            model.addAttribute("account", sessionaccount);
        }
        return "dashboard_client";
    }
}
