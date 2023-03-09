package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associados")
public class AssociadoController {

    private final AssociadoService associadoService;

    @Autowired
    public AssociadoController(AssociadoService associadoService) {
        this.associadoService = associadoService;
    }

    @PostMapping
    public ResponseEntity<Associado> salvarAssociado(@RequestBody CriarAssociadoDTO criarAssociadoDTO) {
        return criarAssociadoDTO == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(associadoService.salvarAssociado(criarAssociadoDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscarAssociadoDTO> buscarAssociadoPorID(@PathVariable Long id) {
        return associadoService.buscarAssociadoPorId(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(associadoService.buscarAssociadoPorId(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BuscarAssociadoDTO>> buscarTodosOsAssociados() {
        return associadoService.buscarTodosOsAssociados().isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(associadoService.buscarTodosOsAssociados(), HttpStatus.OK);
    }

}