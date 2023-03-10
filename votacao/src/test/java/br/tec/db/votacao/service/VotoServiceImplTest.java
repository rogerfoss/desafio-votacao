package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.enums.VotoStatusEnum;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.repository.SessaoDeVotacaoRepository;
import br.tec.db.votacao.repository.VotoRepository;
import br.tec.db.votacao.service.impl.VotoServiceImpl;
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
class VotoServiceImplTest {

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotoServiceImpl votoService;

    @Mock
    private SessaoDeVotacaoService sessaoDeVotacaoService;

    @Mock
    private SessaoDeVotacaoRepository sessaoDeVotacaoRepository;

    @Disabled
    @Test
    public void votarTest() {
        VotarDTO votarDTO = new VotarDTO(VotoStatusEnum.SIM, 1L, 1L);
        SessaoDeVotacao sessaoDeVotacao = new SessaoDeVotacao(1L, LocalDateTime.now(), null,
                SessaoDeVotacaoStatusEnum.INICIADA, new Pauta(), null);

        when(this.votoRepository.save(Mockito.any(Voto.class))).thenReturn(new Voto(1L, VotoStatusEnum.SIM,
                new SessaoDeVotacao(), null));

        Voto voto = votoService.votar(votarDTO);

        assertNotNull(voto);
        assertEquals(VotoStatusEnum.SIM, voto.getStatus());
    }

}