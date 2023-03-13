package br.tec.db.votacao.dto.sessaoDeVotacaoDTO;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CriarSessaoDeVotacaoDTO(
        @NotBlank(message = "A data e hora de início da sessão de votação precisa ser informada.")
        LocalDateTime inicio,
        @NotBlank(message = "O ID da pauta precisa ser informado.")
        Long idPauta
) {

}


