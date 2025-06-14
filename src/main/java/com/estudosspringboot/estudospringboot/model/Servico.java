package com.estudosspringboot.estudospringboot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "servico")
@Data
@ToString
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    private Integer minutos;

    // LOMBOK - Getters e setters
}
