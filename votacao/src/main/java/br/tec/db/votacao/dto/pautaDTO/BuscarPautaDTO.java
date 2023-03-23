package br.tec.db.votacao.dto.pautaDTO;

import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.model.Pauta;

public record BuscarPautaDTO(
        Long id,
        String titulo,
        PautaStatusEnum status
) {
    public BuscarPautaDTO(Pauta pauta) {
        this(pauta.getId(), pauta.getTitulo(), pauta.getStatus());
    }
}
