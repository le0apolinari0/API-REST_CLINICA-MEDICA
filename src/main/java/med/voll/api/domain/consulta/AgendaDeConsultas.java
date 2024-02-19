package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendaConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamneto.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidacaoExecption;

import java.util.List;

@Service
public class AgendaDeConsultas {
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private List<ValidadorAgendaConsulta> validadores; //Interface-validadorAgendaConsulta

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){
        if(!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoExecption("Id do Paciente informado não existe ! ");

        }if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoExecption("Id do Medico informado não existe ! ");
        }

        validadores.forEach(valdi -> valdi.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        if (medico == null){
            throw new ValidacaoExecption(" Não possui Medicos com agenda Livre nesta  data ! ");

        }
        var consulta = new Consulta(null, medico, paciente, dados.data(),null);
        consultaRepository.save(consulta);
        return  new DadosDetalhamentoConsulta(consulta);

    }


    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());

        }if (dados.especialidade() == null){
            throw new ValidacaoExecption(" Campo Especialidade e Obrigatório quando Campo Medico não for Escolhido! ");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(),dados.data());
    }
    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadorCancelamento;
    public void cancelar( DadosCancelarConsulta dados){
        if (!consultaRepository.existsById(dados.idConsulta())){
            throw new ValidacaoExecption(" ID da Consulta Não Existe! ");
        }
        validadorCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }
}


