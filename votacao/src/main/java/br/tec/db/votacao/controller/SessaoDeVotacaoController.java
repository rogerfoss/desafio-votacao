package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.service.SessaoDeVotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessao-de-votacao")
public class SessaoDeVotacaoController {

    private final SessaoDeVotacaoService sessaoDeVotacaoService;

    @Autowired
    public SessaoDeVotacaoController(SessaoDeVotacaoService sessaoDeVotacaoService) {
        this.sessaoDeVotacaoService = sessaoDeVotacaoService;
    }

    @PostMapping
    public ResponseEntity<SessaoDeVotacao> criarSessaoDeVotacao(@RequestBody CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDto) {
        return criarSessaoDeVotacaoDto == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BuscarSessaoDeVotacaoDTO>> buscarTodasSessoesDeVotacao() {
        return sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao().isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscarSessaoDeVotacaoDTO> buscarSessaoDeVotacaoPorId(@PathVariable Long id) {
        return sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(id), HttpStatus.OK);
    }

    @GetMapping("/pauta/{id}")
    public ResponseEntity<BuscarSessaoDeVotacaoDTO> buscarSessaoDeVotacaoPorPauta(@PathVariable Long id) {
        return sessaoDeVotacaoService.buscarSessaoDeVotacaoPorPauta(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorPauta(id), HttpStatus.OK);
    }

    @PutMapping("/encerrar/{id}")
    public ResponseEntity<SessaoDeVotacao> encerrarSessaoDeVotacao(@PathVariable Long id) {
        sessaoDeVotacaoService.encerrarSessaoDeVotacao(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/resultado/{id}")
    public ResponseEntity<SessaoDeVotacao> calcularResultadoDaSessaoDeVotacao(@PathVariable Long id) {
        sessaoDeVotacaoService.calcularResultadoDaSessaoDeVotacao(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}