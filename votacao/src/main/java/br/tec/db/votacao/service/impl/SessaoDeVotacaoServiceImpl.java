package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.enums.VotoStatusEnum;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.SessaoDeVotacaoMapper;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.repository.SessaoDeVotacaoRepository;
import br.tec.db.votacao.service.SessaoDeVotacaoService;
import br.tec.db.votacao.validation.ValidaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessaoDeVotacaoServiceImpl implements SessaoDeVotacaoService {

    private final SessaoDeVotacaoRepository sessaoDeVotacaoRepository;

    private final PautaServiceImpl pautaService;

    @Autowired
    public SessaoDeVotacaoServiceImpl(SessaoDeVotacaoRepository sessaoDeVotacaoRepository,
                                      PautaServiceImpl pautaService) {
        this.sessaoDeVotacaoRepository = sessaoDeVotacaoRepository;
        this.pautaService = pautaService;
    }

    @Override
    public SessaoDeVotacao criarSessaoDeVotacao(
            CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDTO) {

        Pauta pauta = pautaService.buscarPorId(criarSessaoDeVotacaoDTO.idPauta());
        ValidaServices.validaStatusPauta(pauta);

        SessaoDeVotacao sessaoDeVotacao = SessaoDeVotacaoMapper.buildSessaoDeVotacao(criarSessaoDeVotacaoDTO);
        pauta.setStatus(PautaStatusEnum.AGUARDANDO_VOTACAO);
        return sessaoDeVotacaoRepository.save(sessaoDeVotacao);
    }

    @Override
    public BuscarSessaoDeVotacaoDTO buscarSessaoDeVotacaoPorId(Long id) {
        SessaoDeVotacao sessaoDeVotacao = buscarPorId(id);
        return new BuscarSessaoDeVotacaoDTO(sessaoDeVotacao);
    }

    @Override
    public List<BuscarSessaoDeVotacaoDTO> buscarTodasAsSessoesDeVotacao() {
        return sessaoDeVotacaoRepository.findAll().stream().map(BuscarSessaoDeVotacaoDTO::new).toList();
    }

    @Override
    public void encerrarSessaoDeVotacao(Long id) {
        SessaoDeVotacao sessaoDeVotacao = buscarPorId(id);
        ValidaServices.validaStatusSessaoDeVotacao(sessaoDeVotacao);

        sessaoDeVotacao.setFim(LocalDateTime.now());
        sessaoDeVotacao.setStatus(SessaoDeVotacaoStatusEnum.ENCERRADA);
        sessaoDeVotacaoRepository.save(sessaoDeVotacao);
    }

    @Override
    public void calcularResultadoDaSessaoDeVotacao(Long id) {
        SessaoDeVotacao sessaoDeVotacao = buscarPorId(id);

        long votosSim = sessaoDeVotacao.getVotos().stream()
                .filter(voto -> voto.getStatus().equals(VotoStatusEnum.SIM)).count();

        long votosNao = sessaoDeVotacao.getVotos().stream()
                .filter(voto -> voto.getStatus().equals(VotoStatusEnum.NAO)).count();

        sessaoDeVotacao.getPauta().setStatus(
                votosSim > votosNao
                        ? PautaStatusEnum.APROVADA
                        : PautaStatusEnum.REPROVADA);
        pautaService.salvar(sessaoDeVotacao.getPauta());
    }

    protected SessaoDeVotacao buscarPorId(Long id) {
        return sessaoDeVotacaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sessão de votação não encontrada"));
    }

}
