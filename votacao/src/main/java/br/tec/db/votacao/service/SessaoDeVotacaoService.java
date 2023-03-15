package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.model.SessaoDeVotacao;

import java.util.List;

public interface SessaoDeVotacaoService {
    SessaoDeVotacao criarSessaoDeVotacao(CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO);

    BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorId(Long id);

    List<BuscarSessaoDeVotacaoDTO> buscarTodasAsSessoesDeVotacao();

    BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorPauta(Long id);

    void encerrarSessaoDeVotacao(Long id);

    void calcularResultadoDaSessaoDeVotacao(Long id);

}
