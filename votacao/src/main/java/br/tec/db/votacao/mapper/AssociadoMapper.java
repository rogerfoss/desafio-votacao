package br.tec.db.votacao.mapper;

import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.enums.AssociadoStatusEnum;
import br.tec.db.votacao.model.Associado;

public interface AssociadoMapper {

    static Associado buildAssociado(CriarAssociadoDTO criarAssociadoDTO) {
        return Associado.builder()
                .nome(criarAssociadoDTO.nome())
                .cpf(criarAssociadoDTO.cpf())
                .status(AssociadoStatusEnum.PODE_VOTAR)
                .build();
    }
}
