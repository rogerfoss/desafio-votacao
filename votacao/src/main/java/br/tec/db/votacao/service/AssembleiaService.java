package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.model.Assembleia;

import java.util.List;

public interface AssembleiaService {
    Assembleia criarAssembleia(CriarAssembleiaDTO criarAssembleiaDTO);

    List<BuscarAssembleiaDTO> buscarTodasAssembleias();

    BuscarAssembleiaDTO buscarAssembleiaPorId(Long id);

    void finalizarAssembleia(Long assembleiaId);
}
