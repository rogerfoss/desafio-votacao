package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.mapper.AssembleiaMapper;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.repository.AssembleiaRepository;
import br.tec.db.votacao.service.AssembleiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public Assembleia criarAssembleia(CriarAssembleiaDTO criarAssembleiaDTO) throws RuntimeException {

        try {
            return assembleiaRepository.save(AssembleiaMapper.buildAssembleia(criarAssembleiaDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar assembleia");
        }

    }

    public List<BuscarAssembleiaDTO> buscarTodasAssembleias() throws DataAccessException {
        return assembleiaRepository.findAll().stream().map(BuscarAssembleiaDTO::new).toList();
    }

    @Override
    public BuscarAssembleiaDTO buscarAssembleiaPorId(Long AssembleiaId) throws RuntimeException {
        Assembleia assembleia = this.assembleiaRepository.findById(AssembleiaId).orElseThrow(() -> new RuntimeException("Assembleia não encontrada"));
        return new BuscarAssembleiaDTO(assembleia);
    }

    @Override
    public void finalizarAssembleia(Long assembleiaId) throws RuntimeException {
        Assembleia assembleia = assembleiaRepository.findById(assembleiaId).orElse(null);
        if (assembleia == null) {
            throw new RuntimeException("Assembleia não encontrada");
        } else if (assembleia.getStatus().equals(AssembleiaStatusEnum.ENCERRADA)) {
            throw new RuntimeException("Assembleia já finalizada");
        } else {
            assembleia.setFim(LocalDateTime.now());
            assembleia.setStatus(AssembleiaStatusEnum.ENCERRADA);
            assembleiaRepository.save(assembleia);
        }
    }
}