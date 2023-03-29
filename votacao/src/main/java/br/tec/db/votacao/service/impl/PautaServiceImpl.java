package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.PautaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.repository.PautaRepository;
import br.tec.db.votacao.service.PautaService;
import br.tec.db.votacao.validation.ValidaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    private final AssembleiaServiceImpl assembleiaService;

    @Autowired
    public PautaServiceImpl(PautaRepository pautaRepository, AssembleiaServiceImpl assembleiaService) {
        this.pautaRepository = pautaRepository;
        this.assembleiaService = assembleiaService;
    }

    @Override
    public Pauta criarPauta(CriarPautaDTO criarPautaDTO) {
        Assembleia assembleia = assembleiaService.buscarPorId(criarPautaDTO.idAssembleia());
        ValidaServices.validaStatusAssembleia(assembleia, "Não foi possível criar a pauta, assembleia já encerrada.");

        Pauta pauta = PautaMapper.buildPauta(criarPautaDTO);
        assembleia.getPautas().add(pauta);
        return salvar(pauta);
    }

    @Override
    public BuscarPautaDTO buscarPautaPorId(Long id) {
        Pauta pauta = buscarPorId(id);
        return new BuscarPautaDTO(pauta);
    }

    @Override
    public List<BuscarPautaDTO> buscarTodasAsPautas() {
        return pautaRepository.findAll().stream().map(BuscarPautaDTO::new).toList();
    }

    @Override
    public List<BuscarPautaDTO> buscarPautasPorAssembleia(Long id) {
        Assembleia assembleia = assembleiaService.buscarPorId(id);

        return assembleia.getPautas().stream().map(BuscarPautaDTO::new).toList();
    }

    protected Pauta buscarPorId(Long id) {
        return pautaRepository.findById(id).orElseThrow(() -> new NotFoundException("Pauta não encontrada"));
    }

    protected Pauta salvar(Pauta pauta) {
        return pautaRepository.save(pauta);
    }
}
