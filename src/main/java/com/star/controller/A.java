package com.star.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a")
public class A {
    @PostMapping("/a1")
    public String a(AA aa){
        System.out.println(aa.getName());
        return aa.getName()+ aa.getPassword();
    }
}
