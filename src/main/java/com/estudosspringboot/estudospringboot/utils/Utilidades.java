package com.estudosspringboot.estudospringboot.utils;

import com.estudosspringboot.estudospringboot.model.ModelPessoa;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryPessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class Utilidades {

    @Autowired
    private RepositoryPessoa service;

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

    public boolean validarFormatoData(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(data, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean validarFormatoHora(String hora) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(hora, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


}
