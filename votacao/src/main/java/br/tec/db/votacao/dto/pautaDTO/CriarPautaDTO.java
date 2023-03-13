package br.tec.db.votacao.dto.pautaDTO;

import jakarta.validation.constraints.NotBlank;

public record CriarPautaDTO(
        @NotBlank(message = "O título da pauta precisa ser informado.")
        String titulo,
        @NotBlank(message = "O ID da assembleia precisa ser informado.")
        Long idAssembleia
) {

}