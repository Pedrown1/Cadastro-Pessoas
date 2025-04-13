package com.estudosspringboot.estudospringboot.service;


import com.estudosspringboot.estudospringboot.model.Agendamento;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryAgendamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceAgendamento {

    @Autowired
    private RepositoryAgendamento repositoryAgendamento;

    public Agendamento save(Agendamento agendamento) {
        return repositoryAgendamento.save(agendamento);
    }

    public List<Agendamento> findAll() {
        return repositoryAgendamento.findAll();
    }

    public Optional<Agendamento> findById(Long id) {
        return repositoryAgendamento.findById(id);
    }

    public void deleteById(Long id) {
        repositoryAgendamento.deleteById(id);
    }


}
