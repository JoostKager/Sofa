package team2.sofa.sofa.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.model.TransactionForm;
import team2.sofa.sofa.service.FundTransfer;

import java.util.Map;

@Controller
@SessionAttributes({"sessionclient", "account", "transaction"})
public class PaymentController {

    @Autowired
    FundTransfer fundTransfer;

    @GetMapping(value = "startTransfer")
    public String intializeTransfer(@ModelAttribute Account account, Model model){
        model = fundTransfer.readyTransaction(account, model);
        return "money_transfer";
    }
    @GetMapping(value = "confirmTransfer")
    public String confirmPayment(Model model){
        return "confirm_payment.html";
    }

    @PostMapping(value = "transferMoneyHandler")
    public String transferMoneyHandler(TransactionForm transactionForm, Model model) {
        if (fundTransfer.insufficientBalance(transactionForm)){
            String saldo = "onvoldoende Saldo";
            model.addAttribute("saldo", saldo);
            model = fundTransfer.returnToTransaction(transactionForm, model);
            return "money_transfer";
        }
        if (!fundTransfer.checkToAccount(transactionForm.getCreditAccount())) {
            String fout = "ongeldig IBAN";
            model.addAttribute("fout", fout);
            model = fundTransfer.returnToTransaction(transactionForm, model);
            return "money_transfer";
        }
        if (!fundTransfer.nameCheckIban(transactionForm.getName(), transactionForm.getCreditAccount())) {
            String fout = "Achternaam / Bedrijfsnaam en IBAN komen niet overeen";
            model.addAttribute("mismatch", fout);
            model = fundTransfer.returnToTransaction(transactionForm, model);
            return "money_transfer";
        }
        if (transactionForm.getCreditAccount().equals(transactionForm.getDebetAccount())) {
            String fout = "Je kan niet geld naar je eigen bankrekening overmaken";
            model.addAttribute("foei", fout);
            model = fundTransfer.returnToTransaction(transactionForm, model);
            return "money_transfer";
        }
        model = fundTransfer.prepareConfirmation(transactionForm, model);
        return "redirect:/confirmTransfer";
    }

    @PostMapping(value = "confirmPayment")
    public String handleTransfer(TransactionForm transactionForm, RedirectAttributes redirectAttributes) {
        fundTransfer.procesTransaction(transactionForm);
        Account account = fundTransfer.readyDashboard(transactionForm);
        redirectAttributes.addFlashAttribute("acc", account);
        Hibernate.initialize(account.getTransactions());
        return "redirect:/rekeningdetails";
    }

}

