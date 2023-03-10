package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.mapper.PautaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.service.PautaService;
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
public class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CriarPautaDTO> criarPautaDtoJson;

    @MockBean
    private PautaService pautaService;

    private List<BuscarPautaDTO> pautas;
    private Pauta pauta1, pauta2;

    @BeforeEach
    public void inicializar() {
        pauta1 = new Pauta(1L, "pauta 1", PautaStatusEnum.AGUARDANDO_VOTACAO, new Assembleia(), null);
        pauta2 = new Pauta(2L, "pauta 2", PautaStatusEnum.AGUARDANDO_VOTACAO, new Assembleia(), null);

        pautas = new ArrayList<>();
        pautas.add(new BuscarPautaDTO(pauta1));
        pautas.add(new BuscarPautaDTO(pauta2));
    }

    @Test
    public void deveCriarUmaPautaEmUmaAssembleia() throws Exception {
        CriarPautaDTO criarPautaDTO = new CriarPautaDTO("pauta 1", 1L);
        when(pautaService.criarPauta(criarPautaDTO)).thenReturn(PautaMapper.buildPauta(criarPautaDTO));

        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarPautaDtoJson.write(criarPautaDTO).getJson()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Disabled
    @Test
    public void deveRetornarBadRequestAoCriarPautaEmAssembleiaInexistente() throws Exception {
        CriarPautaDTO criarPautaDTO = new CriarPautaDTO("pauta 1", 10L);
        when(pautaService.criarPauta(any(CriarPautaDTO.class)))
                .thenThrow(new RuntimeException("Assembleia não encontrada"));

        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveRetornarBadRequestAoCriarPautaComDTONull() throws Exception {
        when(pautaService.criarPauta(any(CriarPautaDTO.class))).thenReturn(null);

        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveRetornarBadRequestAoCriarPautaEmAssembleiaEncerrada() throws Exception {
        CriarPautaDTO pautaDTO = new CriarPautaDTO("pauta 1", 1L);
        when(pautaService.criarPauta(any(CriarPautaDTO.class)))
                .thenThrow(new RuntimeException("Assembleia já encerrada"));

        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarPautaPorId() throws Exception {

        when(pautaService.buscarPautaPorId(1L)).thenReturn(new BuscarPautaDTO(pauta1));

        mockMvc.perform(get("/pautas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarPautaPorIdInexistente() throws Exception {

        mockMvc.perform(get("/pautas/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarNotFoundAoBuscarPautaPorIdNegativo() throws Exception {

        mockMvc.perform(get("/pautas/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarPautaPorIdInvalido() throws Exception {

        mockMvc.perform(get("/pautas/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarTodasAsPautas() throws Exception {

        when(pautaService.buscarTodasAsPautas()).thenReturn(pautas);

        mockMvc.perform(get("/pautas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarTodasAsPautasVazio() throws Exception {
        List<BuscarPautaDTO> pautas = new ArrayList<>();
        when(pautaService.buscarTodasAsPautas()).thenReturn(pautas);

        mockMvc.perform(get("/pautas"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveBuscarPautasPorAssembleia() throws Exception {

        when(pautaService.buscarPautasPorAssembleia(any(Long.class))).thenReturn(pautas);

        mockMvc.perform(get("/pautas/assembleia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarPautasPorAssembleiaVazio() throws Exception {
        List<BuscarPautaDTO> pautas = new ArrayList<>();
        when(pautaService.buscarPautasPorAssembleia(any(Long.class))).thenReturn(pautas);

        mockMvc.perform(get("/pautas/assembleia/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarNotFoundAoBuscarPautasPorAssembleiaInexistente() throws Exception {
        mockMvc.perform(get("/pautas/assembleia/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarPautasPorAssembleiaInvalido() throws Exception {
        mockMvc.perform(get("/pautas/assembleia/abc"))
                .andExpect(status().isBadRequest());
    }

}