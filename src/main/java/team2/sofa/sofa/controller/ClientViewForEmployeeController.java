package team2.sofa.sofa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.service.Clientview;


@Controller
@SessionAttributes({"sessionclient", "sessionemployee"})
public class ClientViewForEmployeeController {

    @Autowired
    Clientview clientview;

    //gebruikt in employee_view_mkb & employee_view_particulieren
    @GetMapping(value = "accountOverviewHandler")
    public String AccountOverviewHandler(@RequestParam(name = "id") int id, Model model) {
        Account chosenAccount = clientview.FindAccountById(id);
        model.addAttribute("account", chosenAccount);
        return "dashboard_employee";
    }

    @GetMapping(value = "privateAccountListHandlerForEmployee")
    public String privateAccountListHandlerForEmployee(@RequestParam(name = "id") int id, Model model) {
        Account chosenAccount = clientview.FindPrivateOrBusinessAccountById(id, false);
        model.addAttribute("account", chosenAccount);
        return "dashboard_employee";
    }

    @GetMapping(value = "businessAccountListHandlerForEmployee")
    public String businessAccountListHandlerForEmployee(@RequestParam(name = "id") int id, Model model) {
        Account chosenAccount = clientview.FindPrivateOrBusinessAccountById(id, true);
        model.addAttribute("account", chosenAccount);
        return "dashboard_employee";
    }


}
