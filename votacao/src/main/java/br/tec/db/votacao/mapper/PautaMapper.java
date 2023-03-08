package br.tec.db.votacao.mapper;

import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Pauta;

public interface PautaMapper {
    static Pauta buildPauta(CriarPautaDTO criarPautaDTO) {
        return Pauta.builder()
                .titulo(criarPautaDTO.titulo())
                .assembleia(Assembleia.builder().id(criarPautaDTO.idAssembleia()).build())
                .status(PautaStatusEnum.AGUARDANDO_VOTACAO)
                .build();
    }
}
