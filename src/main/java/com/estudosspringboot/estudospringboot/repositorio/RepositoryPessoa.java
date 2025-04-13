package com.estudosspringboot.estudospringboot.repositorio;

import com.estudosspringboot.estudospringboot.model.ModelPessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryPessoa extends JpaRepository<ModelPessoa, Long> {}
