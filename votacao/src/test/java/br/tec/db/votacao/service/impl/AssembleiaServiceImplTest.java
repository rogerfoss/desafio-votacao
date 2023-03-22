package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.AssembleiaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.repository.AssembleiaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        assertThat(assembleia.getStatus()).isEqualTo(AssembleiaStatusEnum.INICIADA);
        assertThat(assembleia.getInicio()).isEqualTo(criarAssembleiaDTO.inicio());
        verify(assembleiaRepository).save(any(Assembleia.class));
    }

    @Test
    void deveBuscarTodasAssembleias() {
        List<Assembleia> assembleias = new ArrayList<>();
        Assembleia assembleia = new Assembleia();
        Assembleia assembleia2 = new Assembleia();
        assembleias.add(assembleia);
        assembleias.add(assembleia2);
        when(assembleiaRepository.findAll()).thenReturn(assembleias);
        List<BuscarAssembleiaDTO> assembleiasDTO = assembleiaService.buscarTodasAssembleias();

        assertThat(assembleiasDTO).hasSize(2);
        verify(assembleiaRepository).findAll();
    }

    @Test
    void deveBuscarAssembleiaPorId() {
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.of(assembleia));
        BuscarAssembleiaDTO assembleiaDTO = assembleiaService.buscarAssembleiaPorId(1L);

        assertThat(assembleia.getId()).isEqualTo(assembleiaDTO.id());
        verify(assembleiaRepository).findById(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarAssembleiaPorIdInexistente() {
        when(assembleiaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> assembleiaService.buscarAssembleiaPorId(99L));
        verify(assembleiaRepository).findById(99L);
    }

    @Test
    void deveFinalizarAssembleia() {
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.of(assembleia));
        assembleiaService.finalizarAssembleia(1L);

        assertThat(assembleia.getStatus()).isEqualTo(AssembleiaStatusEnum.ENCERRADA);
        verify(assembleiaRepository).findById(1L);
        verify(assembleiaRepository).save(assembleia);
    }

    @Test
    void deveLancarBadRequestAoFinalizarAssembleiaJaEncerrada() {
        assembleia.setStatus(AssembleiaStatusEnum.ENCERRADA);
        when(assembleiaRepository.findById(1L)).thenReturn(Optional.of(assembleia));

        assertThrows(BadRequestException.class, () -> assembleiaService.finalizarAssembleia(1L));
        verify(assembleiaRepository).findById(1L);
        verifyNoMoreInteractions(assembleiaRepository);
    }

    @Test
    void deveLancarNotFoundAoFinalizarAssembleiaInexistente() {
        when(assembleiaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> assembleiaService.finalizarAssembleia(99L));
        verify(assembleiaRepository).findById(99L);
        verifyNoMoreInteractions(assembleiaRepository);
    }

}