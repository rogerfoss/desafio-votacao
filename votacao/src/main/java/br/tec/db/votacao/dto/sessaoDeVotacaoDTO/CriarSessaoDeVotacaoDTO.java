package br.tec.db.votacao.dto.sessaoDeVotacaoDTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarSessaoDeVotacaoDTO(
        @NotNull(message = "A data e hora de início da sessão de votação precisa ser informada.")
        LocalDateTime inicio,
        @NotNull(message = "O ID da pauta precisa ser informado.")
        Long idPauta
) {

}


