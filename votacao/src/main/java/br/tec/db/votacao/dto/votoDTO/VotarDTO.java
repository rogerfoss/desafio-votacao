package br.tec.db.votacao.dto.votoDTO;

import br.tec.db.votacao.enums.VotoStatusEnum;
import jakarta.validation.constraints.NotBlank;

public record VotarDTO(
        @NotBlank(message = "O status do voto precisa ser informado (SIM/NAO).")
        VotoStatusEnum status,
        @NotBlank(message = "O ID da sessão de votação precisa ser informado.")
        Long idSessaoDeVotacao,
        @NotBlank(message = "O ID do associado precisa ser informado.")
        Long idAssociado
) {

}