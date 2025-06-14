package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.model.Servico;
import com.estudosspringboot.estudospringboot.service.ServiceServico;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servico")
public class ControllerServico {

    @Autowired
    private ServiceServico serviceServico;

    @Autowired
    private Utilidades util;

    // Criar um novo serviço
    @PostMapping("/cadastrar")
    public Object cadastrarServico(@RequestBody Servico servico) {
        try {
            if (servico.getDescricao() == null || servico.getDescricao().isEmpty()) {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Descrição do serviço é obrigatória", null);
            }
            if (servico.getValor() == null) {
                return util.estruturaAPI(BigDecimal.valueOf(3), "Valor do serviço é obrigatório", null);
            }

            if (servico.getMinutos() == null){
                return util.estruturaAPI(BigDecimal.valueOf(4), "O tempo gasto em minutos é obrigatório", null);
            }

            Servico saved = serviceServico.save(servico);
            return util.estruturaAPI(BigDecimal.ONE, "Serviço criado com sucesso!", saved);

        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Erro ao criar serviço: " + e.getMessage(), null);
        }
    }

    // Listar todos os serviços
    @GetMapping("/listar")
    public Object listarServicos() {
        try {
            List<Servico> servicos = serviceServico.findAll();
            if (servicos.isEmpty()) {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Nenhum serviço encontrado", servicos);
            }
            return util.estruturaAPI(BigDecimal.ONE, "Serviços encontrados com sucesso", servicos);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Erro ao listar serviços: " + e.getMessage(), null);
        }
    }

    // Buscar serviço por ID
    @GetMapping("/{id}")
    public Object buscarServicoPorId(@PathVariable Long id) {
        try {
            Optional<Servico> servico = serviceServico.findById(id);
            if (servico.isPresent()) {
                return util.estruturaAPI(BigDecimal.ONE, "Serviço encontrado", servico.get());
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Serviço não encontrado", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Erro ao buscar serviço: " + e.getMessage(), null);
        }
    }
}
