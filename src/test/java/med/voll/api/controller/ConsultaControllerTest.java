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

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
    //tem que trablhar com o mesmo metodo/parametro que esta no controller-dto(chega na api)

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;
    //tem que trablhar com o mesmo metodo/parametro que esta no controller-dto(devolve para api)

    @MockBean
    private AgendaDeConsultas agendaDeConsultas;
    //Simula uma Bd no teste


    @Test
    @DisplayName(" Deve devolver codigo 400 quando informações estão invalidas/teste de unidade-teste isolado.")
    @WithMockUser
        //simula um usuario logado
    void agendarConsultaCenario1() throws Exception {
       var consultaResponse =  mvc.perform(post("/consultas")).andReturn().getResponse();

        assertThat(consultaResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());


    }

    @Test
    @DisplayName(" Deve devolver codigo 200 quando informações estão validas/teste de unidade-teste isolado.")
    @WithMockUser
        //simula um usuario logado
    void agendarConsultaCenario2() throws Exception {

        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.ORTOPEDIA;

        var dadosDetalhamento= new DadosDetalhamentoConsulta(null, 3l, 6l, data );
        when( agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);
        //antes da chamada do/consultaResponse-configuração do mock/mockito

        var consultaResponse =  mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoConsultaJson.write(
                                new DadosAgendamentoConsulta( 3l ,6l,data,especialidade))
                                  .getJson()))
                .andReturn().getResponse();

        assertThat(consultaResponse.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write( dadosDetalhamento).getJson();

        assertThat(consultaResponse.getContentAsString()).isEqualTo(jsonEsperado);
    }
    /*Para alguns componentes, como classes controller, podemos escrever testes de unidade;
    já para outros, como as interfaces repository, testes de integração são os mais recomendados.*/

}