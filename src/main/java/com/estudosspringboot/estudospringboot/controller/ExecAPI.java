package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.auth.AuthRequest;
import com.estudosspringboot.estudospringboot.auth.JwtUtil;
import com.estudosspringboot.estudospringboot.model.ModelPessoa;
import com.estudosspringboot.estudospringboot.service.ServicePessoa;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ExecAPI {

    @Autowired
    private ServicePessoa service;

    @Autowired
    private Utilidades util;

    @PostMapping("/auth")
    public Map<String, Object> auth(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        if ("Pedro".equals(username) && "123456".equals(password)) {
            String token = JwtUtil.generateToken(username);
            return util.estruturaAPI(BigDecimal.ONE, "Requisição realizada com Sucesso!", token);
        }

        return util.estruturaAPI(BigDecimal.valueOf(3), "Usuário ou senha inválidos", null);
    }

    @PostMapping("/cadastro")
    public List<ModelPessoa> cadastraPessoas(@RequestBody List<ModelPessoa> pessoas) {
        pessoas.forEach(p -> System.out.println("[POST] - Recebido: " + p));
        return service.saveAll(pessoas);
    }

    @GetMapping("/consultar")
    public Map<String, Object> consultarCadastro(){
        try{
            List<ModelPessoa> pessoas = service.findAll();

            if (pessoas.isEmpty()){
                return util.estruturaAPI(BigDecimal.valueOf(2), "Requisição vazia! - []", pessoas);
            }

            return util.estruturaAPI(BigDecimal.ONE, "Requisição realizada com Sucesso!", pessoas);

        }catch (Exception e){
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @GetMapping("/consultar/{id}")
    public Map<String, Object> consultarCadastroById(@PathVariable Long id){
        try{
            boolean encontrou = util.buscaId(id);
            if (encontrou){
                Optional<ModelPessoa> pessoa = service.findById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Pessoa encontrada com sucesso!", pessoa.get());
            }else{
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        }catch (Exception e){
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deletarPessoa(@PathVariable Long id){
        try {
            boolean encontrou = util.buscaId(id);
            if (encontrou){
                service.deleteById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Deletado com Sucesso!", "[ID - "+id+"]");
            }else{
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        }catch (Exception e){
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

}
