package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.Estabelecimento;
import com.estudosspringboot.estudospringboot.model.Profissional;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryEstabelecimento;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryProfissional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceProfissional {

    @Autowired
    private RepositoryProfissional repository;

    @Autowired
    private RepositoryEstabelecimento repositoryEstabelecimento;

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
        for (Profissional p : profissionais) {
            if (p.getEstabelecimento() != null && p.getEstabelecimento().getId() != null) {
                Optional<Estabelecimento> est = repositoryEstabelecimento.findById(p.getEstabelecimento().getId());
                if (est.isPresent()) {
                    p.setEstabelecimento(est.get());
                } else {
                    throw new RuntimeException("Estabelecimento não encontrado para id: " + p.getEstabelecimento().getId());
                }
            }
        }
        return repository.saveAll(profissionais);
    }

}
