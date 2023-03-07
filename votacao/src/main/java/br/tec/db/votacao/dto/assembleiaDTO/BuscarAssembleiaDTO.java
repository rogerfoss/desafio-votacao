package br.tec.db.votacao.dto.assembleiaDTO;

import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.model.Assembleia;

import java.time.LocalDateTime;

public record BuscarAssembleiaDTO(
        Long id,
        LocalDateTime inicio,
        LocalDateTime fim,
        AssembleiaStatusEnum status
) {
    public BuscarAssembleiaDTO(Assembleia assembleia) {
        this(assembleia.getId(), assembleia.getInicio(), assembleia.getFim(), assembleia.getStatus());
    }
}
