package com.estudosspringboot.estudospringboot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "pessoa")
@ToString
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Integer idade;

    private String cpf;

    private String email;

    private String senha;

    private String codigoVerificacao;

    private LocalDateTime dataEnvio;

    // Lombok (GET/SET)
}
