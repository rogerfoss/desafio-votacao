package br.tec.db.votacao.service;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.model.SessaoDeVotacao;

import java.util.List;

public interface SessaoDeVotacaoService {
    SessaoDeVotacao criarSessaoDeVotacao(CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO) throws RuntimeException;

    BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorId(Long id) throws RuntimeException;

    List<BuscarSessaoDeVotacaoDTO> buscarTodasAsSessoesDeVotacao() throws RuntimeException;

    BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorPauta(Long id) throws RuntimeException;

    void encerrarSessaoDeVotacao(Long id) throws RuntimeException;

    void calcularResultadoDaSessaoDeVotacao(Long id) throws RuntimeException;

}
