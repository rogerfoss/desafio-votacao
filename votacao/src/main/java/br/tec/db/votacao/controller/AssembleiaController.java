package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.service.AssembleiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assembleias")
public class AssembleiaController {

    private final AssembleiaService assembleiaService;

    @Autowired
    public AssembleiaController(AssembleiaService assembleiaService) {
        this.assembleiaService = assembleiaService;
    }

    @PostMapping
    public ResponseEntity<Assembleia> criarAssembleia(@RequestBody CriarAssembleiaDTO criarAssembleiaDTO) {
        return criarAssembleiaDTO == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(assembleiaService.criarAssembleia(criarAssembleiaDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscarAssembleiaDTO>> buscarTodasAssembleias() {
        return assembleiaService.buscarTodasAssembleias().isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(assembleiaService.buscarTodasAssembleias(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscarAssembleiaDTO> buscarAssembleiaPorId(@PathVariable Long id) {
        return assembleiaService.buscarAssembleiaPorId(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(assembleiaService.buscarAssembleiaPorId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assembleia> finalizarAssembleia(@PathVariable Long id) {
        assembleiaService.finalizarAssembleia(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
