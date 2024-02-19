package med.voll.api.domain.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.infra.exception.ValidacaoExecption;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendaConsulta {
    @Autowired
    private MedicoRepository repository;
    public void validar(DadosAgendamentoConsulta dados) {
        //escolha do medico e opcional
        if (dados.idMedico() == null) {
            return;
        }
        var medicoEstaAtivo = repository.findAtivoById(dados.idMedico());
        if (!medicoEstaAtivo) {
            throw new ValidacaoExecption(" A Consulta não pode ser agendada,Medicos Selecionados não estão ativos para agendamento! ");
        }
    }
}

