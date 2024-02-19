package med.voll.api.domain.consulta.validacoes.cancelamneto;

import med.voll.api.domain.consulta.DadosCancelarConsulta;

public interface ValidadorCancelamentoDeConsulta {
    void validar(DadosCancelarConsulta dados);
}
