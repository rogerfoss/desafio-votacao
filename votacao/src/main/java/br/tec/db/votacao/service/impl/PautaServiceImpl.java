package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.mapper.PautaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.repository.AssembleiaRepository;
import br.tec.db.votacao.repository.PautaRepository;
import br.tec.db.votacao.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    private final AssembleiaRepository assembleiaRepository;

    @Autowired
    public PautaServiceImpl(PautaRepository pautaRepository, AssembleiaRepository assembleiaRepository) {
        this.pautaRepository = pautaRepository;
        this.assembleiaRepository = assembleiaRepository;
    }

    @Override
    public Pauta criarPauta(CriarPautaDTO criarPautaDTO) throws RuntimeException {
        Assembleia assembleia = assembleiaRepository.findById(criarPautaDTO.idAssembleia()).orElseThrow();
        if (assembleia.getStatus().equals(AssembleiaStatusEnum.INICIADA)) {
            Pauta pauta = PautaMapper.buildPauta(criarPautaDTO);
            assembleia.getPautas().add(pauta);
            return pautaRepository.save(pauta);
        } else {
            throw new RuntimeException("Não foi possível criar a pauta, assembleia já encerrada.");
        }
    }

    @Override
    public BuscarPautaDTO buscarPautaPorId(Long id) throws RuntimeException {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new RuntimeException("Pauta não encontrada"));
        return new BuscarPautaDTO(pauta);
    }

    @Override
    public List<BuscarPautaDTO> buscarTodasAsPautas() throws RuntimeException {
        return pautaRepository.findAll().stream().map(BuscarPautaDTO::new).toList();
    }

    @Override
    public List<BuscarPautaDTO> buscarPautasPorAssembleia(Long id) throws RuntimeException {
        Assembleia assembleia = assembleiaRepository.findById(id).orElseThrow(() -> new RuntimeException("Assembleia não encontrada"));
        return assembleia.getPautas().stream().map(BuscarPautaDTO::new).toList();
    }
}
