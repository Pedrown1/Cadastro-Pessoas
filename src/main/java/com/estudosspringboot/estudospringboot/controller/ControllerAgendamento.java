package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.model.*;
import com.estudosspringboot.estudospringboot.service.*;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/agendamento")
public class ControllerAgendamento {

    @Autowired
    private ServiceAgendamento serviceAgendamento;

    @Autowired
    private ServicePessoa servicePessoa;

    @Autowired
    private ServiceEstabelecimento serviceEstabelecimento;

    @Autowired
    private ServiceProfissional serviceProfissional;

    @Autowired
    private ServiceServico serviceServico;

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

            if (agendamento.getPessoa() == null || agendamento.getPessoa().getId() == null) {
                return util.estruturaAPI(BigDecimal.valueOf(6), "ID da pessoa é obrigatório!", null);
            }

            if (agendamento.getServico() == null || agendamento.getServico().getId() == null) {
                return util.estruturaAPI(BigDecimal.valueOf(9), "ID do serviço é obrigatório!", null);
            }

            if (agendamento.getProfissional() == null || agendamento.getProfissional().getId() == null) {
                return util.estruturaAPI(BigDecimal.valueOf(11), "ID do profissional é obrigatório!", null);
            }

            if (agendamento.getEstabelecimento() == null || agendamento.getEstabelecimento().getId() == null) {
                return util.estruturaAPI(BigDecimal.valueOf(13), "ID do estabelecimento é obrigatório!", null);
            }

            Optional<Servico> servicoOptional = serviceServico.findById(agendamento.getServico().getId());
            if (!servicoOptional.isPresent()) {
                return util.estruturaAPI(BigDecimal.valueOf(10), "Serviço com o ID informado não foi encontrado!", null);
            }
            Servico servico = servicoOptional.get();

            Optional<Pessoa> pessoaOptional = servicePessoa.findById(agendamento.getPessoa().getId());
            if (!pessoaOptional.isPresent()) {
                return util.estruturaAPI(BigDecimal.valueOf(7), "Pessoa com o ID informado não foi encontrada!", null);
            }
            Pessoa pessoa = pessoaOptional.get();

            Optional<Profissional> profissionalOptional = serviceProfissional.findById(agendamento.getProfissional().getId());
            if (!profissionalOptional.isPresent()) {
                return util.estruturaAPI(BigDecimal.valueOf(12), "Profissional com o ID informado não foi encontrado!", null);
            }
            Profissional profissional = profissionalOptional.get();

            Optional<Estabelecimento> estabelecimentoOptional = serviceEstabelecimento.findById(agendamento.getEstabelecimento().getId());
            if (!estabelecimentoOptional.isPresent()) {
                return util.estruturaAPI(BigDecimal.valueOf(14), "Estabelecimento com o ID informado não foi encontrado!", null);
            }
            Estabelecimento estabelecimento = estabelecimentoOptional.get();

            if (!servico.getEstabelecimento().getId().equals(estabelecimento.getId())) {
                return util.estruturaAPI(BigDecimal.valueOf(15),
                        "O serviço informado não pertence ao estabelecimento informado!", null);
            }

            if (!profissional.getEstabelecimento().getId().equals(estabelecimento.getId())) {
                return util.estruturaAPI(BigDecimal.valueOf(16),
                        "O profissional informado não pertence ao estabelecimento informado!", null);
            }

            agendamento.setDescricao(servico.getDescricao());
            agendamento.setValor(servico.getValor());
            agendamento.setPessoa(pessoa);
            agendamento.setServico(servico);
            agendamento.setProfissional(profissional);
            agendamento.setEstabelecimento(estabelecimento);

            if (serviceAgendamento.existeConflitoDeHorario(agendamento)) {
                return util.estruturaAPI(BigDecimal.valueOf(8), "Já existe outro agendamento neste intervalo de horário!", null);
            }

            Agendamento savedAgendamento = serviceAgendamento.save(agendamento);

            String to = pessoa.getEmail();
            String subject = "Novo Agendamento Criado!";
            String body = serviceAgendamento.gerarBodyEmail(savedAgendamento);

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
