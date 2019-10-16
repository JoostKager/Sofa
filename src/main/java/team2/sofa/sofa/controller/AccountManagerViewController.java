package team2.sofa.sofa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.service.RequestPDQ;


@Controller
@SessionAttributes ({"sessionemployee", "bussinessAccount", "fiveDigits"})
public class AccountManagerViewController {

    @Autowired
    RequestPDQ rp;


    @PostMapping(value = "connectPDQ")
    public String requestPDQ(@RequestParam String iban, Model model) {
        Account account = rp.getAccount(iban);
        if (account != null){
            if (account.isBusinessAccount()) {
                String code = rp.storePdq(account);
                model.addAttribute("fiveDigits", code);
                model.addAttribute("bussinessAccount", account);
                return "redirect:/connecting-code";
            } else {
                model.addAttribute("error", "Voer een zakelijk IBAN in");
                return "account_manager_view";
            }
        } else {
            model.addAttribute("error", "Ingevoerd IBAN is niet bekend");
            return "account_manager_view";
        }
    }

    @GetMapping(value= "connecting-code")
    public String goToConnectingCode(Model model){
        return "receive_connecting_code";
    }
}

