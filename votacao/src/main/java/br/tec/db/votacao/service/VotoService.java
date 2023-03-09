package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.model.Voto;

import java.util.List;

public interface VotoService {
    Voto votar(VotarDTO votoDTO) throws RuntimeException;

    BuscarVotoDTO buscarVotoPorId(Long id) throws RuntimeException;

    List<BuscarVotoDTO> buscarTodosOsVotos() throws RuntimeException;

    List<BuscarVotoDTO> buscarVotosPorSessaoDeVotacao(Long id) throws RuntimeException;
}
