package med.voll.api.domain.consulta.validacoes.agendamento;

import org.springframework.stereotype.Component;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidacaoExecption;

import java.time.DayOfWeek;
@Component
public class ValidadorDeHorarioFuncionamento implements ValidadorAgendaConsulta {
    public void validar(DadosAgendamentoConsulta dados){

        var dataConsulta = dados.data();
        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAbertura = dataConsulta.getHour() < 7;
        var depoisDoEncerramneto = dataConsulta.getHour() > 18;

        if ( domingo || antesDaAbertura || depoisDoEncerramneto){
            throw new ValidacaoExecption(" Consulta Agendada Fora do Horário de Funcionamento do Estabelecimento! ");
        }
    }
}
