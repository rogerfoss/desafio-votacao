package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.repository.PautaRepository;
import br.tec.db.votacao.repository.SessaoDeVotacaoRepository;
import br.tec.db.votacao.service.impl.SessaoDeVotacaoServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoDeVotacaoServiceImplTest {

    @Mock
    private SessaoDeVotacaoRepository sessaoDeVotacaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoDeVotacaoServiceImpl sessaoDeVotacaoService;

    @Disabled
    @Test
    public void criarSessaoDeVotacaoTest() {
        CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO = new CriarSessaoDeVotacaoDTO(LocalDateTime.now(), 1L);
        Pauta pauta = new Pauta(1L, "Pauta 1", PautaStatusEnum.AGUARDANDO_VOTACAO, null, null);

        when(this.sessaoDeVotacaoRepository.save(Mockito.any(SessaoDeVotacao.class))).thenReturn(new SessaoDeVotacao(1L, LocalDateTime.now(), null, SessaoDeVotacaoStatusEnum.INICIADA, pauta, null));

        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDTO);

        assertNotNull(sessaoDeVotacao);
        assertEquals(SessaoDeVotacaoStatusEnum.INICIADA, sessaoDeVotacao.getStatus());
    }

}