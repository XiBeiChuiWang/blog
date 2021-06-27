package com.star.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class B {
    @GetMapping("/b")
    public String b(){
        return "a";
    }
}
