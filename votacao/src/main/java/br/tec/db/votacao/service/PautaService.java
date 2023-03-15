package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.model.Pauta;

import java.util.List;

public interface PautaService {
    Pauta criarPauta(CriarPautaDTO criarPautaDTO);

    BuscarPautaDTO buscarPautaPorId(Long id);

    List<BuscarPautaDTO> buscarTodasAsPautas();

    List<BuscarPautaDTO> buscarPautasPorAssembleia(Long id);
}