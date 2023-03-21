package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.PautaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.repository.AssembleiaRepository;
import br.tec.db.votacao.repository.PautaRepository;
import br.tec.db.votacao.service.impl.PautaServiceImpl;
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
    private AssembleiaRepository assembleiaRepository;

    @BeforeEach
    public void inicializar() {
        pautaService = new PautaServiceImpl(pautaRepository, assembleiaRepository);
        criarPautaDTO = new CriarPautaDTO("Pauta 1", 1L);

        assembleia = new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, new ArrayList<>());
        pauta = new Pauta();
        pauta.setAssembleia(assembleia);
        assembleia.getPautas().add(pauta);
    }

    @Test
    public void deveCriarUmaPautaEmUmaAssembleia() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(PautaMapper.buildPauta(criarPautaDTO));

        when(assembleiaRepository.findById(any(Long.class))).thenReturn(Optional.of(assembleia));

        pauta = pautaService.criarPauta(criarPautaDTO);

        assertThat(pauta.getStatus()).isEqualTo(PautaStatusEnum.AGUARDANDO_VOTACAO);
        assertThat(pauta.getTitulo()).isEqualTo(criarPautaDTO.titulo());
        assertThat(pauta.getAssembleia().getId()).isEqualTo(criarPautaDTO.idAssembleia());

        verify(assembleiaRepository).findById(any(Long.class));
        verify(pautaRepository).save(any(Pauta.class));
    }

    @Test
    public void deveRetornarNotFoundSeAssembleiaNaoEncontradaAoCriarPauta() {
        when(assembleiaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pautaService.criarPauta(criarPautaDTO));
        verifyNoInteractions(pautaRepository);
    }

    @Test
    public void deveRetornarBadRequestSeAssembleiaJaEncerradaAoCriarPauta() {
        assembleia.setStatus(AssembleiaStatusEnum.ENCERRADA);
        when(assembleiaRepository.findById(any(Long.class))).thenReturn(Optional.of(assembleia));

        assertThrows(BadRequestException.class, () -> pautaService.criarPauta(criarPautaDTO));
        verifyNoInteractions(pautaRepository);
    }

    @Test
    void deveBuscarPautaPorId() {
        when(pautaRepository.findById(any(Long.class))).thenReturn(Optional.of(pauta));
        BuscarPautaDTO buscarPautaDTO = pautaService.buscarPautaPorId(1L);

        assertThat(pauta.getId()).isEqualTo(buscarPautaDTO.id());
        verify(pautaRepository).findById(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarPautaPorIdInexistente() {
        when(pautaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pautaService.buscarPautaPorId(99L));
        verify(pautaRepository).findById(99L);
    }

    @Test
    void deveBuscarTodasAsPautas() {
        List<Pauta> pautas = new ArrayList<>();
        Pauta pauta2 = new Pauta();
        pauta2.setAssembleia(assembleia);
        pautas.add(pauta);
        pautas.add(pauta2);

        when(pautaRepository.findAll()).thenReturn(pautas);
        List<BuscarPautaDTO> buscarPautasDTO = pautaService.buscarTodasAsPautas();

        assertThat(buscarPautasDTO).hasSize(2);
        verify(pautaRepository).findAll();
    }

    @Test
    void deveBuscarPautasPorAssembleia() {
        when(assembleiaRepository.findById(any(Long.class))).thenReturn(Optional.of(assembleia));

        List<BuscarPautaDTO> buscarPautasDTO = pautaService.buscarPautasPorAssembleia(1L);

        assertThat(buscarPautasDTO).hasSize(1);
        assertThat(pauta.getId()).isEqualTo(buscarPautasDTO.get(0).id());
        verify(assembleiaRepository).findById(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarPautasPorAssembleiaInexistente() {
        when(assembleiaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pautaService.buscarPautasPorAssembleia(99L));
        verify(assembleiaRepository).findById(99L);
    }

}