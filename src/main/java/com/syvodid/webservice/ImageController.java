package com.syvodid.webservice; // Змініть на ваш актуальний пакет

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @GetMapping("/test-connection")
    public String testConnection() {
        return "Connection to the database is successful!";
    }
}