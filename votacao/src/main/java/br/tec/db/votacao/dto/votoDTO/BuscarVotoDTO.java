package br.tec.db.votacao.dto.votoDTO;

import br.tec.db.votacao.enums.VotoStatusEnum;
import br.tec.db.votacao.model.Voto;

public record BuscarVotoDTO(
        Long id,
        VotoStatusEnum status
) {
    public BuscarVotoDTO(Voto voto) {
        this(voto.getId(), voto.getStatus());
    }
}
