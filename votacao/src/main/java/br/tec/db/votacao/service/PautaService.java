package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.model.Pauta;

import java.util.List;

public interface PautaService {
    Pauta criarPauta(CriarPautaDTO criarPautaDTO) throws RuntimeException;

    BuscarPautaDTO buscarPautaPorId(Long id) throws RuntimeException;

    List<BuscarPautaDTO> buscarTodasAsPautas() throws RuntimeException;

    List<BuscarPautaDTO> buscarPautasPorAssembleia(Long id) throws RuntimeException;
}