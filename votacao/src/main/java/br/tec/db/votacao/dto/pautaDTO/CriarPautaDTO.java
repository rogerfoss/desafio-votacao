package br.tec.db.votacao.dto.pautaDTO;

import jakarta.validation.constraints.NotBlank;

public record CriarPautaDTO(
        @NotBlank
        String titulo,
        @NotBlank
        Long idAssembleia
) {

}