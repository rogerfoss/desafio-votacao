package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.enums.VotoStatusEnum;
import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.SessaoDeVotacaoMapper;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.repository.PautaRepository;
import br.tec.db.votacao.repository.SessaoDeVotacaoRepository;
import br.tec.db.votacao.service.SessaoDeVotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessaoDeVotacaoServiceImpl implements SessaoDeVotacaoService {

    private final SessaoDeVotacaoRepository sessaoDeVotacaoRepository;

    private final PautaRepository pautaRepository;

    @Autowired
    public SessaoDeVotacaoServiceImpl(SessaoDeVotacaoRepository sessaoDeVotacaoRepository,
                                      PautaRepository pautaRepository) {
        this.sessaoDeVotacaoRepository = sessaoDeVotacaoRepository;
        this.pautaRepository = pautaRepository;
    }

    @Override
    public SessaoDeVotacao criarSessaoDeVotacao(
            CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO) {

        Pauta pauta = pautaRepository.findById(criarSessaoDeVotacaoDTO.idPauta())
                .orElseThrow(() -> new NotFoundException("Pauta não encontrada."));

        if (pauta.getStatus().equals(PautaStatusEnum.APROVADA) ||
                pauta.getStatus().equals(PautaStatusEnum.REPROVADA)) {
            throw new BadRequestException("Não foi possível criar a sessão de votação, pauta já definida.");
        } else if (pauta.getStatus().equals(PautaStatusEnum.AGUARDANDO_VOTACAO)) {
            throw new BadRequestException("Não foi possível criar a sessão de votação, " +
                    "pauta já possui sessão de votação.");
        } else {
            SessaoDeVotacao sessaoDeVotacao = SessaoDeVotacaoMapper.buildSessaoDeVotacao(criarSessaoDeVotacaoDTO);
            pauta.setStatus(PautaStatusEnum.AGUARDANDO_VOTACAO);
            return sessaoDeVotacaoRepository.save(sessaoDeVotacao);
        }
    }

    @Override
    public BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorId(Long id) {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão de votação não encontrada."));

        return new BuscarSessaoDeVotacaoDTO(sessaoDeVotacao);
    }

    @Override
    public List<BuscarSessaoDeVotacaoDTO> buscarTodasAsSessoesDeVotacao() {
        return sessaoDeVotacaoRepository.findAll().stream().map(BuscarSessaoDeVotacaoDTO::new).toList();
    }

    @Override
    public void encerrarSessaoDeVotacao(Long id) {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão de votação não encontrada."));

        if (sessaoDeVotacao.getStatus().equals(SessaoDeVotacaoStatusEnum.ENCERRADA)) {
            throw new BadRequestException("Sessão de votação já encerrada.");
        } else {
            sessaoDeVotacao.setFim(LocalDateTime.now());
            sessaoDeVotacao.setStatus(SessaoDeVotacaoStatusEnum.ENCERRADA);
            sessaoDeVotacaoRepository.save(sessaoDeVotacao);
        }
    }

    @Override
    public void calcularResultadoDaSessaoDeVotacao(Long id) {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão de votação não encontrada."));

        long votosSim = sessaoDeVotacao.getVotos().stream()
                .filter(voto -> voto.getStatus().equals(VotoStatusEnum.SIM)).count();

        long votosNao = sessaoDeVotacao.getVotos().stream()
                .filter(voto -> voto.getStatus().equals(VotoStatusEnum.NAO)).count();

        sessaoDeVotacao.getPauta().setStatus(
                votosSim > votosNao
                        ? PautaStatusEnum.APROVADA
                        : PautaStatusEnum.REPROVADA);
        pautaRepository.save(sessaoDeVotacao.getPauta());
    }

}
