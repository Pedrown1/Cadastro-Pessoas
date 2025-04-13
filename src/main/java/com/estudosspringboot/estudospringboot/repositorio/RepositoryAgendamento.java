package com.estudosspringboot.estudospringboot.repositorio;

import com.estudosspringboot.estudospringboot.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryAgendamento extends JpaRepository<Agendamento, Long> {}
