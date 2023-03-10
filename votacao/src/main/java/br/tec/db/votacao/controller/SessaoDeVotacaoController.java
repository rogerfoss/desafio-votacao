package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.service.SessaoDeVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Cria uma nova sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão de votação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível criar a sessão de votação")
    })
    @PostMapping
    public ResponseEntity<SessaoDeVotacao> criarSessaoDeVotacao(@RequestBody CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDto) {
        return criarSessaoDeVotacaoDto == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDto), HttpStatus.CREATED);
    }

    @Operation(description = "Busca todas as sessões de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessões de votação encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar nenhuma sessão de votação")
    })
    @GetMapping
    public ResponseEntity<List<BuscarSessaoDeVotacaoDTO>> buscarTodasSessoesDeVotacao() {
        return sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao().isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao(), HttpStatus.OK);
    }

    @Operation(description = "Busca uma sessão de votação pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão de votação encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar uma sessão de votação com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarSessaoDeVotacaoDTO> buscarSessaoDeVotacaoPorId(@PathVariable Long id) {
        return sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(id), HttpStatus.OK);
    }

    @Operation(description = "Busca uma sessão de votação pelo id da pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão de votação encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar uma sessão de votação com o id da pauta informado")
    })
    @GetMapping("/pauta/{id}")
    public ResponseEntity<BuscarSessaoDeVotacaoDTO> buscarSessaoDeVotacaoPorPauta(@PathVariable Long id) {
        return sessaoDeVotacaoService.buscarSessaoDeVotacaoPorPauta(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorPauta(id), HttpStatus.OK);
    }

    @Operation(description = "Encerra uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão de votação encerrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "A sessão de votação já foi encerrada"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar uma sessão de votação com o id informado")
    })
    @PutMapping("/encerrar/{id}")
    public ResponseEntity<SessaoDeVotacao> encerrarSessaoDeVotacao(@PathVariable Long id) {
        sessaoDeVotacaoService.encerrarSessaoDeVotacao(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(description = "Calcula o resultado de uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado da sessão de votação calculado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar uma sessão de votação com o id informado")
    })
    @PutMapping("/resultado/{id}")
    public ResponseEntity<SessaoDeVotacao> calcularResultadoDaSessaoDeVotacao(@PathVariable Long id) {
        sessaoDeVotacaoService.calcularResultadoDaSessaoDeVotacao(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}