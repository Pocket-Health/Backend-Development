package ru.ffanjex.backenddevelopment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/")
public class TestController {
    @GetMapping
    public String test() {
        return "PocketHealth v1";
    }
}
