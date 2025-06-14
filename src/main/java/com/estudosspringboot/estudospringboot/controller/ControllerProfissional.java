package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.model.Profissional;
import com.estudosspringboot.estudospringboot.service.ServiceProfissional;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/profissional")
public class ControllerProfissional {

    @Autowired
    private ServiceProfissional service;

    @Autowired
    private Utilidades util;

    @PostMapping("/cadastro")
    public Map<String, Object> cadastrarProfissionais(@RequestBody List<Profissional> profissionais) {
        try {
            List<Profissional> salvos = service.saveAll(profissionais);
            return util.estruturaAPI(BigDecimal.ONE, "Profissionais cadastrados com sucesso!", salvos);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha ao cadastrar profissionais: " + e.getMessage(), null);
        }
    }

    @GetMapping("/consultar")
    public Map<String, Object> consultarProfissionais() {
        try {
            List<Profissional> lista = service.findAll();
            if (lista.isEmpty()) {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Nenhum profissional encontrado.", lista);
            }
            return util.estruturaAPI(BigDecimal.ONE, "Profissionais encontrados com sucesso!", lista);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @GetMapping("/consultar/{id}")
    public Map<String, Object> consultarProfissionalById(@PathVariable Long id) {
        try {
            Optional<Profissional> op = service.findById(id);
            if (op.isPresent()) {
                return util.estruturaAPI(BigDecimal.ONE, "Profissional encontrado com sucesso!", op.get());
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deletarProfissional(@PathVariable Long id) {
        try {
            Optional<Profissional> op = service.findById(id);
            if (op.isPresent()) {
                service.deleteById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Profissional deletado com sucesso!", "[ID - " + id + "]");
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }
}
