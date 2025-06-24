package com.estudosspringboot.estudospringboot;

import com.estudosspringboot.estudospringboot.model.Pessoa;

public class main {
    public static void main(String[] args) {
        Pessoa p = new Pessoa();

        p.setNome("Pedro");
        p.setCpf("10207849325");

        System.out.println(p.toString());
    }
}
