package com.estudosspringboot.estudospringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

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
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id", referencedColumnName = "id")
    private Estabelecimento estabelecimento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id", referencedColumnName = "id")
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    private String data;

    private String hora;

    @JsonIgnore
    private String descricao;
    @JsonIgnore
    private BigDecimal valor;
}
