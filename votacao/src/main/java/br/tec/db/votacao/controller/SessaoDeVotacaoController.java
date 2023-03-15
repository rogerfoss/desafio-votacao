package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.BuscarSessaoDeVotacaoDTO;
import br.tec.db.votacao.dto.sessaoDeVotacaoDTO.CriarSessaoDeVotacaoDTO;
import br.tec.db.votacao.model.SessaoDeVotacao;
import br.tec.db.votacao.service.SessaoDeVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessao-de-votacao")
@Tag(name = "Sessão de Votação")
public class SessaoDeVotacaoController {

    private final SessaoDeVotacaoService sessaoDeVotacaoService;

    @Autowired
    public SessaoDeVotacaoController(SessaoDeVotacaoService sessaoDeVotacaoService) {
        this.sessaoDeVotacaoService = sessaoDeVotacaoService;
    }

    @Operation(summary = "Cria uma nova sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se a sessão de votação for criada com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Se a pauta não estiver aguardando votação ou os dados informados forem inválidos"),
            @ApiResponse(responseCode = "404", description = "Se a pauta não for encontrada")
    })
    @PostMapping
    public ResponseEntity<SessaoDeVotacao> criarSessaoDeVotacao(
            @RequestBody @Valid CriarSessaoDeVotacaoDTO criarSessaoDeVotacaoDto) {
        return new ResponseEntity<>(
                sessaoDeVotacaoService.criarSessaoDeVotacao(criarSessaoDeVotacaoDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todas as sessões de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se as sessões de votação forem encontradas"),
            @ApiResponse(responseCode = "404", description = "Se nenhuma sessão de votação for encontrada")
    })
    @GetMapping
    public ResponseEntity<List<BuscarSessaoDeVotacaoDTO>> buscarTodasSessoesDeVotacao() {
        return new ResponseEntity<>(sessaoDeVotacaoService.buscarTodasAsSessoesDeVotacao(), HttpStatus.OK);
    }

    @Operation(summary = "Busca uma sessão de votação pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se a sessão de votação for encontrada"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se não for encontrada uma sessão de votação com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarSessaoDeVotacaoDTO> buscarSessaoDeVotacaoPorId(@PathVariable Long id) {
        return new ResponseEntity<>(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Busca uma sessão de votação pelo id da pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se as sessões de votação forem encontradas"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se nenhuma sessão de votação for encontrada na pauta, " +
                            "ou se a pauta não for encontrada")
    })
    @GetMapping("/pauta/{id}")
    public ResponseEntity<BuscarSessaoDeVotacaoDTO> buscarSessaoDeVotacaoPorPauta(@PathVariable Long id) {
        return new ResponseEntity<>(sessaoDeVotacaoService.buscarSessaoDeVotacaoPorPauta(id), HttpStatus.OK);
    }

    @Operation(summary = "Encerra uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se a sessão de votação for encerrada com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Se a sessão de votação já estiver encerrada ou o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se nenhuma sessão de votação for encontrada com o id informado")
    })
    @PutMapping("/encerrar/{id}")
    public ResponseEntity<SessaoDeVotacao> encerrarSessaoDeVotacao(@PathVariable Long id) {
        sessaoDeVotacaoService.encerrarSessaoDeVotacao(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Calcula o resultado de uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se o resultado da sessão de votação for calculado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se nenhuma sessão de votação for encontrada com o id informado")
    })
    @PutMapping("/resultado/{id}")
    public ResponseEntity<SessaoDeVotacao> calcularResultadoDaSessaoDeVotacao(@PathVariable Long id) {
        sessaoDeVotacaoService.calcularResultadoDaSessaoDeVotacao(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}