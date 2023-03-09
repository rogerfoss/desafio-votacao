package br.tec.db.votacao.dto.votoDTO;

import br.tec.db.votacao.enums.VotoStatusEnum;
import jakarta.validation.constraints.NotBlank;

public record VotarDTO(
        @NotBlank
        VotoStatusEnum status,
        @NotBlank
        Long idSessaoDeVotacao,
        @NotBlank
        Long idAssociado
) {

}