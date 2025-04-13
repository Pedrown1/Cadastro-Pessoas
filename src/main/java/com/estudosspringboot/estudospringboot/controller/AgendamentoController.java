package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.model.Agendamento;
import com.estudosspringboot.estudospringboot.model.ModelPessoa;
import com.estudosspringboot.estudospringboot.service.EmailService;
import com.estudosspringboot.estudospringboot.service.ServiceAgendamento;
import com.estudosspringboot.estudospringboot.service.ServicePessoa;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {

    @Autowired
    private ServiceAgendamento serviceAgendamento;

    @Autowired
    private ServicePessoa servicePessoa;

    @Autowired
    private Utilidades util;

    @Autowired
    private EmailService emailService;

    @PostMapping("/cadastrar")
    public Map<String, Object> cadastrarAgendamento(@RequestBody Agendamento agendamento) {
        try {
            if (serviceAgendamento.dataHoraObrigatoriedade(agendamento)) {
                return util.estruturaAPI(BigDecimal.valueOf(3), "Data e hora do agendamento são obrigatórias!", null);
            }

            String msgErro = serviceAgendamento.dataHoraInvalida(agendamento);
            if (msgErro != null) {
                return util.estruturaAPI(BigDecimal.valueOf(4), msgErro, null);
            }

            if (agendamento.getValor() == null){
                return util.estruturaAPI(BigDecimal.valueOf(5), "Informar o valor do corte é obrigatório.", null);
            }

            if (agendamento.getPessoa() == null || agendamento.getPessoa().getId() == null) {
                return util.estruturaAPI(BigDecimal.valueOf(6), "ID da pessoa é obrigatório!", null);
            }

            Optional<ModelPessoa> pessoaOptional = servicePessoa.findById(agendamento.getPessoa().getId());
            if (!pessoaOptional.isPresent()) {
                return util.estruturaAPI(BigDecimal.valueOf(7), "Pessoa com o ID informado não foi encontrada!", null);
            }
            ModelPessoa pessoa = pessoaOptional.get();

            agendamento.setPessoa(pessoa);

            Agendamento savedAgendamento = serviceAgendamento.save(agendamento);

            String to = pessoa.getEmail();
            String subject = "Novo Agendamento Criado!!";
            String body = "<h1>Um novo agendamento foi realizado:</h1>" +
                    "<br><b>ID do Agendamento:</b> " + savedAgendamento.getId() +
                    "<br><b>Data:</b> " + savedAgendamento.getData() +
                    "<br><b>Hora:</b> " + savedAgendamento.getHora() +
                    "<br><b>Descrição:</b> " + savedAgendamento.getDescricao() +
                    "<br><b>Valor:</b> " + savedAgendamento.getValor();

            emailService.sendEmail(to, subject, body);

            return util.estruturaAPI(BigDecimal.ONE, "Agendamento realizado com sucesso!", savedAgendamento);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha ao realizar agendamento: " + e.getMessage(), null);
        }
    }


    @GetMapping("/consultar")
    public Map<String, Object> consultarAgendamentos() {
        try {
            List<Agendamento> agendamentos = serviceAgendamento.findAll();

            if (agendamentos.isEmpty()) {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Nenhum agendamento encontrado!", agendamentos);
            }

            return util.estruturaAPI(BigDecimal.ONE, "Agendamentos encontrados com sucesso!", agendamentos);
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na consulta: " + e.getMessage(), null);
        }
    }

    @GetMapping("/consultar/{id}")
    public Map<String, Object> consultarAgendamentoById(@PathVariable Long id) {
        try {
            Optional<Agendamento> agendamento = serviceAgendamento.findById(id);

            if (agendamento.isPresent()) {
                return util.estruturaAPI(BigDecimal.ONE, "Agendamento encontrado com sucesso!", agendamento.get());
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Agendamento não encontrado!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha na consulta: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deletarAgendamento(@PathVariable Long id) {
        try {
            Optional<Agendamento> agendamento = serviceAgendamento.findById(id);

            if (agendamento.isPresent()) {
                serviceAgendamento.deleteById(id);
                return util.estruturaAPI(BigDecimal.ONE, "Agendamento ("+id+") deletado com sucesso!", null);
            } else {
                return util.estruturaAPI(BigDecimal.valueOf(2), "Agendamento não encontrado!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return util.estruturaAPI(BigDecimal.ZERO, "Falha ao deletar agendamento: " + e.getMessage(), null);
        }
    }
}
