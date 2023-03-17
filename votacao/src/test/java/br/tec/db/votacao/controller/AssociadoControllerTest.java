package br.tec.db.votacao.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static br.tec.db.votacao.SqlProvider.insertAssociado;
import static br.tec.db.votacao.SqlProvider.resetarDB;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String URL = "/associados";

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    public void deveSalvarAssociado() throws Exception {

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"João da Silva\",\"cpf\": \"02996891074\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PODE_VOTAR"));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarAssociadoSemNome() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\": \"02996891074\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagemCampo").value("Nome precisa ser informado"));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarAssociadoSemCPF() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"João da Silva\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagemCampo").value("CPF precisa ser informado"));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarAssociadoComCPFInvalido() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"João da Silva\",\"cpf\": \"0299689107\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagemCampo").value("CPF inválido"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertAssociado),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveBuscarTodosOsAssociados() throws Exception {

        mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertAssociado),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveBuscarAssociadoPorID() throws Exception {

        mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Ferreira"));
    }

    @Test
    public void deveRetornarBadRequestAoBuscarAssociadoPorIdInvalido() throws Exception {
        mockMvc.perform(get(URL + "/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Failed to convert 'null' with value: 'abc'"));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarAssociadoPorIDInexistente() throws Exception {
        mockMvc.perform(get(URL + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Associado não encontrado"));
    }

}