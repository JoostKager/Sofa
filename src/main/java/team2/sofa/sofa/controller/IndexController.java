package team2.sofa.sofa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    @GetMapping(value = "")
    public String startHandler() {
        return "index";
    }

    @GetMapping(value = "index")
    public String homeHandler() {
        return "index";
    }

}

