package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.Profissional;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryProfissional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceProfissional {

    @Autowired
    private RepositoryProfissional repository;

    public List<Profissional> findAll() {
        return repository.findAll();
    }

    public Optional<Profissional> findById(Long id) {
        return repository.findById(id);
    }

    public Profissional save(Profissional profissional) {
        return repository.save(profissional);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Profissional> saveAll(List<Profissional> profissionais) {
        return repository.saveAll(profissionais);
    }

}
