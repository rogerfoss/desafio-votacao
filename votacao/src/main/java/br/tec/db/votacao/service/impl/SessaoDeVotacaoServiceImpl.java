package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.enums.VotoStatusEnum;
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
    public SessaoDeVotacaoServiceImpl(SessaoDeVotacaoRepository sessaoDeVotacaoRepository, PautaRepository pautaRepository) {
        this.sessaoDeVotacaoRepository = sessaoDeVotacaoRepository;
        this.pautaRepository = pautaRepository;
    }

    @Override
    public SessaoDeVotacao criarSessaoDeVotacao(CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO) throws RuntimeException {
        Pauta pauta = pautaRepository.findById(criarSessaoDeVotacaoDTO.idPauta()).orElseThrow();
        if (pauta.getStatus().equals(PautaStatusEnum.AGUARDANDO_VOTACAO)) {
            SessaoDeVotacao sessaoDeVotacao = SessaoDeVotacaoMapper.buildSessaoDeVotacao(criarSessaoDeVotacaoDTO);
            pauta.setSessaoDeVotacao(sessaoDeVotacao);
            return sessaoDeVotacaoRepository.save(sessaoDeVotacao);
        } else {
            throw new RuntimeException("Não foi possível criar a sessão de votação, pauta já encerrada.");
        }
    }

    @Override
    public BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorId(Long id) throws RuntimeException {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Sessão de votação não encontrada."));
        return new BuscarSessaoDeVotacaoDTO(sessaoDeVotacao);
    }

    @Override
    public List<BuscarSessaoDeVotacaoDTO> buscarTodasAsSessoesDeVotacao() throws RuntimeException {
        return sessaoDeVotacaoRepository.findAll().stream().map(BuscarSessaoDeVotacaoDTO::new).toList();
    }

    @Override
    public BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorPauta(Long id) throws RuntimeException {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new RuntimeException("Sem sessão na pauta ou pauta não encontrada."));
        return new BuscarSessaoDeVotacaoDTO(pauta.getSessaoDeVotacao());
    }

    @Override
    public void encerrarSessaoDeVotacao(Long id) throws RuntimeException {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Sessão de votação não encontrada."));
        if (sessaoDeVotacao.getStatus().equals(SessaoDeVotacaoStatusEnum.ENCERRADA)) {
            throw new RuntimeException("Sessão de votação já encerrada.");
        } else {
            sessaoDeVotacao.setFim(LocalDateTime.now());
            sessaoDeVotacao.setStatus(SessaoDeVotacaoStatusEnum.ENCERRADA);
            sessaoDeVotacaoRepository.save(sessaoDeVotacao);
        }
    }

    @Override
    public void calcularResultadoDaSessaoDeVotacao(Long id) throws RuntimeException {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Sessão de votação não encontrada."));
        long votosSim = sessaoDeVotacao.getVotos().stream().filter(voto -> voto.getStatus().equals(VotoStatusEnum.SIM)).count();
        long votosNao = sessaoDeVotacao.getVotos().stream().filter(voto -> voto.getStatus().equals(VotoStatusEnum.NAO)).count();
        sessaoDeVotacao.getPauta().setStatus(votosSim > votosNao ? PautaStatusEnum.APROVADA : PautaStatusEnum.REPROVADA);
        pautaRepository.save(sessaoDeVotacao.getPauta());
    }

}
