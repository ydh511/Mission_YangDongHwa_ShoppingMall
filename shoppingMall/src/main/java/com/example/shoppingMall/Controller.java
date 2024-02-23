package com.example.shoppingMall;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class Controller {
    @PostMapping("/hello")
    public String hello(){
        return "hello";
    }
}
