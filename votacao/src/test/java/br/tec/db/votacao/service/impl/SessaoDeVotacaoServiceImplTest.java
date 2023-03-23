package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.enums.VotoStatusEnum;
import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.SessaoDeVotacaoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.repository.PautaRepository;
import br.tec.db.votacao.repository.SessaoDeVotacaoRepository;
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
class SessaoDeVotacaoServiceImplTest {

    private SessaoDeVotacaoServiceImpl sessaoDeVotacaoService;
    private CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO;
    private SessaoDeVotacao sessaoDeVotacao;
    private Pauta pauta;

    @Mock
    private SessaoDeVotacaoRepository sessaoDeVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @BeforeEach
    public void inicializar() {
        sessaoDeVotacaoService = new SessaoDeVotacaoServiceImpl(sessaoDeVotacaoRepository, pautaRepository);
        criarSessaoDeVotacaoDTO = new CriarSessaoDeVotacaoDTO(LocalDateTime.now(), 1L);

        pauta = new Pauta(1L, "Pauta 1", PautaStatusEnum.CRIADA);
        sessaoDeVotacao = new SessaoDeVotacao();
        sessaoDeVotacao.setPauta(pauta);
    }

    @Test
    public void deveCriarSessaoDeVotacao() {
        when(sessaoDeVotacaoRepository.save(any(SessaoDeVotacao.class)))
                .thenReturn(SessaoDeVotacaoMapper.buildSessaoDeVotacao(criarSessaoDeVotacaoDTO));

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        sessaoDeVotacao = sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDTO);

        assertThat(sessaoDeVotacao.getStatus()).isEqualTo(SessaoDeVotacaoStatusEnum.INICIADA);
        assertThat(sessaoDeVotacao.getInicio()).isEqualTo(criarSessaoDeVotacaoDTO.inicio());
        assertThat(sessaoDeVotacao.getPauta().getId()).isEqualTo(criarSessaoDeVotacaoDTO.idPauta());
        verify(pautaRepository).findById(1L);
        verify(sessaoDeVotacaoRepository).save(any(SessaoDeVotacao.class));
    }

    @Test
    public void deveRetornarNotFoundSePautaNaoEncontradaAoCriarSessaoDeVotacao() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDTO));

        verify(pautaRepository).findById(anyLong());
    }

    @Test
    public void deveRetornarBadRequestSePautaJaEstiverDefinidaAoCriarSessaoDeVotacao() {
        pauta.setStatus(PautaStatusEnum.APROVADA);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        assertThrows(BadRequestException.class,
                () -> sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDTO));

        verify(pautaRepository).findById(1L);
        verifyNoInteractions(sessaoDeVotacaoRepository);
    }

    @Test
    public void deveRetornarBadRequestAoCriarSessaoSeJaHouverSessaoNaPauta() {
        pauta.setStatus(PautaStatusEnum.AGUARDANDO_VOTACAO);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        assertThrows(BadRequestException.class,
                () -> sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDTO));

        verify(pautaRepository).findById(1L);
        verifyNoInteractions(sessaoDeVotacaoRepository);
    }

    @Test
    public void deveBuscarSessaoPorId() {
        when(sessaoDeVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoDeVotacao));
        BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoDTO = sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(1L);

        assertThat(sessaoDeVotacao.getId()).isEqualTo(buscarSessaoDeVotacaoDTO.id());
        verify(sessaoDeVotacaoRepository).findById(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarSessaoPorIdInexistente() {
        when(sessaoDeVotacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(99L));

        verify(sessaoDeVotacaoRepository).findById(99L);
    }

    @Test
    void deveBuscarTodasAsSessoesDeVotacao() {
        List<SessaoDeVotacao> sessoesDeVotacao = new ArrayList<>();
        SessaoDeVotacao sessaoDeVotacao2 = new SessaoDeVotacao();
        sessaoDeVotacao2.setPauta(pauta);
        sessoesDeVotacao.add(sessaoDeVotacao);
        sessoesDeVotacao.add(sessaoDeVotacao2);

        when(sessaoDeVotacaoRepository.findAll()).thenReturn(sessoesDeVotacao);
        List<BuscarSessaoDeVotacaoDTO> buscarSessoesDTO = sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao();

        assertThat(buscarSessoesDTO.size()).isEqualTo(2);
        verify(sessaoDeVotacaoRepository).findAll();
    }

    @Test
    void deveEncerrarSessaoDeVotacao() {
        sessaoDeVotacao.setStatus(SessaoDeVotacaoStatusEnum.INICIADA);
        when(sessaoDeVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoDeVotacao));

        sessaoDeVotacaoService.encerrarSessaoDeVotacao(1L);

        assertThat(sessaoDeVotacao.getStatus()).isEqualTo(SessaoDeVotacaoStatusEnum.ENCERRADA);
        verify(sessaoDeVotacaoRepository).findById(1L);
        verify(sessaoDeVotacaoRepository).save(sessaoDeVotacao);
    }

    @Test
    void deveLancarNotFoundAoEncerrarSessaoDeVotacaoInexistente() {
        when(sessaoDeVotacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> sessaoDeVotacaoService.encerrarSessaoDeVotacao(99L));

        verify(sessaoDeVotacaoRepository).findById(99L);
    }

    @Test
    void deveLancarBadRequestAoEncerrarSessaoDeVotacaoJaEncerrada() {
        sessaoDeVotacao.setStatus(SessaoDeVotacaoStatusEnum.ENCERRADA);
        when(sessaoDeVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoDeVotacao));

        assertThrows(BadRequestException.class,
                () -> sessaoDeVotacaoService.encerrarSessaoDeVotacao(1L));

        verify(sessaoDeVotacaoRepository).findById(1L);
    }

    @Test
    void calcularResultadoDaSessaoDeVotacao() {
        List<Voto> votos = new ArrayList<>();
        votos.add(new Voto(1L, VotoStatusEnum.SIM, new Associado()));
        votos.add(new Voto(2L, VotoStatusEnum.SIM, new Associado()));
        sessaoDeVotacao.setVotos(votos);

        when(sessaoDeVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoDeVotacao));

        sessaoDeVotacaoService.calcularResultadoDaSessaoDeVotacao(1L);

        assertThat(pauta.getStatus()).isEqualTo(PautaStatusEnum.APROVADA);
        verify(sessaoDeVotacaoRepository).findById(1L);
        verify(pautaRepository).save(pauta);
    }

    @Test
    void deveLancarNotFoundAoCalcularResultadoDaSessaoDeVotacaoInexistente() {
        when(sessaoDeVotacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> sessaoDeVotacaoService.calcularResultadoDaSessaoDeVotacao(99L));

        verify(sessaoDeVotacaoRepository).findById(99L);
    }

}