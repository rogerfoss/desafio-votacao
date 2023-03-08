package br.tec.db.votacao.dto.sessaoDeVotacaoDTO;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CriarSessaoDeVotacaoDTO(
        @NotBlank
        LocalDateTime inicio,
        @NotBlank
        Long idPauta
) {

}


