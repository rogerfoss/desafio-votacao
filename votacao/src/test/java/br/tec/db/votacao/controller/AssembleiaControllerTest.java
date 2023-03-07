package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.service.AssembleiaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AssembleiaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssembleiaService assembleiaService;

    @Test
    public void deveCriarAssembleia() throws Exception {

        when(assembleiaService.criarAssembleia(any(CriarAssembleiaDTO.class))).thenReturn(new Assembleia());

        mockMvc.perform(post("/assembleias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"inicio\": \"2023-09-01T10:00:00\"}"))
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
        Assembleia assembleia = new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null);
        List<BuscarAssembleiaDTO> assembleias = new ArrayList<>();
        assembleias.add(new BuscarAssembleiaDTO(assembleia));
        when(assembleiaService.buscarTodasAssembleias()).thenReturn(assembleias);

        mockMvc.perform(get("/assembleias"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void deveRetornarNotFoundSeEstiverVazioAoBuscarTodasAssembleias() throws Exception {
        when(assembleiaService.buscarTodasAssembleias()).thenReturn(List.of());

        mockMvc.perform(get("/assembleias"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveBuscarAssembleiaPorId() throws Exception {
        Assembleia assembleia = new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null);
        when(assembleiaService.buscarAssembleiaPorId(any(Long.class))).thenReturn(new BuscarAssembleiaDTO(assembleia));

        mockMvc.perform(get("/assembleias/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornarNotFoundAoBuscarAssembleiaPorIdInexistente() throws Exception {
        when(assembleiaService.buscarAssembleiaPorId(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/assembleias/85"))
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
        Assembleia assembleia = new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null);
        List<BuscarAssembleiaDTO> assembleias = new ArrayList<>();
        assembleias.add(new BuscarAssembleiaDTO(assembleia));
        when(assembleiaService.buscarTodasAssembleias()).thenReturn(assembleias);

        mockMvc.perform(put("/assembleias/1"))
                .andExpect(status().isOk());
    }

}
