package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.Agendamento;
import com.estudosspringboot.estudospringboot.model.Estabelecimento;
import com.estudosspringboot.estudospringboot.model.Pessoa;
import com.estudosspringboot.estudospringboot.model.Servico;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryEstabelecimento;
import com.estudosspringboot.estudospringboot.repositorio.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceServico {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private RepositoryEstabelecimento repositoryEstabelecimento;

    public Optional<Servico> findById(Long id) {
        return servicoRepository.findById(id);
    }

    public List<Servico> findAll() {
        return servicoRepository.findAll();
    }

    public Servico save(Servico servico) {
        if (servico.getEstabelecimento() != null && servico.getEstabelecimento().getId() != null) {
            Optional<Estabelecimento> est = repositoryEstabelecimento.findById(servico.getEstabelecimento().getId());
            est.ifPresent(servico::setEstabelecimento);
        }
        return servicoRepository.save(servico);
    }

    public void deleteById(Long id) {
        servicoRepository.deleteById(id);
    }
}
