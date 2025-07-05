package com.estudosspringboot.estudospringboot.repositorio;

import com.estudosspringboot.estudospringboot.model.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryEstabelecimento extends JpaRepository<Estabelecimento, Long> {
    List<Estabelecimento> findByNomeContainingIgnoreCase(String nome);
}
