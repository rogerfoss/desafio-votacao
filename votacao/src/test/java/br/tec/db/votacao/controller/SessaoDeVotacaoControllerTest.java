package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.mapper.SessaoDeVotacaoMapper;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.service.SessaoDeVotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class SessaoDeVotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CriarSessaoDeVotacaoDTO> criarSessaoDeVotacaoDtoJson;

    @MockBean
    private SessaoDeVotacaoService sessaoDeVotacaoService;

    private List<BuscarSessaoDeVotacaoDTO> sessoesDeVotacao;
    private SessaoDeVotacao sessaoDeVotacao1, sessaoDeVotacao2;

    @BeforeEach
    public void inicializar() {
        sessaoDeVotacao1 = new SessaoDeVotacao(1L, LocalDateTime.now(), null, SessaoDeVotacaoStatusEnum.INICIADA, new Pauta(), null);
        sessaoDeVotacao2 = new SessaoDeVotacao(2L, LocalDateTime.now(), null, SessaoDeVotacaoStatusEnum.INICIADA, new Pauta(), null);

        sessoesDeVotacao = new ArrayList<>();
        sessoesDeVotacao.add(new BuscarSessaoDeVotacaoDTO(sessaoDeVotacao1));
        sessoesDeVotacao.add(new BuscarSessaoDeVotacaoDTO(sessaoDeVotacao2));
    }

    @Test
    public void deveCriarUmaSessaoDeVotacaoEmUmaPauta() throws Exception {
        CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO = new CriarSessaoDeVotacaoDTO(LocalDateTime.now(), 1L);
        when(sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDTO)).thenReturn(SessaoDeVotacaoMapper.buildSessaoDeVotacao(criarSessaoDeVotacaoDTO));


        mockMvc.perform(post("/sessao-de-votacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarSessaoDeVotacaoDtoJson.write(criarSessaoDeVotacaoDTO).getJson()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornarBadRequestAoCriarSessaoDeVotacaoComDTONull() throws Exception {
        when(sessaoDeVotacaoService.criarSessaoDeVotacao(any(CriarSessaoDeVotacaoDTO.class))).thenReturn(null);

        mockMvc.perform(post("/sessao-de-votacao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveRetornarBadRequestAoCriarSessaoEmUmaPautaJaEncerrada() throws Exception {
        when(sessaoDeVotacaoService.criarSessaoDeVotacao(any(CriarSessaoDeVotacaoDTO.class))).thenThrow(new RuntimeException("Pauta já encerrada!"));

        mockMvc.perform(post("/sessao-de-votacao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarTodasAsSessoesDeVotacao() throws Exception {

        when(sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao()).thenReturn(sessoesDeVotacao);

        mockMvc.perform(get("/sessao-de-votacao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarTodasAsSessoesDeVotacaoVazio() throws Exception {
        List<BuscarSessaoDeVotacaoDTO> sessoesDeVotacao = new ArrayList<>();
        when(sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao()).thenReturn(sessoesDeVotacao);

        mockMvc.perform(get("/sessao-de-votacao"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveBuscarSessaoDeVotacaoPorId() throws Exception {

        when(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(1L)).thenReturn(new BuscarSessaoDeVotacaoDTO(sessaoDeVotacao1));

        mockMvc.perform(get("/sessao-de-votacao/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarSessaoDeVotacaoPorIdInexistente() throws Exception {

        mockMvc.perform(get("/sessao-de-votacao/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarSessaoDeVotacaoPorIdInvalido() throws Exception {

        mockMvc.perform(get("/sessao-de-votacao/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarSessaoDeVotacaoPorPauta() throws Exception {

        when(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorPauta(1L)).thenReturn(new BuscarSessaoDeVotacaoDTO(sessaoDeVotacao1));

        mockMvc.perform(get("/sessao-de-votacao/pauta/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarSessaoDeVotacaoPorPautaInexistente() throws Exception {

        mockMvc.perform(get("/sessao-de-votacao/pauta/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarSessaoDeVotacaoPorPautaInvalido() throws Exception {

        mockMvc.perform(get("/sessao-de-votacao/pauta/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveFinalizarUmaSessaoDeVotacao() throws Exception {

        mockMvc.perform(put("/sessao-de-votacao/encerrar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deveCalcularResultadoDaSessaoDeVotacao() throws Exception {

        mockMvc.perform(put("/sessao-de-votacao/resultado/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}



