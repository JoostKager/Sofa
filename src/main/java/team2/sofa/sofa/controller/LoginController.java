package team2.sofa.sofa.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.Employee;
import team2.sofa.sofa.model.EmployeeRole;
import team2.sofa.sofa.service.Login;
import team2.sofa.sofa.service.PasswordValidator;


@Controller
@SessionAttributes({"sessionclient", "connect", "nrBusiness", "nrPrivate", "sessionemployee"})
public class LoginController {
    @Autowired
    Login login;
    @Autowired
    PasswordValidator passwordValidator;
    @Autowired
    ClientViewController clientViewController;

    private final RestTemplate restTemplate;

    public LoginController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping(value = "login")
    public String indexHandler(Model model) {
        model.addAttribute("client", new Client());
        return "login";
    }

    @GetMapping(value = "logout")
    public String logoutHandler(Model model) {
        model.addAttribute("sessionclient", "");
        model.addAttribute("connect", "");
        model.addAttribute("client", new Client());
        return "index";
    }

    @GetMapping(value = "login_medewerker")
    public String goToLoginEmployeeHandler(Model model) {
        model.addAttribute("employee", new Employee());
        return "login_employee";
    }

    @PostMapping(value = "loginClientHandler")
    public String loginClientHandler(Client client, Model model, RedirectAttributes redirectAttributes) {
        Client c = createPost(client);
        System.out.println(c);
        boolean loginOk = passwordValidator.validateClientPassword(client);
        if (loginOk) {
            model.addAttribute("sessionclient", new Client());
            redirectAttributes.addFlashAttribute("clientUsername", client.getUsername());
            return "redirect:/rekeningenoverzicht";
        } else {
            model.addAttribute("fout", "Gebruikersnaam en/of wachtwoord zijn niet juist");
            return "login";
        }
    }

    @PostMapping(value = "loginEmployeeHandler")
    public String loginEmployeeHandler(Employee employee, Model model) {

        Employee currentEmployee = new Employee();
        currentEmployee.setUsername(employee.getUsername());
        currentEmployee.setPassword(employee.getPassword());
        boolean loginOK = passwordValidator.validateEmployeePassword(currentEmployee);
        if (loginOK) {
            Employee fullemployee = login.employeeLogin(currentEmployee, model);
            if (fullemployee.getRole().equals(EmployeeRole.HOOFD_PARTICULIEREN)) {
                return "redirect:/loadEmployeeViewPrivate";
            }
            if (fullemployee.getRole().equals(EmployeeRole.ACCOUNTMANAGER)) {
                return "redirect:/loadEmployeeViewAccountManager";
            } else {
                return "redirect:/loadEmployeeViewBusiness";
            }
        } else {
            model.addAttribute("Fout", "Gebruikersnaam en/of wachtword zijn niet juist");
            return "login_employee";
        }
    }

    public Client createPost(Client client) {
        String url = "http://host.docker.internal:7800/loginapi/v1/login";
    
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    
        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("username", client.getUsername());
        map.put("password", client.getPassword());
    
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    
        // send POST request
        ResponseEntity<Client> response = this.restTemplate.postForEntity(url, entity, Client.class);
    
        // check response status code
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
    }    
}

