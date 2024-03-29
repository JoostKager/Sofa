package team2.sofa.sofa.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import team2.sofa.sofa.model.Client;
import team2.sofa.sofa.model.Login;

@RestController

public class AceController {
    @Autowired
    RestTemplate restTemplate;

    public String getLogin(Client client){
    String url = "http://localhost:7800/loginapi/v1/login?user="+client.getUsername()+"&pass="+client.getPassword();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Client> entity = new HttpEntity<Client>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }
    public String postLogin(Client client) {
        String url = "http://localhost:7800/loginapi/v1/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        Map<String, Object> map = new HashMap<>();
        map.put("username", client.getUsername());
        map.put("password", client.getPassword());
    
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        // HttpEntity<Client> entity = new HttpEntity<Client>(client,headers);
        
        return restTemplate.exchange(
           url, HttpMethod.POST, entity, String.class).getBody();
     }

}