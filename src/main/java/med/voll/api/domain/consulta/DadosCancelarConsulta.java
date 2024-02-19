package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DadosCancelarConsulta(
        @NotNull
        Long idConsulta,
        @NotNull
        MotivoCancelamento motivo) {
}

