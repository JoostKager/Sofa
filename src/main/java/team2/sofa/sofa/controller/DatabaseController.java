package team2.sofa.sofa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import team2.sofa.sofa.service.DbInitializer;

/**
 * basale controller puur voor het vullen van de database met data uit .CSV bestand
 * maakt zodoende gebruik van de service dbInitializer.
 */
@Controller
public class DatabaseController {
    // samen nooit meer dan hoeveelheid in dataset die wordt ingeladen in de service DbInitializer
    // Small tot 99
    // Medium tot 5000
    // Large tot 7000
    private final int CLIENTS = 4000;
    private final int EMPLOYEES = 6;
    private final int BUSINESS = 1000;


    @Autowired
    DbInitializer dbInitializer;

    @GetMapping(value = "initdb")
    public String indexInitdbHandler(Model model) {
        dbInitializer.makeClient(CLIENTS);
        dbInitializer.fillPrivateAccounts();
        dbInitializer.makeEmployees(EMPLOYEES);
        dbInitializer.assignEmployeeRoles();
        dbInitializer.makeBusiness(BUSINESS, CLIENTS);
        dbInitializer.makeDoubleAccounts();
        dbInitializer.fillTransactions();
        return "index";
    }
}
