package com.estudosspringboot.estudospringboot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Data
@ToString
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_id", referencedColumnName = "id")
    private ModelPessoa pessoa;

    private String data;

    private String hora;

    private String descricao;

    private BigDecimal valor;
}
