package br.com.ericsson.smartnotification.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String getPage(Map<String, Object> model) {       
        return "../page/index";
    }
    
}
