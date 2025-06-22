package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.model.Estabelecimento;
import com.estudosspringboot.estudospringboot.service.ServiceEstabelecimento;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estabelecimento")
public class ControllerEstabelecimento {

    @Autowired
    private ServiceEstabelecimento service;

    @Autowired
    private Utilidades util;

    @PostMapping("/cadastro")
    public Map<String, Object> cadastrarEstabelecimentos(@RequestBody List<Estabelecimento> estabelecimentos) {
        try {
            List<Estabelecimento> salvos = service.saveAll(estabelecimentos);
            return util.estruturaAPI(BigDecimal.ONE, "Estabelecimentos cadastrados com sucesso!", salvos);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha ao cadastrar estabelecimentos: " + e.getMessage(), null);
        }
    }

    @GetMapping("/consultar")
    public Map<String, Object> consultarEstabelecimentos() {
        try {
            List<Estabelecimento> lista = service.findAll();
            if (lista.isEmpty()) {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Nenhum estabelecimento encontrado.", lista);
            }
            return util.estruturaAPI(BigDecimal.ONE, "Estabelecimentos encontrados com sucesso!", lista);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @GetMapping("/consultar/{id}")
    public Map<String, Object> consultarEstabelecimentoById(@PathVariable Long id) {
        try {
            Optional<Estabelecimento> op = service.findById(id);
            if (op.isPresent()) {
                return util.estruturaAPI(BigDecimal.ONE, "Estabelecimento encontrado com sucesso!", op.get());
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deletarEstabelecimento(@PathVariable Long id) {
        try {
            Optional<Estabelecimento> op = service.findById(id);
            if (op.isPresent()) {
                service.deleteById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Estabelecimento deletado com sucesso!", "[ID - " + id + "]");
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }
}
