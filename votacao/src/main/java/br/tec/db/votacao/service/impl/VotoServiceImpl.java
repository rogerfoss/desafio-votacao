package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.mapper.VotoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.repository.AssociadoRepository;
import br.tec.db.votacao.repository.SessaoDeVotacaoRepository;
import br.tec.db.votacao.repository.VotoRepository;
import br.tec.db.votacao.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepository;

    private final SessaoDeVotacaoRepository sessaoDeVotacaoRepository;

    private final AssociadoRepository associadoRepository;

    @Autowired
    public VotoServiceImpl(VotoRepository votoRepository, SessaoDeVotacaoRepository sessaoDeVotacaoRepository,
                           AssociadoRepository associadoRepository) {

        this.votoRepository = votoRepository;
        this.sessaoDeVotacaoRepository = sessaoDeVotacaoRepository;
        this.associadoRepository = associadoRepository;
    }

    @Override
    public Voto votar(VotarDTO votarDTO) {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(votarDTO.idSessaoDeVotacao())
                .orElseThrow();

        Associado associado = associadoRepository.findById(votarDTO.idAssociado()).orElseThrow();
        if (sessaoDeVotacao.getStatus().equals(SessaoDeVotacaoStatusEnum.ENCERRADA)) {
            throw new RuntimeException("Sessão de votação encerrada");
        } else if (sessaoDeVotacao.getVotos().stream()
                .anyMatch(voto -> voto.getAssociado().getId().equals(associado.getId()))) {
            throw new RuntimeException("Associado já votou nesta sessão");
        } else {
            Voto voto = VotoMapper.buildVoto(votarDTO);
            sessaoDeVotacao.getVotos().add(voto);
            votoRepository.save(voto);
            return votoRepository.save(voto);
        }
    }

    @Override
    public BuscarVotoDTO buscarVotoPorId(Long id) {
        Voto voto = votoRepository.findById(id).orElseThrow(() -> new RuntimeException("Voto não encontrado"));
        return new BuscarVotoDTO(voto);
    }

    @Override
    public List<BuscarVotoDTO> buscarTodosOsVotos() {
        return votoRepository.findAll().stream().map(BuscarVotoDTO::new).toList();
    }

    @Override
    public List<BuscarVotoDTO> buscarVotosPorSessaoDeVotacao(Long id) {
        SessaoDeVotacao sessaoDeVotacao = sessaoDeVotacaoRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Sessão de votação não encontrada"));
        return sessaoDeVotacao.getVotos().stream().map(BuscarVotoDTO::new).collect(Collectors.toList());
    }
}
