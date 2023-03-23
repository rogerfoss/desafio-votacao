package br.tec.db.votacao.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static br.tec.db.votacao.SqlProvider.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String URL = "/votos";

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertSessaoDeVotacao),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveVotarEmUmaSessao() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"1\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("NAO"))
                .andExpect(jsonPath("$.associado.id").value(1));
    }

    @Test
    public void deveRetornarBadRequestAoVotarSemInformarStatus() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idSessaoDeVotacao\":\"1\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagemCampo").value("O status do voto precisa ser informado (SIM/NAO)"));
    }

    @Test
    public void deveRetornarBadRequestAoVotarComStatusInvalido() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SIMM\",\"idSessaoDeVotacao\":\"1\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Failed to read request"));
    }

    @Test
    public void deveRetornarBadRequestAoVotarSemInformarIdSessaoDeVotacao() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagemCampo").value("O ID da sessão de votação precisa ser informado"));
    }

    @Test
    public void deveRetornarBadRequestAoVotarComIdSessaoDeVotacaoInvalido() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"abc\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Failed to read request"));
    }

    @Test
    public void deveRetornarBadRequestAoVotarSemInformarIdAssociado() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagemCampo").value("O ID do associado precisa ser informado"));
    }

    @Test
    public void deveRetornarBadRequestAoVotarComIdAssociadoInvalido() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"1\",\"idAssociado\":\"abc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Failed to read request"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertSessaoDeVotacao),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveRetornarBadRequestAoVotarSeSessaoJaEncerrada() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"2\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Sessão de votação encerrada"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveRetornarBadRequestAoVotarSeAssociadoJaVotou() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"1\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Associado já votou nesta sessão"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveRetornarNotFoundAoVotarEmSessaoInexistente() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"99\",\"idAssociado\":\"1\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Sessão de votação não encontrada"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveRetornarNotFoundAoVotarComAssociadoInexistente() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"NAO\",\"idSessaoDeVotacao\":\"1\",\"idAssociado\":\"99\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Associado não encontrado"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveBuscarTodosOsVotos() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveBuscarVotoPorId() throws Exception {
        mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("SIM"));
    }

    @Test
    public void deveRetornarBadRequestAoBuscarVotoPorIdInvalido() throws Exception {
        mockMvc.perform(get(URL + "/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Failed to convert 'null' with value: 'abc'"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveRetornarNotFoundAoBuscarVotoPorIdInexistente() throws Exception {
        mockMvc.perform(get(URL + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Voto não encontrado"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveBuscarVotosPorSessaoDeVotacao() throws Exception {
        mockMvc.perform(get("/votos/sessao/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarBadRequestAoBuscarVotosPorSessaoDeVotacaoComIdInvalido() throws Exception {
        mockMvc.perform(get("/votos/sessao/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Failed to convert 'null' with value: 'abc'"));
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVoto),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetarDB)
    })
    public void deveRetornarNotFoundAoBuscarVotosPorSessaoDeVotacaoComIdInexistente() throws Exception {
        mockMvc.perform(get("/votos/sessao/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Sessão de votação não encontrada"));
    }


}