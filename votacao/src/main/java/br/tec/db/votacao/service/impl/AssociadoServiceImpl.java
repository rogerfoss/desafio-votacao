package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.mapper.AssociadoMapper;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.repository.AssociadoRepository;
import br.tec.db.votacao.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository associadoRepository;

    @Autowired
    public AssociadoServiceImpl(AssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    @Override
    public Associado salvarAssociado(CriarAssociadoDTO criarAssociadoDTO) {
        return associadoRepository.save(AssociadoMapper.buildAssociado(criarAssociadoDTO));
    }

    @Override
    public BuscarAssociadoDTO buscarAssociadoPorId(Long id) {
        Associado associado = buscarPorId(id);
        return new BuscarAssociadoDTO(associado);
    }

    @Override
    public List<BuscarAssociadoDTO> buscarTodosOsAssociados() {
        return associadoRepository.findAll().stream().map(BuscarAssociadoDTO::new).toList();
    }

    protected Associado buscarPorId(Long id) {
        return associadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Associado não encontrado"));
    }

}