package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/End")
public class EndController {
    @PostMapping("/bye")
    public String endMethod(){
        return "EndPage";
    }
}
