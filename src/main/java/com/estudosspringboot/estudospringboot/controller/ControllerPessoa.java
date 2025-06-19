package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.auth.AuthRequest;
import com.estudosspringboot.estudospringboot.auth.JwtUtil;
import com.estudosspringboot.estudospringboot.model.Pessoa;
import com.estudosspringboot.estudospringboot.service.ServicePessoa;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ControllerPessoa {

    @Autowired
    private ServicePessoa service;

    @Autowired
    private Utilidades util;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth")
    public Map<String, Object> auth(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        Optional<Pessoa> userOpt;

        boolean isEmail = username.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        if (isEmail) {
            userOpt = service.findByEmail(username);
        } else {
            String cpfNumeros = username.replaceAll("\\D", "");
            userOpt = service.findByCpf(cpfNumeros);
        }

        if (userOpt.isPresent()) {
            Pessoa pessoa = userOpt.get();

            if (passwordEncoder.matches(authRequest.getPassword(), pessoa.getSenha())) {
                String token = JwtUtil.generateToken(pessoa.getEmail());
                return util.estruturaAPI(BigDecimal.ONE, "Requisição realizada com Sucesso!", token);
            }
        }

        return util.estruturaAPI(BigDecimal.valueOf(3), "Usuário ou senha inválidos", null);
    }



    @PostMapping("/pessoa/cadastro")
    public Map<String, Object> cadastraPessoas(@RequestBody List<Pessoa> pessoas) {

        String errorMsg = service.validaInfo(pessoas);
        if (errorMsg != null) {
            return util.estruturaAPI(BigDecimal.valueOf(5), errorMsg, null);
        }

        for (Pessoa pessoa : pessoas) {
            if (pessoa.getCpf() != null) {
                String cpfLimpo = pessoa.getCpf().replaceAll("[^\\d]", "");
                pessoa.setCpf(cpfLimpo);
            }

            if (pessoa.getSenha() != null) {
                String senhaCriptografada = passwordEncoder.encode(pessoa.getSenha());
                pessoa.setSenha(senhaCriptografada);
            }
        }

        service.saveAll(pessoas);
        return util.estruturaAPI(BigDecimal.ONE, "Pessoa Cadastrada com Sucesso!", pessoas);
    }


    @GetMapping("/pessoa/consultar")
    public Map<String, Object> consultarCadastro() {
        try {
            List<Pessoa> pessoas = service.findAll();
            if (pessoas.isEmpty()) {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Requisição vazia! - []", pessoas);
            }
            return util.estruturaAPI(BigDecimal.ONE, "Requisição realizada com Sucesso!", pessoas);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @GetMapping("/pessoa/consultar/{id}")
    public Map<String, Object> consultarCadastroById(@PathVariable Long id) {
        try {
            boolean encontrou = util.buscaId(id);
            if (encontrou) {
                Optional<Pessoa> pessoa = service.findById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Pessoa encontrada com sucesso!", pessoa.get());
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/pessoa/delete/{id}")
    public Map<String, Object> deletarPessoa(@PathVariable Long id) {
        try {
            boolean encontrou = util.buscaId(id);
            if (encontrou) {
                service.deleteById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Deletado com Sucesso!", "[ID - " + id + "]");
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }
}
