package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.enums.AssociadoStatusEnum;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.AssociadoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.repository.AssociadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceImplTest {

    private AssociadoServiceImpl associadoService;
    private CriarAssociadoDTO criarAssociadoDTO;
    private Associado associado;

    @Mock
    private AssociadoRepository associadoRepository;

    @BeforeEach
    public void inicializar() {
        associadoService = new AssociadoServiceImpl(associadoRepository);
        criarAssociadoDTO = new CriarAssociadoDTO("Joao da Silva", "12345678901");
        associado = new Associado();
        associado.setStatus(AssociadoStatusEnum.PODE_VOTAR);
    }

    @Test
    void deveSalvarUmAssociado() {
        when(associadoRepository.save(any(Associado.class)))
                .thenReturn(AssociadoMapper.buildAssociado(criarAssociadoDTO));

        Associado associado = associadoService.salvarAssociado(criarAssociadoDTO);

        assertThat(associado.getStatus()).isEqualTo(AssociadoStatusEnum.PODE_VOTAR);
        assertThat(associado.getNome()).isEqualTo(criarAssociadoDTO.nome());
        verify(associadoRepository).save(any(Associado.class));
    }

    @Test
    void deveBuscarAssociadoPorId() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
        BuscarAssociadoDTO buscarAssociadoDTO = associadoService.buscarAssociadoPorId(1L);

        assertThat(associado.getId()).isEqualTo(buscarAssociadoDTO.id());
        verify(associadoRepository).findById(1L);
    }

    @Test
    void deveLancarNotFoundAoBuscarAssociadoPorIdInexistente() {
        when(associadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> associadoService.buscarAssociadoPorId(99L));
        verify(associadoRepository).findById(99L);
    }

    @Test
    void deveBuscarTodosOsAssociados() {
        List<Associado> associados = new ArrayList<>();
        Associado associado2 = new Associado();
        associados.add(associado);
        associados.add(associado2);
        when(associadoRepository.findAll()).thenReturn(associados);
        List<BuscarAssociadoDTO> associadosDTO = associadoService.buscarTodosOsAssociados();

        assertThat(associadosDTO).hasSize(2);
        verify(associadoRepository).findAll();
    }
}

