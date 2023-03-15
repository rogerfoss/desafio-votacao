package br.tec.db.votacao.dto.votoDTO;

import br.tec.db.votacao.enums.VotoStatusEnum;
import jakarta.validation.constraints.NotNull;

public record VotarDTO(
        @NotNull(message = "O status do voto precisa ser informado (SIM/NAO).")
        VotoStatusEnum status,
        @NotNull(message = "O ID da sessão de votação precisa ser informado.")
        Long idSessaoDeVotacao,
        @NotNull(message = "O ID do associado precisa ser informado.")
        Long idAssociado
) {

}