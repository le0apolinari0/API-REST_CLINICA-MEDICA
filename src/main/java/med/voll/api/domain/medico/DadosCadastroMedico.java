package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank(message = " Campo Nome Médico é obrigatório")
        String nome,

        @NotBlank(message = "O Campo Email é obrigatório")
        @Email(message = "Formato do email Digitado é inválido")
        String email,

        @NotBlank(message = "O Campo Telefone é obrigatório")
        String telefone,

        @NotBlank(message = " O Campo CRM é obrigatório")
        @Pattern(regexp = "\\d{4,6}", message = "Formato do CRM Digitado é inválido")
        String crm,

        @NotNull(message = "O Campo Especialidade é obrigatória")
        Especialidade especialidade,

        @NotNull(message = "Os Dados do endereço são obrigatórios")
        @Valid DadosEndereco endereco) {

}
