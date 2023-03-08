package br.tec.db.votacao.dto.sessaoDeVotacaoDTO;

import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.model.SessaoDeVotacao;

import java.time.LocalDateTime;

public record BuscarSessaoDeVotacaoDTO(
        Long id,
        LocalDateTime inicio,
        LocalDateTime fim,
        SessaoDeVotacaoStatusEnum status,
        Long pautaId
) {
    public BuscarSessaoDeVotacaoDTO(SessaoDeVotacao sessaoDeVotacao) {
        this(sessaoDeVotacao.getId(), sessaoDeVotacao.getInicio(), sessaoDeVotacao.getFim(), sessaoDeVotacao.getStatus(), sessaoDeVotacao.getPauta().getId());
    }
}
