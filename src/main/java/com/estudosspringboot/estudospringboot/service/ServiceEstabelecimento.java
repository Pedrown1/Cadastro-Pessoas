package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.Agendamento;
import com.estudosspringboot.estudospringboot.model.Estabelecimento;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryEstabelecimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ServiceEstabelecimento {

    @Autowired
    private RepositoryEstabelecimento repository;

    public List<Estabelecimento> saveAll(List<Estabelecimento> estabelecimentos) {
        return repository.saveAll(estabelecimentos);
    }

    public List<Estabelecimento> findAll() {
        return repository.findAll();
    }

    public Optional<Estabelecimento> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
