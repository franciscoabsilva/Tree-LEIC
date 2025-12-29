package com.example.demo; // <--- CONFIRMA SE O TEU PACKAGE É ESTE

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin; // Importante para evitar bloqueios

@RestController
@CrossOrigin(origins = "*") // Permite que qualquer origem chame este endpoint
public class LogController {

    @GetMapping("/api/log")
    public void registarLog(@RequestParam(value = "msg", defaultValue = "SemMensagem") String msg) {
        // O RequestLoggingFilter vai apanhar isto e fazer o print.
        // Não precisamos de fazer nada aqui.
    }
}