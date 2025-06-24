package com.estudosspringboot.estudospringboot.repository;

import com.estudosspringboot.estudospringboot.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryPessoa extends JpaRepository<Pessoa, Long> {}
