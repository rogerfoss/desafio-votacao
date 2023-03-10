package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.enums.AssociadoStatusEnum;
import br.tec.db.votacao.mapper.AssociadoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.service.AssociadoService;
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
public class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CriarAssociadoDTO> criarAssociadoDtoJson;

    @MockBean
    private AssociadoService associadoService;

    private List<BuscarAssociadoDTO> associados;
    private Associado associado1, associado2;

    @BeforeEach
    public void inicializar() {
        associado1 = new Associado(1L, "João da Silva", "12345678901", AssociadoStatusEnum.PODE_VOTAR);
        associado2 = new Associado(2L, "Maria da Silva", "12345678902", AssociadoStatusEnum.PODE_VOTAR);

        associados = new ArrayList<>();
        associados.add(new BuscarAssociadoDTO(associado1));
        associados.add(new BuscarAssociadoDTO(associado2));
    }

    @Test
    public void deveSalvarUmNovoAssociado() throws Exception {
        CriarAssociadoDTO criarAssociadoDTO = new CriarAssociadoDTO("João da Silva", "12345678901");

        when(associadoService.salvarAssociado(criarAssociadoDTO))
                .thenReturn(AssociadoMapper.buildAssociado(criarAssociadoDTO));

        mockMvc.perform(post("/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarAssociadoDtoJson.write(criarAssociadoDTO).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deveImpedirSalvarAssociadoComCPFInvalido() throws Exception {
        CriarAssociadoDTO criarAssociadoDTO = new CriarAssociadoDTO("João da Silva", "123456789012");

        when(associadoService.salvarAssociado(criarAssociadoDTO)).thenThrow(new RuntimeException("CPF inválido"));

        mockMvc.perform(post("/associados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveRetornarBadRequestAoSalvarAssociadoComDTONull() throws Exception {
        when(associadoService.salvarAssociado(any(CriarAssociadoDTO.class))).thenReturn(null);

        mockMvc.perform(post("/associados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void devebuscarAssociadoPorID() throws Exception {

        when(associadoService.buscarAssociadoPorId(1L)).thenReturn(new BuscarAssociadoDTO(associado1));

        mockMvc.perform(get("/associados/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarAssociadoPorIDInexistente() throws Exception {
        mockMvc.perform(get("/associados/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarAssociadoPorIdInvalido() throws Exception {

        mockMvc.perform(get("/associados/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarTodosOsAssociados() throws Exception {

        when(associadoService.buscarTodosOsAssociados()).thenReturn(associados);

        mockMvc.perform(get("/associados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarTodosOsAssociadosVazio() throws Exception {
        List<BuscarAssociadoDTO> associados = new ArrayList<>();

        when(associadoService.buscarTodosOsAssociados()).thenReturn(associados);

        mockMvc.perform(get("/associados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
