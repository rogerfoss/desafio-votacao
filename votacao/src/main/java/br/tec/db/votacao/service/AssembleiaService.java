package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.model.Assembleia;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AssembleiaService {
    Assembleia criarAssembleia(CriarAssembleiaDTO criarAssembleiaDTO) throws RuntimeException;

    List<BuscarAssembleiaDTO> buscarTodasAssembleias() throws DataAccessException;

    BuscarAssembleiaDTO buscarAssembleiaPorId(Long id) throws RuntimeException;

    void finalizarAssembleia(Long assembleiaId) throws RuntimeException;
}
