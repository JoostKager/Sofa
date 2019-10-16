package team2.sofa.sofa.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team2.sofa.sofa.model.Account;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.Connector;
import team2.sofa.sofa.service.Clientview;
import team2.sofa.sofa.service.ConnectingService;

import java.util.Map;

@Controller
@SessionAttributes({"sessionclient", "connect", "nrBusiness", "nrPrivate"})
public class ConnectController {

    @Autowired
    ConnectingService cs;
    @Autowired
    Clientview clientview;

    @GetMapping(value = "koppelRekeninghouder")
    public String connect(@ModelAttribute("connection") Connector connection, @ModelAttribute("connecting") Account account, Model model) {
        if (connection.getId() != 0) {
            model.addAttribute("con", connection);
        }
        if (account.getId() != 0) {
            model.addAttribute("acc", account);
        }
        return "connect_accounts";
    }

    /**
     * mapping vanuit clientdash board naar connect_accounts
     * vanuit inlog van degene die een nieuwe rekeninghouder wil toevoegen aan rekening waarop is ingelogd.
     *
     * @param id id van het account dat is geselecteerd door de ingelogd gebruiker om een extra rekeninghouder toe te voegen
     * @return verwijzing naar pagina waarin username en beveiligingscode kan worden ingevoerd.
     */
    @PostMapping(value = "connectAccount")
    public String connectAccounts(@RequestParam int id, RedirectAttributes redirectAttributes) {
        Account account = cs.getAccount(id);
        redirectAttributes.addAttribute("connecting", account);
        return "redirect:/koppelRekeninghouder";
    }

    /**
     * handler voor verwerken van input die is gegeven door orignele rekeninghouder.
     * de ingevoerde gegevens (IBAN rekening, beveiligingscode en username extra rekeninghouder)
     * worden opgeslagen in de tabel 'connector'
     *
     * @param body  een map waarin de gegevens zitten die door de gebruiker zijn ingevoerd
     * @param model
     * @return
     */
    @PostMapping(value = "connectForm")
    public String connectHandler(@RequestParam Map<String, String> body,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (cs.checkUserName(body)) {
            //check op eigen naam en op voorkomen van username in db
            Account account = cs.saveCoupling(body);
            redirectAttributes.addFlashAttribute("acc", account);
            Hibernate.initialize(account.getTransactions());
            return "redirect:/rekeningdetails";
        } else {
            Account account = cs.getAccountbyIBAN(body.get("bankaccount"));
            model.addAttribute("acc", account);
            model.addAttribute("wrong", "De gebruikernaam is niet juist");
            return "connect_accounts";
        }
    }

    /**
     * mapping die aan knop hangt die alleen zichtbaar is als de gebruikersnaam voorkomt in de tabel "connector"
     * indien dit zo is. verschijnt de knop "voeg rekenig toe" in client-view.
     * het connector opbject wordt ingeladen en meegegeven aan het volgende scherm
     * waarin de nieuwe rekeninghouder de ontvangen beveiligingscode kan invoeren
     *
     * @param id                 van het connector object in de DB
     * @param redirectAttributes wordt gebruikt om object door te zenden via redirect
     * @return
     */
    @PostMapping(value = "newConnection")
    public String matchAccounts(@RequestParam int id, RedirectAttributes redirectAttributes) {
        Connector connector = cs.getConnection(id);
        redirectAttributes.addFlashAttribute("connection", connector);
        return "redirect:/koppelRekeninghouder";
    }

    /**
     * nadat de toekomstig mede rekeninghouder het IBAN en juiste beveiligingscode heeft ingevoerd.
     * wordt de ingelogde gebruiker gekoppeld als mede eigenaar van de rekening.
     *
     * @param body  ingevoerde gegevens in het formulier
     * @param model
     * @return
     */
    @PostMapping(value = "connectValidate")
    public String checkMatch(@RequestParam Map<String, String> body, Model model, RedirectAttributes redirectAttributes) {
        //Onderstaande methode stopt alle instanties waarbij de username voorkomt in een list
        //en checkt op IBAN en security code
        int id = Integer.valueOf(body.get("idconnect"));
        Connector c = cs.getConnection(id);
        if (c.getSecurityCode().equals(body.get("accesscode"))
                && c.getIban().equals(body.get("banknr"))) {
            //call service method to update client, account en connector
            cs.processCoupling(c);
            redirectAttributes.addFlashAttribute("clientUsername", c.getUsername());
            return "redirect:/rekeningenoverzicht";
        }
        model.addAttribute("con", c);
        model.addAttribute("wrong", "de koppelcode is niet juist");
        return "connect_accounts";
    }
}
