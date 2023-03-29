package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.AssembleiaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.repository.AssembleiaRepository;
import br.tec.db.votacao.service.AssembleiaService;
import br.tec.db.votacao.validation.ValidaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssembleiaServiceImpl implements AssembleiaService {

    private final AssembleiaRepository assembleiaRepository;

    @Autowired
    public AssembleiaServiceImpl(AssembleiaRepository assembleiaRepository) {
        this.assembleiaRepository = assembleiaRepository;
    }

    @Override
    public Assembleia criarAssembleia(CriarAssembleiaDTO criarAssembleiaDTO) {
        return assembleiaRepository.save(AssembleiaMapper.buildAssembleia(criarAssembleiaDTO));
    }

    public List<BuscarAssembleiaDTO> buscarTodasAssembleias() {
        return assembleiaRepository.findAll().stream().map(BuscarAssembleiaDTO::new).toList();
    }

    @Override
    public BuscarAssembleiaDTO buscarAssembleiaPorId(Long AssembleiaId) {
        Assembleia assembleia = buscarPorId(AssembleiaId);

        return new BuscarAssembleiaDTO(assembleia);
    }

    @Override
    public void finalizarAssembleia(Long assembleiaId) {
        Assembleia assembleia = buscarPorId(assembleiaId);
        ValidaServices.validaStatusAssembleia(assembleia, "Assembleia já finalizada");

        assembleia.setFim(LocalDateTime.now());
        assembleia.setStatus(AssembleiaStatusEnum.ENCERRADA);
        assembleiaRepository.save(assembleia);
    }

    protected Assembleia buscarPorId(Long id) {
        return assembleiaRepository.findById(id).orElseThrow(() -> new NotFoundException("Assembleia não encontrada"));
    }


}