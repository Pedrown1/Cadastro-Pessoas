package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.Pessoa;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryPessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ServicePessoa {

    @Autowired
    private RepositoryPessoa repository;

    public List<Pessoa> saveAll(List<Pessoa> pessoas) {
        return repository.saveAll(pessoas);
    }

    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    public Optional<Pessoa> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Pessoa> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public String validaInfo(List<Pessoa> pessoas) {

        for (Pessoa pessoa : pessoas) {

            if (pessoa.getNome() == null) {
                return "Informar o Nome da pessoa é obrigatório.";
            }
            if (pessoa.getCpf() == null) {
                return "Informar o CPF da pessoa é obrigatório.";
            }

            if (pessoa.getIdade() == null) {
                return "Informar a Idade da pessoa é obrigatório.";
            }

            if (pessoa.getEmail() == null ) {
                return "Informar o E-mail da pessoa é obrigatório.";
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(pessoa.getEmail());

            if (!matcher.matches()) {
                return "O e-mail informado para a pessoa é inválido.";
            }

        }

        return null;
    }
}
