package com.example.oreily;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

// Mirage — для случаев, когда
// переменная не описана в объекте Environment приложения
@RestController
//@RequestMapping("/greeting")
@ConfigurationProperties(prefix = "greeting")
public class Greeting {
    @Value("${greeting-name: Mirage}")
    private String name;

    @Value("${greeting-coffee:}")
    private String coffee;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoffee() {
        return coffee;
    }

    public void setCoffee(String coffee) {
        this.coffee = coffee;
    }
}
