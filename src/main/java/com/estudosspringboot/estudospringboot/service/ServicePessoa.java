package com.estudosspringboot.estudospringboot.service;

import com.estudosspringboot.estudospringboot.model.Pessoa;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryPessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ServicePessoa {

    @Autowired
    private RepositoryPessoa repository;

    public List<Pessoa> saveAll(List<Pessoa> pessoas) {
        return repository.saveAll(pessoas);
    }

    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    public Optional<Pessoa> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Pessoa> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<Pessoa> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public Optional<Pessoa> findByEmailAndCodigo(String email, String codigo) {
        return repository.findByEmailAndCodigoVerificacao(email, codigo);
    }

    public String validaInfo(List<Pessoa> pessoas) {


        for (Pessoa pessoa : pessoas) {

            if (pessoa.getNome() == null || pessoa.getNome().trim().length() < 5) {
                return "O nome deve ter pelo menos 5 caracteres.";
            }

            if (pessoa.getCpf() == null) {
                return "Informar o CPF da pessoa é obrigatório.";
            } else{
                String cpfNumeros = pessoa.getCpf().replaceAll("\\D", "");
                pessoa.setCpf(cpfNumeros);

                if (repository.findByCpf(cpfNumeros).isPresent()) {
                    return "O CPF informado já está cadastrado.";
                }
            }

            if (pessoa.getIdade() == null) {
                return "Informar a Idade da pessoa é obrigatório.";
            }else if (pessoa.getIdade() <= 12){
                return "A idade deve ser maior que 12 anos.";
            }

            if (pessoa.getEmail() == null) {
                return "Informar o E-mail é obrigatório.";
            } else if (repository.findByEmail(pessoa.getEmail()).isPresent()) {
                return "O e-mail informado já está cadastrado.";
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(pessoa.getEmail());
            if (!matcher.matches()) {
                return "O E-mail informado é inválido.";
            }

            String senhaRegex = "^(?=.*[A-Z])(?=.*[\\W_]).{8,}$";
            Pattern senhaPattern = Pattern.compile(senhaRegex);
            Matcher senhaMatcher = senhaPattern.matcher(pessoa.getSenha());
            if (!senhaMatcher.matches()) {
                return "Senha: mínimo 8 caracteres, com letra maiúscula e um caractere especial.";
            }

        }

        return null;
    }
}
