package com.estudosspringboot.estudospringboot.controller;

import com.estudosspringboot.estudospringboot.model.Agendamento;
import com.estudosspringboot.estudospringboot.model.Pessoa;
import com.estudosspringboot.estudospringboot.model.Servico;
import com.estudosspringboot.estudospringboot.service.EmailService;
import com.estudosspringboot.estudospringboot.service.ServiceAgendamento;
import com.estudosspringboot.estudospringboot.service.ServicePessoa;
import com.estudosspringboot.estudospringboot.service.ServiceServico;
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
public class AgendamentoController {

    @Autowired
    private ServiceAgendamento serviceAgendamento;

    @Autowired
    private ServicePessoa servicePessoa;

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

            agendamento.setDescricao(servico.getDescricao());
            agendamento.setValor(servico.getValor());
            agendamento.setPessoa(pessoa);
            agendamento.setServico(servico);

            if (serviceAgendamento.existeConflitoDeHorario(agendamento)) {
                return util.estruturaAPI(BigDecimal.valueOf(8), "Já existe outro agendamento neste intervalo de horário!", null);
            }

            Agendamento savedAgendamento = serviceAgendamento.save(agendamento);

            String to = pessoa.getEmail();
            String subject = "Novo Agendamento Criado!";
            String body = "<!DOCTYPE html>" +
                    "<html lang='pt-br'>" +
                    "<head><meta charset='UTF-8'><title>Agendamento Realizado!</title></head>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>" +
                    "<table align='center' width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>" +
                    "<tr><td style='padding: 20px; text-align: center; background-color: #007bff; color: white; border-radius: 8px 8px 0 0;'><h1 style='margin: 0;'>Novo Agendamento!</h1></td></tr>" +
                    "<tr><td style='padding: 20px; color: #333333; font-size: 16px;'>" +
                    "<p>Olá,</p><p>Um novo agendamento foi realizado com sucesso. Seguem os detalhes:</p>" +
                    "<table width='100%' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>" +
                    "<tr><td style='font-weight: bold; width: 150px;'>Data:</td><td>" + savedAgendamento.getData() + "</td></tr>" +
                    "<tr style='background-color: #f9f9f9;'><td style='font-weight: bold;'>Hora:</td><td>" + savedAgendamento.getHora() + "</td></tr>" +
                    "<tr><td style='font-weight: bold;'>Descrição:</td><td>" + savedAgendamento.getDescricao() + "</td></tr>" +
                    "<tr style='background-color: #f9f9f9;'><td style='font-weight: bold;'>Valor:</td><td>R$ " + String.format(Locale.US, "%.2f", savedAgendamento.getValor()) + "</td></tr>" +
                    "</table><p style='margin-top: 30px;'>Obrigado por usar nosso sistema!<br>Código Teste Pedro</p></td></tr>" +
                    "<tr><td style='padding: 10px; text-align: center; font-size: 12px; color: #777777; background-color: #f4f4f4; border-radius: 0 0 8px 8px;'>&copy; 2025 Sua Empresa. Todos os direitos reservados.</td></tr>" +
                    "</table></body></html>";

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
