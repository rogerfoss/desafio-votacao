package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
public class VotoController {

    private final VotoService votoService;

    @Autowired
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    public ResponseEntity<Voto> votar(@RequestBody VotarDTO votarDTO) {
        return votarDTO == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(votoService.votar(votarDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscarVotoDTO> buscarVotoPorId(@PathVariable Long id) {
        return id == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(votoService.buscarVotoPorId(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BuscarVotoDTO>> buscarTodosOsVotos() {
        return votoService.buscarTodosOsVotos().isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(votoService.buscarTodosOsVotos(), HttpStatus.OK);
    }

    @GetMapping("/sessao/{id}")
    public ResponseEntity<List<BuscarVotoDTO>> buscarVotosPorSessaoDeVotacao(@PathVariable Long id) {
        return votoService.buscarVotosPorSessaoDeVotacao(id).isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(votoService.buscarVotosPorSessaoDeVotacao(id), HttpStatus.OK);
    }
}