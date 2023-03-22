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
import br.tec.db.votacao.repository.AssociadoRepository;
import br.tec.db.votacao.repository.SessaoDeVotacaoRepository;
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
    private SessaoDeVotacaoRepository sessaoDeVotacaoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @BeforeEach
    public void inicializar() {
        votoService = new VotoServiceImpl(votoRepository, sessaoDeVotacaoRepository, associadoRepository);
        votarDTO = new VotarDTO(VotoStatusEnum.SIM, 1L, 1L);
        sessaoDeVotacao = new SessaoDeVotacao(
                1L, LocalDateTime.now(), null, SessaoDeVotacaoStatusEnum.INICIADA, null, new ArrayList<>());

        associado = new Associado(1L, "Joao da Silva", "52342310030", AssociadoStatusEnum.PODE_VOTAR);
        voto = new Voto(1L, VotoStatusEnum.SIM, sessaoDeVotacao, associado);
    }

    @Test
    public void deveSalvarUmNovoVoto() {
        when(votoRepository.save(any(Voto.class))).thenReturn(VotoMapper.buildVoto(votarDTO));
        when(sessaoDeVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoDeVotacao));
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(associado));

        voto = votoService.votar(votarDTO);

        assertThat(voto.getStatus()).isEqualTo(votarDTO.status());
        assertThat(voto.getSessaoDeVotacao().getId()).isEqualTo(votarDTO.idSessaoDeVotacao());
        assertThat(voto.getAssociado().getId()).isEqualTo(votarDTO.idAssociado());
        verify(sessaoDeVotacaoRepository).findById(anyLong());
        verify(associadoRepository).findById(anyLong());
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    public void deveRetornarNotFoundQuandoSessaoDeVotacaoNaoEncontradaAoVotar() {
        when(sessaoDeVotacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> votoService.votar(votarDTO));
        verify(sessaoDeVotacaoRepository).findById(anyLong());
        verifyNoInteractions(votoRepository);
    }

    @Test
    public void deveRetornarNotFoundQuandoAssociadoNaoEncontradoAoVotar() {
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(sessaoDeVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoDeVotacao));

        assertThrows(NotFoundException.class, () -> votoService.votar(votarDTO));
        verify(associadoRepository).findById(anyLong());
        verify(sessaoDeVotacaoRepository).findById(anyLong());
        verifyNoInteractions(votoRepository);
    }

    @Test
    public void deveRetornarBadRequestSeSessaoDeVotacaoJaEncerradaAoVotar() {
        sessaoDeVotacao.setStatus(SessaoDeVotacaoStatusEnum.ENCERRADA);
        when(sessaoDeVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoDeVotacao));
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

        assertThrows(BadRequestException.class, () -> votoService.votar(votarDTO));
        verify(sessaoDeVotacaoRepository).findById(1L);
        verify(associadoRepository).findById(1L);
        verifyNoInteractions(votoRepository);
    }

    @Test
    public void deveRetornarBadRequestAoVotarSeAssociadoJaVotouNessaSessao() {
        sessaoDeVotacao.getVotos().add(voto);
        when(sessaoDeVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoDeVotacao));
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

        assertThrows(BadRequestException.class, () -> votoService.votar(votarDTO));
        verify(sessaoDeVotacaoRepository).findById(1L);
        verify(associadoRepository).findById(1L);
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
        Voto voto2 = new Voto(2L, VotoStatusEnum.SIM, sessaoDeVotacao, associado);
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
        when(sessaoDeVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoDeVotacao));

        List<BuscarVotoDTO> buscarVotoDTOS = votoService.buscarVotosPorSessaoDeVotacao(1L);

        assertThat(buscarVotoDTOS.size()).isEqualTo(1);
        verify(sessaoDeVotacaoRepository).findById(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarVotosPorSessaoDeVotacaoInexistente() {
        when(sessaoDeVotacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> votoService.buscarVotosPorSessaoDeVotacao(99L));
        verify(sessaoDeVotacaoRepository).findById(99L);
    }

}