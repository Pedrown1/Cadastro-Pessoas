package com.estudosspringboot.estudospringboot.utils;

import com.estudosspringboot.estudospringboot.model.ModelPessoa;
import com.estudosspringboot.estudospringboot.service.ServicePessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Utilidades {

    @Autowired
    private ServicePessoa service;

    public Map<String, Object> estruturaAPI(BigDecimal status, String descricao, Object dados){
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("Status: ", status);
        response.put("Descrição: ", descricao);
        response.put("Dados: ", dados);

        return response;
    }

    public boolean buscaId(Long id) {
        Optional<ModelPessoa> pessoa = service.findById(id);
        if (pessoa.isPresent()) {
            return true;
        }
        return false;
    }

}
