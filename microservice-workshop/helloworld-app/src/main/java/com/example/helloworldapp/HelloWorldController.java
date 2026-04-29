package com.example.helloworldapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloWorldController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/helloworld")
    public String helloWorld() {
        String hello = restTemplate.getForObject("http://hello-service/hello", String.class);
        String world = restTemplate.getForObject("http://world-service/world", String.class);
        return hello + " " + world;
    }

}