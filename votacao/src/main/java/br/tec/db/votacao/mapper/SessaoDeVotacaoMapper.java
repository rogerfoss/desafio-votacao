package br.tec.db.votacao.mapper;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;

public interface SessaoDeVotacaoMapper {
    static SessaoDeVotacao buildSessaoDeVotacao(CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO) {
        return SessaoDeVotacao.builder()
                .inicio(criarSessaoDeVotacaoDTO.inicio())
                .status(SessaoDeVotacaoStatusEnum.INICIADA)
                .pauta(Pauta.builder().id(criarSessaoDeVotacaoDTO.idPauta()).build())
                .build();
    }
}
