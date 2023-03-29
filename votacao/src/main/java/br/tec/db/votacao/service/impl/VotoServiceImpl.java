package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.VotoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.repository.VotoRepository;
import br.tec.db.votacao.service.VotoService;
import br.tec.db.votacao.validation.ValidaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepository;

    private final SessaoDeVotacaoServiceImpl sessaoDeVotacaoService;

    private final AssociadoServiceImpl associadoService;

    @Autowired
    public VotoServiceImpl(VotoRepository votoRepository, SessaoDeVotacaoServiceImpl sessaoDeVotacaoService,
                           AssociadoServiceImpl associadoService) {

        this.votoRepository = votoRepository;
        this.sessaoDeVotacaoService = sessaoDeVotacaoService;
        this.associadoService = associadoService;
    }

    @Override
    public Voto votar(VotarDTO votarDTO) {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoService.buscarPorId(votarDTO.idSessaoDeVotacao());
        Associado associado = associadoService.buscarPorId(votarDTO.idAssociado());

        ValidaServices.validaStatusSessaoDeVotacao(sessaoDeVotacao);
        ValidaServices.validaAssociadoJaVotou(sessaoDeVotacao, associado);

        Voto voto = VotoMapper.buildVoto(votarDTO);
        sessaoDeVotacao.getVotos().add(voto);
        return votoRepository.save(voto);
    }

    @Override
    public BuscarVotoDTO buscarVotoPorId(Long id) {
        Voto voto = buscarPorId(id);
        return new BuscarVotoDTO(voto);
    }

    @Override
    public List<BuscarVotoDTO> buscarTodosOsVotos() {
        return votoRepository.findAll().stream().map(BuscarVotoDTO::new).toList();
    }

    @Override
    public List<BuscarVotoDTO> buscarVotosPorSessaoDeVotacao(Long id) {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoService.buscarPorId(id);
        return sessaoDeVotacao.getVotos().stream().map(BuscarVotoDTO::new).collect(Collectors.toList());
    }

    protected Voto buscarPorId(Long id) {
        return votoRepository.findById(id).orElseThrow(() -> new NotFoundException("Voto não encontrado"));
    }
}
