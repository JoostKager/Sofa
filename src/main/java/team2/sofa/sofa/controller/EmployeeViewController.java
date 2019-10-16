package team2.sofa.sofa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import team2.sofa.sofa.model.BusinessAccount;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.PrivateAccount;
import team2.sofa.sofa.service.BalancePerSectorData;
import team2.sofa.sofa.service.SectorAnalyzer;
import team2.sofa.sofa.service.TopTenHighestBalanceFinder;
import team2.sofa.sofa.service.TopTenMostActiveClient;

import java.util.List;

@Controller
@SessionAttributes({"sessionemployee", "sessionclient"})
public class EmployeeViewController {


    @Autowired
    TopTenMostActiveClient topTenMostActiveClient;
    @Autowired
    TopTenHighestBalanceFinder topTenHighestBalanceFinder;
    @Autowired
    SectorAnalyzer sectorAnalyzer;


    @GetMapping(value = "tenMostActiveHandler")
    public String tenMostActiveHandler(@RequestParam(name = "id") int id, Model model) {
        return topTenMostActiveClient.mostActiveClientBuilder(id, model);
    }

    @GetMapping(value= "loadEmployeeViewPrivate")
    public String loadEmployeeViewPrivate(Model model) {
            List<PrivateAccount> topTenHighest;
            topTenHighest = topTenHighestBalanceFinder.getTopTenHighestBalance();
            model.addAttribute("tenHighestBalance", topTenHighest);
            return "employee_view_particulieren";
    }

    @GetMapping(value= "loadEmployeeViewBusiness")
    public String loadEmployeeViewBusiness(Model model) {
        List<Client> topTenMostActive;
        List<BusinessAccount> topTenHighest;
        List<BalancePerSectorData> balancePerSector;
        topTenHighest = topTenHighestBalanceFinder.getTopTenHighestBalanceBusiness();
        topTenMostActive = topTenMostActiveClient.getTopTenMostActiveClients();
        balancePerSector = sectorAnalyzer.getAverageBalancePerSector();
        model.addAttribute("tenMostActive", topTenMostActive);
        model.addAttribute("tenHighestBalance", topTenHighest);
        model.addAttribute("balancePerSector", balancePerSector);
        return "employee_view_mkb";
    }

    @GetMapping(value= "loadEmployeeViewAccountManager")
    public String loadEmployeeViewAccountManager(Model model) {
        return "account_manager_view";

    }

}
