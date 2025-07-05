package com.estudosspringboot.estudospringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/cadastro")
    public String mostrarCadastro() {
        return "autenticacao/cadastro";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "autenticacao/login";
    }

    @GetMapping("/confirmacao")
    public String mostrarConfirmacao() {
        return "autenticacao/confirmacao";
    }

    @GetMapping("/estabelecimento")
    public String viewEstabelecimentos() {
        return "estabelecimento/local";
    }

}
