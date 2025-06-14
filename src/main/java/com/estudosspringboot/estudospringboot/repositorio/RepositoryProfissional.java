package com.estudosspringboot.estudospringboot.repositorio;

import com.estudosspringboot.estudospringboot.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryProfissional extends JpaRepository<Profissional, Long> {
}