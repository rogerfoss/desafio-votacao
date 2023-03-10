package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.enums.AssociadoStatusEnum;
import br.tec.db.votacao.mapper.AssociadoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.repository.AssociadoRepository;
import br.tec.db.votacao.service.impl.AssociadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        assertNotNull(associado);
        assertEquals(associado.getStatus(), AssociadoStatusEnum.PODE_VOTAR);
    }

    @Test
    void deveLancarExcecaoAoCriarAssociadoComDTONull() {
        CriarAssociadoDTO criarAssociadoDTO = new CriarAssociadoDTO(null, null);
        when(associadoRepository.save(any(Associado.class))).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> associadoService.salvarAssociado(criarAssociadoDTO));
    }

    @Test
    void deveBuscarAssociadoPorId() {
        when(associadoRepository.findById(any(Long.class))).thenReturn(Optional.of(associado));
        BuscarAssociadoDTO buscarAssociadoDTO = associadoService.buscarAssociadoPorId(1L);
        assertNotNull(buscarAssociadoDTO);
        assertEquals(associado.getStatus(), AssociadoStatusEnum.PODE_VOTAR);
    }

    @Test
    void deveLancarExcecaoAoBuscarAssociadoPorIdSeNaoEncontrar() {
        when(associadoRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> associadoService.buscarAssociadoPorId(1L));
    }

    @Test
    void deveBuscarTodosOsAssociados() {
        List<Associado> associados = new ArrayList<>();
        Associado associado = new Associado();
        Associado associado2 = new Associado();
        associados.add(associado);
        associados.add(associado2);
        when(associadoRepository.findAll()).thenReturn(associados);
        List<BuscarAssociadoDTO> buscarAssociadoDTOS = associadoService.buscarTodosOsAssociados();
        assertNotNull(buscarAssociadoDTOS);
        assertEquals(buscarAssociadoDTOS.size(), 2);
    }
}

