package br.tec.db.votacao.dto.associadoDTO;

import br.tec.db.votacao.enums.AssociadoStatusEnum;
import br.tec.db.votacao.model.Associado;

public record BuscarAssociadoDTO(
        Long id,
        String nome,
        String cpf,
        AssociadoStatusEnum status
) {
    public BuscarAssociadoDTO(Associado associado) {
        this(associado.getId(), associado.getNome(), associado.getCpf(), associado.getStatus());
    }
}
