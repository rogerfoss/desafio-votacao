package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.enums.VotoStatusEnum;
import br.tec.db.votacao.mapper.VotoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<VotarDTO> votarDtoJson;

    @MockBean
    private VotoService votoService;

    private List<BuscarVotoDTO> votos;
    private Voto voto1, voto2;

    @BeforeEach
    public void inicializar() {
        voto1 = new Voto(1L, VotoStatusEnum.SIM, new SessaoDeVotacao(), new Associado());
        voto2 = new Voto(2L, VotoStatusEnum.NAO, new SessaoDeVotacao(), new Associado());

        votos = new ArrayList<>();
        votos.add(new BuscarVotoDTO(voto1));
        votos.add(new BuscarVotoDTO(voto2));
    }

    @Test
    public void deveVotarEmUmaSessao() throws Exception {
        VotarDTO votarDTO = new VotarDTO(VotoStatusEnum.SIM, 1L, 1L);
        when(votoService.votar(votarDTO)).thenReturn(VotoMapper.buildVoto(votarDTO));

        mockMvc.perform(post("/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(votarDtoJson.write(votarDTO).getJson()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornarBadRequestAoVotarEmSessaoInexistente() throws Exception {
        VotarDTO votarDTO = new VotarDTO(VotoStatusEnum.SIM, 99L, 1L);

        when(votoService.votar(votarDTO)).thenThrow(new RuntimeException("Sessão de votação inexistente"));

        mockMvc.perform(post("/votos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveRetornarBadRequestAoVotarEmSessaoEncerrada() throws Exception {
        VotarDTO votarDTO = new VotarDTO(VotoStatusEnum.SIM, 1L, 1L);

        when(votoService.votar(votarDTO)).thenThrow(new RuntimeException("Sessão de votação encerrada"));

        mockMvc.perform(post("/votos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarVotoPorId() throws Exception {
        when(votoService.buscarVotoPorId(1L)).thenReturn(new BuscarVotoDTO(voto1));

        mockMvc.perform(get("/votos/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Disabled
    @Test
    public void deveRetornarNotFoundAoBuscarVotoPorIdInexistente() throws Exception {

        mockMvc.perform(get("/votos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarVotoPorIdInvalido() throws Exception {

        mockMvc.perform(get("/votos/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarTodosOsVotos() throws Exception {

        when(votoService.buscarTodosOsVotos()).thenReturn(votos);

        mockMvc.perform(get("/votos"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarTodosOsVotosVazio() throws Exception {
        List<BuscarVotoDTO> votos = new ArrayList<>();
        when(votoService.buscarTodosOsVotos()).thenReturn(votos);

        mockMvc.perform(get("/votos"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveBuscarVotosPorSessaoDeVotacao() throws Exception {

        when(votoService.buscarVotosPorSessaoDeVotacao(any(Long.class))).thenReturn(votos);

        mockMvc.perform(get("/votos/sessao/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarVotosPorSessaoDeVotacaoVazio() throws Exception {
        List<BuscarVotoDTO> votos = new ArrayList<>();
        when(votoService.buscarVotosPorSessaoDeVotacao(any(Long.class))).thenReturn(votos);

        mockMvc.perform(get("/votos/sessao/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarVotosPorSessaoDeVotacaoComIdInvalido() throws Exception {

        mockMvc.perform(get("/votos/sessao/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveRetornarNotFoundAoBuscarVotosPorSessaoDeVotacaoComIdInexistente() throws Exception {

        mockMvc.perform(get("/votos/sessao/99"))
                .andExpect(status().isNotFound());
    }

}