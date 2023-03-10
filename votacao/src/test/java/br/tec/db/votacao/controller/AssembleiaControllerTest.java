package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.mapper.AssembleiaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.service.AssembleiaService;
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
public class AssembleiaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CriarAssembleiaDTO> CriarAssembleiaDtoJson;

    @MockBean
    private AssembleiaService assembleiaService;

    private List<BuscarAssembleiaDTO> assembleias;
    private Assembleia assembleia1, assembleia2;

    @BeforeEach
    public void inicializar() {
        assembleia1 = new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null);
        assembleia2 = new Assembleia(2L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null);

        assembleias = new ArrayList<>();
        assembleias.add(new BuscarAssembleiaDTO(assembleia1));
        assembleias.add(new BuscarAssembleiaDTO(assembleia2));
    }


    @Test
    public void deveCriarAssembleia() throws Exception {

        CriarAssembleiaDTO criarAssembleiaDTO = new CriarAssembleiaDTO(LocalDateTime.now());

        when(assembleiaService.criarAssembleia(criarAssembleiaDTO))
                .thenReturn(AssembleiaMapper.buildAssembleia(criarAssembleiaDTO));

        mockMvc.perform(post("/assembleias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CriarAssembleiaDtoJson.write(criarAssembleiaDTO).getJson()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornarBadRequestAoCriarAssembleiaComDTONull() throws Exception {
        when(assembleiaService.criarAssembleia(any(CriarAssembleiaDTO.class))).thenReturn(null);

        mockMvc.perform(post("/assembleias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveBuscarTodasAssembleias() throws Exception {

        when(assembleiaService.buscarTodasAssembleias()).thenReturn(assembleias);

        mockMvc.perform(get("/assembleias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void deveRetornarNotFoundSeNaoHouverAssembleias() throws Exception {
        List<BuscarAssembleiaDTO> assembleias = new ArrayList<>();

        when(assembleiaService.buscarTodasAssembleias()).thenReturn(assembleias);

        mockMvc.perform(get("/assembleias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveBuscarAssembleiaPorId() throws Exception {
        when(assembleiaService.buscarAssembleiaPorId(1L)).thenReturn(new BuscarAssembleiaDTO(assembleia1));

        mockMvc.perform(get("/assembleias/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarAssembleiaPorIdInexistente() throws Exception {
        mockMvc.perform(get("/assembleias/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarNotFoundAoBuscarAssembleiaPorIdNegativo() throws Exception {
        mockMvc.perform(get("/assembleias/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarBadRequestAoBuscarAssembleiaPorIdInvalido() throws Exception {
        mockMvc.perform(get("/assembleias/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveFinalizarAssembleia() throws Exception {
        mockMvc.perform(put("/assembleias/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
