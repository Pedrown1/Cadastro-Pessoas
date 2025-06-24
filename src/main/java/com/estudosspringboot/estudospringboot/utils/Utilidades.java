package com.estudosspringboot.estudospringboot.utils;

import org.springframework.validation.ObjectError;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utilidades {

    public Map<String, Object> estruturaAPI(BigDecimal status, String descricao, Object dados){

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("Status: ", status);
        response.put("Descricao: ", descricao);
        response.put("Dados: ", dados);

        return response;
    }



}

