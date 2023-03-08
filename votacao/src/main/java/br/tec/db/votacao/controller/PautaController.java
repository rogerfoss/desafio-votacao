package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    private final PautaService pautaService;

    @Autowired
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping
    public ResponseEntity<Pauta> criarPauta(@RequestBody CriarPautaDTO criarPautaDTO) {
        return criarPautaDTO == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(pautaService.criarPauta(criarPautaDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscarPautaDTO> buscarPautaPorID(@PathVariable Long id) {
        return pautaService.buscarPautaPorId(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(pautaService.buscarPautaPorId(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BuscarPautaDTO>> buscarTodasAsPautas() {
        return pautaService.buscarTodasAsPautas().isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(pautaService.buscarTodasAsPautas(), HttpStatus.OK);
    }

    @GetMapping("/assembleia/{id}")
    public ResponseEntity<List<BuscarPautaDTO>> buscarPautasPorAssembleia(@PathVariable Long id) {
        return pautaService.buscarPautasPorAssembleia(id).isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(pautaService.buscarPautasPorAssembleia(id), HttpStatus.OK);
    }
}