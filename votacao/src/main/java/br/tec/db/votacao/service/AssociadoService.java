package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.model.Associado;

import java.util.List;

public interface AssociadoService {

    Associado salvarAssociado(CriarAssociadoDTO criarAssociadoDTO) throws RuntimeException;

    BuscarAssociadoDTO buscarAssociadoPorId(Long id) throws RuntimeException;

    List<BuscarAssociadoDTO> buscarTodosOsAssociados() throws RuntimeException;

}
