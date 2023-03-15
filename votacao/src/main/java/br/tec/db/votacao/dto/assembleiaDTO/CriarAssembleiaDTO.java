package br.tec.db.votacao.dto.assembleiaDTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarAssembleiaDTO(
        @NotNull(message = "O início da assembleia precisa ser informado.")
        LocalDateTime inicio
) {

}