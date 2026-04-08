package com.timeright.tcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.timeright.tcc.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/confirmar")
    public String confirmarEmail(@RequestParam String token) {
        return usuarioService.confirmarEmail(token);
    }
}