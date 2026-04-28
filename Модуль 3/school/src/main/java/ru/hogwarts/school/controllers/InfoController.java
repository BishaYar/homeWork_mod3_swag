package ru.hogwarts.school.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class InfoController {

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/port")
    public String getPort() {
        return "Port = " + serverPort;
    }
}