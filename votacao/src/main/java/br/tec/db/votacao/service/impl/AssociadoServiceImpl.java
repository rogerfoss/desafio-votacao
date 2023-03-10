package br.tec.db.votacao.service.impl;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
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
    public Associado salvarAssociado(CriarAssociadoDTO criarAssociadoDTO) throws RuntimeException {
        try {
            return associadoRepository.save(AssociadoMapper.buildAssociado(criarAssociadoDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar associado");
        }
    }

    @Override
    public BuscarAssociadoDTO buscarAssociadoPorId(Long id) throws RuntimeException {
        Associado associado = this.associadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Associado não encontrado"));

        return new BuscarAssociadoDTO(associado);
    }

    @Override
    public List<BuscarAssociadoDTO> buscarTodosOsAssociados() throws RuntimeException {
        return associadoRepository.findAll().stream().map(BuscarAssociadoDTO::new).toList();
    }

}