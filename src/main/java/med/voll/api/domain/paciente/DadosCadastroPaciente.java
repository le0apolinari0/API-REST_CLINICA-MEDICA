package med.voll.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.Especialidade;

public record DadosCadastroPaciente(
        @NotBlank (message = "O campo Nome Paciente é obrigatório") String nome,
        @NotBlank(message = "O Campo Email é obrigatório")
        @Email(message = "Formato do email Digitado é inválido")
        String email,
        @NotBlank(message = "O Campo Telefone é obrigatório")
        String telefone,
        @NotBlank @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}") String cpf,
        @NotNull(message = "Os Dados do endereço são obrigatórios")
        @Valid DadosEndereco endereco){
}

