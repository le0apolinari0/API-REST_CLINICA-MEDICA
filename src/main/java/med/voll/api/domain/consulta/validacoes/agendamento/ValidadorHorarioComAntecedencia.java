package med.voll.api.domain.consulta.validacoes.agendamento;

import org.springframework.stereotype.Component;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidacaoExecption;

import java.time.Duration;
import java.time.LocalDateTime;
@Component
public class ValidadorHorarioComAntecedencia implements ValidadorAgendaConsulta {
    public void validar(DadosAgendamentoConsulta dados){

        var dataConsulta = dados.data();
        var  horaAtual = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(horaAtual,dataConsulta).toMinutes();

        if (diferencaEmMinutos < 30 ){
            throw new ValidacaoExecption("Agendamento de Consultas devem ser Feitas com 30 minutos de Antecedência !");
        }
    }
}
