package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.ModelPessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePessoa extends JpaRepository<ModelPessoa, Long> {}
