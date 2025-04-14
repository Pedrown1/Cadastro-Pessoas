package com.estudosspringboot.estudospringboot.service;


import com.estudosspringboot.estudospringboot.model.Agendamento;
import com.estudosspringboot.estudospringboot.repositorio.RepositoryAgendamento;
import com.estudosspringboot.estudospringboot.utils.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceAgendamento {

    @Autowired
    private Utilidades util;

    @Autowired
    private RepositoryAgendamento repositoryAgendamento;

    public Agendamento save(Agendamento agendamento) {
        return repositoryAgendamento.save(agendamento);
    }

    public List<Agendamento> findAll() {
        return repositoryAgendamento.findAll();
    }

    public Optional<Agendamento> findById(Long id) {
        return repositoryAgendamento.findById(id);
    }

    public void deleteById(Long id) {
        repositoryAgendamento.deleteById(id);
    }

    public boolean dataHoraObrigatoriedade(Agendamento agendamento){
        return agendamento.getPessoa() == null || agendamento.getData() == null || agendamento.getData().isBlank() || agendamento.getHora() == null || agendamento.getHora().isBlank();
    }

    public String dataHoraInvalida(Agendamento agendamento) {

        if (!util.validarFormatoData(agendamento.getData()) || !util.validarFormatoHora(agendamento.getHora())) {
            return "Formato inválido! Data deve ser dd/MM/yyyy e hora deve ser HH:mm.";
        }

        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate dataRecebida = LocalDate.parse(agendamento.getData(), formatterData);
        LocalDate dataAtual = LocalDate.now();

        if (dataRecebida.isBefore(dataAtual)) {
            return "Data Inválida! Não é possível realizar agendamento em dias retroativos ao vigente.";
        }

        if (dataRecebida.isEqual(dataAtual)) {
            LocalTime horaRecebida = LocalTime.parse(agendamento.getHora(), formatterHora);
            LocalTime horaAtual = LocalTime.now();

            if (horaRecebida.isBefore(horaAtual)) {
                return "Hora Inválida! Não é possível realizar agendamento em hora retroativa à atual.";
            }
        }

        return null;
    }

    public boolean existeConflitoDeHorario(Agendamento novoAgendamento) {
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate data = LocalDate.parse(novoAgendamento.getData(), formatterData);
        LocalTime hora = LocalTime.parse(novoAgendamento.getHora(), formatterHora);

        List<Agendamento> agendamentosDoDia = repositoryAgendamento.findByData(novoAgendamento.getData());

        for (Agendamento ag : agendamentosDoDia) {
            if (ag.getHora() == null || ag.getHora().isBlank()) continue;

            LocalTime horaAgendada = LocalTime.parse(ag.getHora(), formatterHora);
            long diferencaMinutos = Math.abs(java.time.Duration.between(horaAgendada, hora).toMinutes());
            if (diferencaMinutos < 60) {
                return true;
            }
        }
        return false;
    }



}
