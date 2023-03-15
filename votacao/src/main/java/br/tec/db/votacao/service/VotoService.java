package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.model.Voto;

import java.util.List;

public interface VotoService {
    Voto votar(VotarDTO votoDTO);

    BuscarVotoDTO buscarVotoPorId(Long id);

    List<BuscarVotoDTO> buscarTodosOsVotos();

    List<BuscarVotoDTO> buscarVotosPorSessaoDeVotacao(Long id);
}
