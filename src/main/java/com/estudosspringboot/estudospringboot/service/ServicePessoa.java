package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.ModelPessoa;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryPessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicePessoa {

    @Autowired
    private RepositoryPessoa repository;

    public List<ModelPessoa> saveAll(List<ModelPessoa> pessoas) {
        return repository.saveAll(pessoas);
    }

    public List<ModelPessoa> findAll() {
        return repository.findAll();
    }

    public Optional<ModelPessoa> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public String validaInfo(List<ModelPessoa> pessoas) {

        for (ModelPessoa pessoa : pessoas) {

            Long id = pessoa.getId();

            if (pessoa.getNome() == null) {
                return "Informar o Nome da pessoa ("+id+") é obrigatório.";
            }
            if (pessoa.getCpf() == null) {
                return "Informar o CPF da pessoa ("+id+") é obrigatório.";
            }

            if (pessoa.getIdade() == null) {
                return "Informar a Idade da pessoa ("+id+") é obrigatório.";
            }

            if (pessoa.getEmail() == null) {
                return "Informar o E-mail da pessoa ("+id+") é obrigatório.";
            }
        }

        return null;
    }
}
