package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Vota em uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível realizar o voto")
    })
    @PostMapping
    public ResponseEntity<Voto> votar(@RequestBody VotarDTO votarDTO) {
        return votarDTO == null
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(votoService.votar(votarDTO), HttpStatus.CREATED);
    }

    @Operation(description = "Busca um voto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar um voto com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarVotoDTO> buscarVotoPorId(@PathVariable Long id) {
        return id == null
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(votoService.buscarVotoPorId(id), HttpStatus.OK);
    }

    @Operation(description = "Busca todos os votos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Votos encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar nenhum voto")
    })
    @GetMapping
    public ResponseEntity<List<BuscarVotoDTO>> buscarTodosOsVotos() {
        return votoService.buscarTodosOsVotos().isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(votoService.buscarTodosOsVotos(), HttpStatus.OK);
    }

    @Operation(description = "Busca todos os votos de uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Votos encontrados com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar nenhum voto para a sessão de votação informada")
    })
    @GetMapping("/sessao/{id}")
    public ResponseEntity<List<BuscarVotoDTO>> buscarVotosPorSessaoDeVotacao(@PathVariable Long id) {
        return votoService.buscarVotosPorSessaoDeVotacao(id).isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(votoService.buscarVotosPorSessaoDeVotacao(id), HttpStatus.OK);
    }
}