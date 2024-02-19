package med.voll.api.controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {
        @Autowired
        private MockMvc mvc;
        @Autowired
        private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;
        //tem que trablhar com o mesmo metodo/parametro que esta no controller-dto(chega na api)

        @Autowired
        private JacksonTester<DadosMedicoDetalhado> dadosDetalhamentoMedicoJson;
        //tem que trablhar com o mesmo metodo/parametro que esta no controller-dto(devolve para api)

        @MockBean
        private MedicoRepository repository ;
        //Simula uma Bd no teste

        @Test
        @DisplayName(" Deve devolver codigo http 400 quando informacoes estao invalidas.")
        @WithMockUser
        //simula um usuario logado
        void cadastrarMedicocenario1()throws Exception   {
            var response =  mvc.perform(post("/medicos")).andReturn().getResponse();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());


        }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas")
    @WithMockUser
    @Autowired
    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua-10",
                "bairro",
                "12235",
                "Campinas",
                "SP",
                null,
                "25"

        );
    }

    void cadastrarMedicocenario2() throws Exception {
        var dadosCadastro = new DadosCadastroMedico(
                "Medico Vander",
                "medico@voll.med",
                "12988456987",
                "003450",
                Especialidade.CARDIOLOGIA,
                dadosEndereco());

        when(repository.save(any())).thenReturn(new Medico(dadosCadastro));

        var response = mvc
                .perform(post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJson.write(dadosCadastro).getJson()))
                .andReturn().getResponse();

        var dadosDetalhamento = new DadosMedicoDetalhado(

                null,
                dadosCadastro.nome(),
                dadosCadastro.email(),
                dadosCadastro.crm(),
                dadosCadastro.telefone(),
                dadosCadastro.especialidade(),
                new Endereco(dadosCadastro.endereco())
        );
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);


    }
}
