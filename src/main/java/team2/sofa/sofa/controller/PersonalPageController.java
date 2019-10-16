package team2.sofa.sofa.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team2.sofa.sofa.model.Address;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.service.ConnectingService;
import team2.sofa.sofa.service.Login;
import team2.sofa.sofa.service.UpdateClient;

import java.util.Map;

@Controller
@SessionAttributes({"sessionclient", "connector", "address","clientId"})
public class PersonalPageController {

    @Autowired
    UpdateClient updateClient;
    @Autowired
    Login login;
    @Autowired
    ConnectingService cs;

    @PostMapping(value = "updateHandler")
    public String updateHandler(@RequestParam int id, Model model) {
        Client client = updateClient.findClient(id);
        model.addAttribute("sessionclient", client);
        Hibernate.initialize(client.getAccounts());
        return "redirect:wijzigGegevens";
    }
    @GetMapping(value = "wijzigGegevens")
    public String change(@ModelAttribute("used") String fout, Model model){
        if (!fout.equals("")){
            model.addAttribute("error", fout);}
        return "edit_client";
    }

    @PostMapping(value = "changeAddressForm")
    public String changeAddress(@RequestParam int clientId, @RequestParam int addressId, Model model) {
        Address address = updateClient.findAddress(addressId);
        model.addAttribute("address", address);
        model.addAttribute("clientId", clientId);
        return "redirect:/verhuizing";
    }
    @GetMapping(value = "verhuizing")
    public String verhuizing(Model model){
        return "change_address";
    }

    @PostMapping(value = "changeAddress")
    public String processAddress(@RequestParam Map<String, String> input,
                                 @RequestParam int clientId,
                                 @ModelAttribute("address") Address address,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Client client = updateClient.findClient(clientId);
        Address checkedAddress = updateClient.checkAddress(input);
        //indien het een nieuw adress is dat nog niet voorkomt of
        //indien adres niet is aangepast na weergave onderstaande error
        if (checkedAddress.getId() == 0 || address.getId()== checkedAddress.getId()){
            client = updateClient.changeAddress(client, checkedAddress);
            redirectAttributes.addFlashAttribute("clientUsername", client.getUsername());
            return "redirect:/rekeningenoverzicht";
        }
        model.addAttribute("address", checkedAddress);
        model.addAttribute("clientId", clientId);
        model.addAttribute("error", "bedoelde u misschien dit adres?");
        return "change_address";
    }

    @PostMapping(value = "updateClient")
    public String processUpdate(@RequestParam Map<String, String> input,
                                Model model,
                                @ModelAttribute("sessionclient") Client oldclient,
                                RedirectAttributes redirectAttributes) {
        Client client = updateClient.findClient(Integer.valueOf(input.get("id")));
        String newUsername = input.get("user");
        if (newUsername.equals(oldclient.getUsername())) {
            client = updateClient.processChanges(client, input);
            redirectAttributes.addFlashAttribute("clientUsername", client.getUsername());
            return "redirect:/rekeningenoverzicht";
        } else {
            if (updateClient.usernameExists(newUsername)) {
                redirectAttributes.addFlashAttribute("used", "gebruikersnaam is al in gebruik");
                return "redirect:/wijzigGegevens";
            }
            cs.changeUsername(oldclient.getUsername(), input.get("user"));
            client.setUsername(input.get("user"));
            client = updateClient.processChanges(client, input);
            redirectAttributes.addFlashAttribute("clientUsername", client.getUsername());
            return "redirect:/rekeningenoverzicht";
        }
    }
}