package com.estudosspringboot.estudospringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"codigoVerificacao", "dataEnvio"})
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
