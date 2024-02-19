package med.voll.api.domain.consulta.validacoes.cancelamneto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelarConsulta;
import med.voll.api.infra.exception.ValidacaoExecption;

import java.time.Duration;
import java.time.LocalDateTime;

    @Component
    public class ValidarCancelarAgendamentoComAntecedencia implements ValidadorCancelamentoDeConsulta {

        @Autowired
        private ConsultaRepository repository;

        @Override
        public void validar(DadosCancelarConsulta dados) {
            var consulta = repository.getReferenceById(dados.idConsulta());
            var agora = LocalDateTime.now();
            var diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

            if (diferencaEmHoras < 24) {
                throw new ValidacaoExecption("Consulta somente pode ser cancelada com antecedência mínima de 24h!");
            }
        }
    }

