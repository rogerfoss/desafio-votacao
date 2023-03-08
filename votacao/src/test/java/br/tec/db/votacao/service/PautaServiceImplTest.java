package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.repository.AssembleiaRepository;
import br.tec.db.votacao.repository.PautaRepository;
import br.tec.db.votacao.service.impl.PautaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PautaServiceImplTest {

    private PautaServiceImpl pautaService;

    @Captor
    private ArgumentCaptor<Pauta> pautaArgumentCaptor;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AssembleiaRepository assembleiaRepository;

    @BeforeEach
    public void inicializar() {
        MockitoAnnotations.openMocks(this);
        pautaService = new PautaServiceImpl(pautaRepository, assembleiaRepository);
    }

    private List<Pauta> listaPautas() {
        List<Pauta> list = new ArrayList<>();

        list.add(new Pauta(1L, "Pauta 1", PautaStatusEnum.AGUARDANDO_VOTACAO, new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null), null));
        list.add(new Pauta(2L, "Pauta 2", PautaStatusEnum.AGUARDANDO_VOTACAO, new Assembleia(2L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null), null));
        list.add(new Pauta(3L, "Pauta 3", PautaStatusEnum.AGUARDANDO_VOTACAO, new Assembleia(3L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null), null));

        return list;
    }

    @Disabled
    @Test
    public void criarPautaTest() {
        CriarPautaDTO criarPautaDTO = new CriarPautaDTO("Pauta 1", 1L);
        Assembleia assembleia = new Assembleia(1L, LocalDateTime.now(), null, AssembleiaStatusEnum.INICIADA, null, null);

        Mockito.when(this.assembleiaRepository.findById(criarPautaDTO.idAssembleia())).thenReturn(Optional.of(assembleia));
        Mockito.when(this.pautaRepository.save(Mockito.any(Pauta.class))).thenReturn(new Pauta(1L, "Pauta 1", PautaStatusEnum.AGUARDANDO_VOTACAO, assembleia, null));

        Pauta pautaCriada = pautaService.criarPauta(criarPautaDTO);

        Mockito.verify(pautaRepository).save(pautaArgumentCaptor.capture());
        Pauta pautaCapturada = pautaArgumentCaptor.getValue();

        assertEquals(pautaCriada.getId(), pautaCapturada.getId());
        assertEquals(pautaCriada.getStatus(), pautaCapturada.getStatus());
        Mockito.verify(pautaRepository, Mockito.times(1)).save(pautaCapturada);
    }

}