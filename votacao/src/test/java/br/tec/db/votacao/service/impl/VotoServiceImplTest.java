package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.enums.AssociadoStatusEnum;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.enums.VotoStatusEnum;
import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.VotoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.repository.VotoRepository;
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
class VotoServiceImplTest {

    private VotoServiceImpl votoService;
    private VotarDTO votarDTO;
    private Voto voto;
    private SessaoDeVotacao sessaoDeVotacao;
    private Associado associado;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoDeVotacaoServiceImpl sessaoDeVotacaoService;

    @Mock
    private AssociadoServiceImpl associadoService;

    @BeforeEach
    public void inicializar() {
        votoService = new VotoServiceImpl(votoRepository, sessaoDeVotacaoService, associadoService);
        votarDTO = new VotarDTO(VotoStatusEnum.SIM, 1L, 1L);
        sessaoDeVotacao = new SessaoDeVotacao(
                1L, LocalDateTime.now(), null, SessaoDeVotacaoStatusEnum.INICIADA, null, new ArrayList<>());

        associado = new Associado(1L, "Joao da Silva", "52342310030", AssociadoStatusEnum.PODE_VOTAR);
        voto = new Voto(1L, VotoStatusEnum.SIM, associado);
    }

    @Test
    public void deveSalvarUmNovoVoto() {
        when(votoRepository.save(any(Voto.class))).thenReturn(VotoMapper.buildVoto(votarDTO));
        when(sessaoDeVotacaoService.buscarPorId(anyLong())).thenReturn(sessaoDeVotacao);
        when(associadoService.buscarPorId(anyLong())).thenReturn(associado);

        voto = votoService.votar(votarDTO);

        assertThat(voto.getStatus()).isEqualTo(votarDTO.status());
        assertThat(voto.getAssociado().getId()).isEqualTo(votarDTO.idAssociado());
        verify(sessaoDeVotacaoService).buscarPorId(anyLong());
        verify(associadoService).buscarPorId(anyLong());
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    public void deveRetornarNotFoundQuandoSessaoDeVotacaoNaoEncontradaAoVotar() {
        votarDTO = new VotarDTO(VotoStatusEnum.SIM, 99L, 1L);
        when(sessaoDeVotacaoService.buscarPorId(99L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> votoService.votar(votarDTO));
        verify(sessaoDeVotacaoService).buscarPorId(99L);
        verifyNoInteractions(votoRepository);
    }

    @Test
    public void deveRetornarNotFoundQuandoAssociadoNaoEncontradoAoVotar() {
        votarDTO = new VotarDTO(VotoStatusEnum.SIM, 1L, 99L);
        when(associadoService.buscarPorId(99L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> votoService.votar(votarDTO));
        verify(associadoService).buscarPorId(99L);
        verifyNoInteractions(votoRepository);
    }

    @Test
    public void deveRetornarBadRequestSeSessaoDeVotacaoJaEncerradaAoVotar() {
        sessaoDeVotacao.setStatus(SessaoDeVotacaoStatusEnum.ENCERRADA);
        when(sessaoDeVotacaoService.buscarPorId(1L)).thenReturn(sessaoDeVotacao);
        when(associadoService.buscarPorId(1L)).thenReturn(associado);

        assertThrows(BadRequestException.class, () -> votoService.votar(votarDTO));
        verify(sessaoDeVotacaoService).buscarPorId(1L);
        verify(associadoService).buscarPorId(1L);
        verifyNoInteractions(votoRepository);
    }

    @Test
    public void deveRetornarBadRequestAoVotarSeAssociadoJaVotouNessaSessao() {
        sessaoDeVotacao.getVotos().add(voto);
        when(sessaoDeVotacaoService.buscarPorId(1L)).thenReturn(sessaoDeVotacao);
        when(associadoService.buscarPorId(1L)).thenReturn(associado);

        assertThrows(BadRequestException.class, () -> votoService.votar(votarDTO));
        verify(sessaoDeVotacaoService).buscarPorId(1L);
        verify(associadoService).buscarPorId(1L);
        verifyNoInteractions(votoRepository);
    }

    @Test
    public void deveBuscarUmVotoPorId() {
        when(votoRepository.findById(1L)).thenReturn(Optional.of(voto));
        BuscarVotoDTO buscarVotoDTO = votoService.buscarVotoPorId(1L);

        assertThat(voto.getId()).isEqualTo(buscarVotoDTO.id());
        verify(votoRepository).findById(1L);
    }

    @Test
    public void deveLancarNotFoundAoBuscarVotoPorIdInexistente() {
        when(votoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> votoService.buscarVotoPorId(99L));
        verify(votoRepository).findById(99L);
    }

    @Test
    public void devebuscarTodosOsVotos() {
        List<Voto> votos = new ArrayList<>();
        Voto voto2 = new Voto(2L, VotoStatusEnum.SIM, associado);
        votos.add(voto);
        votos.add(voto2);

        when(votoRepository.findAll()).thenReturn(votos);
        List<BuscarVotoDTO> buscarVotoDTOS = votoService.buscarTodosOsVotos();

        assertThat(buscarVotoDTOS.size()).isEqualTo(2);
        verify(votoRepository).findAll();
    }

    @Test
    public void buscarVotosPorSessaoDeVotacao() {
        sessaoDeVotacao.getVotos().add(voto);
        when(sessaoDeVotacaoService.buscarPorId(1L)).thenReturn(sessaoDeVotacao);

        List<BuscarVotoDTO> buscarVotoDTOS = votoService.buscarVotosPorSessaoDeVotacao(1L);

        assertThat(buscarVotoDTOS.size()).isEqualTo(1);
        verify(sessaoDeVotacaoService).buscarPorId(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarVotosPorSessaoDeVotacaoInexistente() {
        when(sessaoDeVotacaoService.buscarPorId(99L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> votoService.buscarVotosPorSessaoDeVotacao(99L));
        verify(sessaoDeVotacaoService).buscarPorId(99L);
    }

}