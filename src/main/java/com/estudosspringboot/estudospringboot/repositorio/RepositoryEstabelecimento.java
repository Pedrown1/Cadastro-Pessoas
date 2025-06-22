package com.estudosspringboot.estudospringboot.repositorio;

import com.estudosspringboot.estudospringboot.model.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEstabelecimento extends JpaRepository<Estabelecimento, Long> {
}
