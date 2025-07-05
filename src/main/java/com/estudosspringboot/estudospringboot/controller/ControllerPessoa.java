package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.auth.AuthRequest;
import com.estudosspringboot.estudospringboot.auth.JwtUtil;
import com.estudosspringboot.estudospringboot.model.Pessoa;
import com.estudosspringboot.estudospringboot.service.EmailService;
import com.estudosspringboot.estudospringboot.service.ServicePessoa;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ControllerPessoa {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ServicePessoa service;

    @Autowired
    private Utilidades util;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth")
    public Map<String, Object> auth(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        Optional<Pessoa> userOpt;

        boolean isEmail = username.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        if (isEmail) {
            userOpt = service.findByEmail(username.toLowerCase());
        } else {
            String cpfNumeros = username.replaceAll("\\D", "");
            userOpt = service.findByCpf(cpfNumeros);
        }

        if (userOpt.isPresent()) {
            Pessoa pessoa = userOpt.get();

            if (pessoa.getCodigoVerificacao() != null) {
                return util.estruturaAPI(BigDecimal.valueOf(4), "Confirmação de e-mail pendente. Verifique seu e-mail.", null);
            }

            if (passwordEncoder.matches(authRequest.getPassword(), pessoa.getSenha())) {
                String token = JwtUtil.generateToken(pessoa.getEmail());
                System.out.println("TOKEN: " + token);
                return util.estruturaAPI(BigDecimal.ONE, "Requisição realizada com Sucesso!", token);
            }
        }


        return util.estruturaAPI(BigDecimal.valueOf(3), "Usuário ou senha inválidos", null);
    }

    @PostMapping("/pessoa/reenviar-codigo")
    public Map<String, Object> reenviarCodigo(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<Pessoa> pessoaOpt = service.findByEmail(email);
        if (pessoaOpt.isEmpty()) {
            return util.estruturaAPI(BigDecimal.valueOf(2), "E-mail não encontrado!", null);
        }

        Pessoa pessoa = pessoaOpt.get();

        if (pessoa.getCodigoVerificacao() == null) {
            return util.estruturaAPI(BigDecimal.valueOf(3), "Este e-mail já foi verificado.", null);
        }

        String novoCodigo = util.gerarCodigo();
        pessoa.setCodigoVerificacao(novoCodigo);
        pessoa.setDataEnvio(LocalDateTime.now());
        service.saveAll(List.of(pessoa));

        String corpo = "<p>Olá!</p>"
                + "<p>Seu novo código de verificação é: <strong style='font-size: 18px; color: #008000;'>" + novoCodigo + "</strong></p>"
                + "<p>Use este código para confirmar seu cadastro.</p><br><p>Estudos Pedro</p>";

        emailService.sendEmail(email, "Reenvio de Código de Verificação", corpo);


        return util.estruturaAPI(BigDecimal.ONE, "Código reenviado para o e-mail.", null);
    }


    @PostMapping("/pessoa/cadastro")
    public Map<String, Object> cadastraPessoas(@RequestBody List<Pessoa> pessoas) {

        String errorMsg = service.validaInfo(pessoas);
        if (errorMsg != null) {
            return util.estruturaAPI(BigDecimal.valueOf(5), errorMsg, null);
        }

        for (Pessoa pessoa : pessoas) {
            if (pessoa.getCpf() != null) {
                String cpfLimpo = pessoa.getCpf().replaceAll("[^\\d]", "");
                pessoa.setCpf(cpfLimpo);
            }

            if (pessoa.getSenha() != null) {
                String senhaCriptografada = passwordEncoder.encode(pessoa.getSenha());
                pessoa.setSenha(senhaCriptografada);
            }

            pessoa.setEmail(pessoa.getEmail().toLowerCase());

            String codigo = util.gerarCodigo();
            pessoa.setCodigoVerificacao(codigo);
            pessoa.setDataEnvio(LocalDateTime.now());

            System.out.println("CODIGO GERADO CADASTRO: " + codigo);

            String subject = "Código de Verificação - Cadastro";
            String body = "<p>Olá, <strong>" + pessoa.getNome() + "</strong>!</p>"
                    + "<p>Seu código de verificação é: <strong style='font-size: 18px; color: #008000;'>" + codigo + "</strong></p>"
                    + "<p>Use este código para confirmar seu cadastro.</p><br><p>Estudos Pedro</p>";

            emailService.sendEmail(pessoa.getEmail(), subject, body);
        }

        service.saveAll(pessoas);
        return util.estruturaAPI(BigDecimal.ONE, "Pessoa cadastrada! Verifique seu e-mail para ativar.", null);
    }

    @PostMapping("/pessoa/confirmar-email")
    public Map<String, Object> confirmarEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String codigo = request.get("codigo");

        System.out.println("CODIGO CONFIRMACAO: " + codigo);

        Optional<Pessoa> opt = service.findByEmailAndCodigo(email, codigo);

        if (opt.isEmpty()) {
            return util.estruturaAPI(BigDecimal.valueOf(3), "Código inválido ou e-mail incorreto.", null);
        }

        Pessoa pessoa = opt.get();
        pessoa.setCodigoVerificacao(null);
        pessoa.setDataEnvio(null);
        service.saveAll(List.of(pessoa));

        return util.estruturaAPI(BigDecimal.ONE, "E-mail confirmado com sucesso!", pessoa);
    }


    @GetMapping("/pessoa/consultar")
    public Map<String, Object> consultarCadastro() {
        try {
            List<Pessoa> pessoas = service.findAll();
            if (pessoas.isEmpty()) {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Requisição vazia! - []", pessoas);
            }
            return util.estruturaAPI(BigDecimal.ONE, "Requisição realizada com Sucesso!", pessoas);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @GetMapping("/pessoa/consultar/{id}")
    public Map<String, Object> consultarCadastroById(@PathVariable Long id) {
        try {
            boolean encontrou = util.buscaId(id);
            if (encontrou) {
                Optional<Pessoa> pessoa = service.findById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Pessoa encontrada com sucesso!", pessoa.get());
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/pessoa/delete/{id}")
    public Map<String, Object> deletarPessoa(@PathVariable Long id) {
        try {
            boolean encontrou = util.buscaId(id);
            if (encontrou) {
                service.deleteById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Deletado com Sucesso!", "[ID - " + id + "]");
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "ID não encontrado!", "[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na requisição: " + e.getMessage(), null);
        }
    }
}
