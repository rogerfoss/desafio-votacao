package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.PautaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.repository.PautaRepository;
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
class PautaServiceImplTest {

    private PautaServiceImpl pautaService;
    private CriarPautaDTO criarPautaDTO;
    private Pauta pauta;
    private Assembleia assembleia;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AssembleiaServiceImpl assembleiaService;

    @BeforeEach
    public void inicializar() {
        pautaService = new PautaServiceImpl(pautaRepository, assembleiaService);
        criarPautaDTO = new CriarPautaDTO("Pauta 1", 1L);

        assembleia = new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, new ArrayList<>());
        pauta = new Pauta();
        assembleia.getPautas().add(pauta);
    }

    @Test
    public void deveCriarUmaPautaEmUmaAssembleia() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(PautaMapper.buildPauta(criarPautaDTO));
        when(assembleiaService.buscarPorId(1L)).thenReturn(assembleia);

        pauta = pautaService.criarPauta(criarPautaDTO);

        assertThat(pauta.getStatus()).isEqualTo(PautaStatusEnum.CRIADA);
        assertThat(pauta.getTitulo()).isEqualTo(criarPautaDTO.titulo());

        verify(assembleiaService).buscarPorId(1L);
        verify(pautaRepository).save(any(Pauta.class));
    }

    @Test
    public void deveRetornarNotFoundSeAssembleiaNaoEncontradaAoCriarPauta() {
        criarPautaDTO = new CriarPautaDTO("Pauta 1", 99L);
        when(assembleiaService.buscarPorId(99L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> pautaService.criarPauta(criarPautaDTO));
        verify(assembleiaService).buscarPorId(99L);
        verifyNoInteractions(pautaRepository);
    }

    @Test
    public void deveRetornarBadRequestSeAssembleiaJaEncerradaAoCriarPauta() {
        assembleia.setStatus(AssembleiaStatusEnum.ENCERRADA);
        when(assembleiaService.buscarPorId(1L)).thenReturn(assembleia);

        assertThrows(BadRequestException.class, () -> pautaService.criarPauta(criarPautaDTO));
        verify(assembleiaService).buscarPorId(1L);
        verifyNoInteractions(pautaRepository);
    }

    @Test
    void deveBuscarPautaPorId() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        BuscarPautaDTO buscarPautaDTO = pautaService.buscarPautaPorId(1L);

        assertThat(pauta.getId()).isEqualTo(buscarPautaDTO.id());
        verify(pautaRepository).findById(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarPautaPorIdInexistente() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pautaService.buscarPautaPorId(99L));
        verify(pautaRepository).findById(99L);
    }

    @Test
    void deveBuscarTodasAsPautas() {
        List<Pauta> pautas = new ArrayList<>();
        Pauta pauta2 = new Pauta();
        pautas.add(pauta);
        pautas.add(pauta2);

        when(pautaRepository.findAll()).thenReturn(pautas);
        List<BuscarPautaDTO> buscarPautasDTO = pautaService.buscarTodasAsPautas();

        assertThat(buscarPautasDTO).hasSize(2);
        verify(pautaRepository).findAll();
    }

    @Test
    void deveBuscarPautasPorAssembleia() {
        when(assembleiaService.buscarPorId(1L)).thenReturn(assembleia);

        List<BuscarPautaDTO> buscarPautasDTO = pautaService.buscarPautasPorAssembleia(1L);

        assertThat(buscarPautasDTO).hasSize(1);
        assertThat(pauta.getId()).isEqualTo(buscarPautasDTO.get(0).id());
        assertThat(assembleia.getPautas().get(0).getId()).isEqualTo(buscarPautasDTO.get(0).id());
        verify(assembleiaService).buscarPorId(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarPautasPorAssembleiaInexistente() {
        when(assembleiaService.buscarPorId(99L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> pautaService.buscarPautasPorAssembleia(99L));
        verify(assembleiaService).buscarPorId(99L);
    }

}