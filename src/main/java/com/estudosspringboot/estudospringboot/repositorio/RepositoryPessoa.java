package com.estudosspringboot.estudospringboot.repositorio;

import com.estudosspringboot.estudospringboot.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryPessoa extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByEmail(String email);
    Optional<Pessoa> findByEmailAndCodigoVerificacao(String email, String codigo);
    Optional<Pessoa> findByCpf(String cpf);
}
