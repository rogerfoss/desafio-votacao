package br.tec.db.votacao.mapper;

import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.model.Voto;

public interface VotoMapper {
    static Voto buildVoto(VotarDTO votarDTO) {
        return Voto.builder()
                .status(votarDTO.status())
                .associado(Associado.builder().id(votarDTO.idAssociado()).build())
                .build();
    }
}
