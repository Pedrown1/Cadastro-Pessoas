package com.estudosspringboot.estudospringboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class ModelPessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private int idade;

    private String cpf;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getNome() {
//        return nome;
//    }
//
//    public void setNome(String nome) {
//        this.nome = nome;
//    }
//
//    public int getIdade() {
//        return idade;
//    }
//
//    public void setIdade(int idade) {
//        this.idade = idade;
//    }
//
//    public String getCpf() {
//        return cpf;
//    }
//
//    public void setCpf(String cpf) {
//        this.cpf = cpf;
//    }
//
//    @Override
//    public String toString() {
//        return "ModelPessoa{" +
//                "id=" + id +
//                ", nome='" + nome + '\'' +
//                ", idade=" + idade +
//                ", cpf='" + cpf + '\'' +
//                '}';
//    }

    // Lombok (GET/SET)
}
