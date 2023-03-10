package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.mapper.AssembleiaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.repository.AssembleiaRepository;
import br.tec.db.votacao.service.impl.AssembleiaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssembleiaServiceImplTest {

    private AssembleiaServiceImpl assembleiaService;

    private CriarAssembleiaDTO criarAssembleiaDTO;

    private Assembleia assembleia;

    @Mock
    private AssembleiaRepository assembleiaRepository;

    @BeforeEach
    public void inicializar() {
        assembleiaService = new AssembleiaServiceImpl(assembleiaRepository);
        criarAssembleiaDTO = new CriarAssembleiaDTO(LocalDateTime.now());
        assembleia = new Assembleia();
        assembleia.setStatus(AssembleiaStatusEnum.INICIADA);
    }

    @Test
    void deveCriarAssembleia() {
        when(assembleiaRepository.save(any(Assembleia.class)))
                .thenReturn(AssembleiaMapper.buildAssembleia(criarAssembleiaDTO));

        Assembleia assembleia = assembleiaService.criarAssembleia(criarAssembleiaDTO);
        assertNotNull(assembleia);
        assertEquals(assembleia.getStatus(), AssembleiaStatusEnum.INICIADA);
    }

    @Test
    void deveLancarExcecaoAoCriarAssembleiaSeNaoSalvar() {
        CriarAssembleiaDTO criarAssembleiaDTO = new CriarAssembleiaDTO(null);
        when(assembleiaRepository.save(any(Assembleia.class))).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> assembleiaService.criarAssembleia(criarAssembleiaDTO));
    }

    @Test
    void deveBuscarTodasAssembleias() {
        List<Assembleia> assembleias = new ArrayList<>();
        Assembleia assembleia = new Assembleia();
        Assembleia assembleia2 = new Assembleia();
        assembleias.add(assembleia);
        assembleias.add(assembleia2);
        when(assembleiaRepository.findAll()).thenReturn(assembleias);
        List<BuscarAssembleiaDTO> assembleiaDTOS = assembleiaService.buscarTodasAssembleias();
        assertNotNull(assembleiaDTOS);
        assertEquals(assembleiaDTOS.size(), 2);
    }

    @Test
    void deveBuscarAssembleiaPorId() {
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.of(assembleia));
        BuscarAssembleiaDTO assembleiaDTO = assembleiaService.buscarAssembleiaPorId(1L);
        assertNotNull(assembleiaDTO);
        assertEquals(assembleia.getStatus(), AssembleiaStatusEnum.INICIADA);
    }

    @Test
    void deveLancarExcecaoAoBuscarAssembleiaPorIdInexistente() {
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> assembleiaService.buscarAssembleiaPorId(1L));
    }

    @Test
    void deveFinalizarAssembleia() {
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.of(assembleia));
        assembleiaService.finalizarAssembleia(1L);
        assertEquals(assembleia.getStatus(), AssembleiaStatusEnum.ENCERRADA);
    }

    @Test
    void deveLancarExcecaoAoFinalizarAssembleiaJaEncerrada() {
        assembleia.setStatus(AssembleiaStatusEnum.ENCERRADA);
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.of(assembleia));
        assertThrows(RuntimeException.class, () -> assembleiaService.finalizarAssembleia(1L));
    }

    @Test
    void deveLancarExcecaoAoFinalizarAssembleiaInexistente() {
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> assembleiaService.finalizarAssembleia(1L));
    }

}