package br.tec.db.votacao.mapper;

import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.model.Pauta;

public interface PautaMapper {
    static Pauta buildPauta(CriarPautaDTO criarPautaDTO) {
        return Pauta.builder()
                .titulo(criarPautaDTO.titulo())
                .status(PautaStatusEnum.CRIADA)
                .build();
    }
}
