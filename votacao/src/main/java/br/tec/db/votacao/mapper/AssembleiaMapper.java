package br.tec.db.votacao.mapper;

import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.model.Assembleia;

public interface AssembleiaMapper {

    static Assembleia buildAssembleia(CriarAssembleiaDTO criarAssembleiaDTO) {
        return Assembleia.builder()
                .inicio(criarAssembleiaDTO.inicio())
                .status(AssembleiaStatusEnum.INICIADA)
                .build();
    }

}
