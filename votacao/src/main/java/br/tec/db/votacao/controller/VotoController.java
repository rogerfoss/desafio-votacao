package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.votoDTO.BuscarVotoDTO;
import br.tec.db.votacao.dto.votoDTO.VotarDTO;
import br.tec.db.votacao.model.Voto;
import br.tec.db.votacao.service.VotoService;
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
@RequestMapping("/votos")
@Tag(name = "Votos")
public class VotoController {

    private final VotoService votoService;

    @Autowired
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @Operation(summary = "Vota em uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se o voto for computado com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Se a sessão de votação estiver encerrada, " +
                            "se o associado já tiver votado ou se os dados informados forem inválidos"),
            @ApiResponse(responseCode = "404",
                    description = "Se a sessão de votação ou o associado não forem encontrados")
    })
    @PostMapping
    public ResponseEntity<Voto> votar(@RequestBody @Valid VotarDTO votarDTO) {
        return new ResponseEntity<>(votoService.votar(votarDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca um voto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se o voto for encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404", description = "Se não for encontrado um voto com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarVotoDTO> buscarVotoPorId(@PathVariable Long id) {
        return new ResponseEntity<>(votoService.buscarVotoPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os votos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se os votos forem encontrados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<BuscarVotoDTO>> buscarTodosOsVotos() {
        return new ResponseEntity<>(votoService.buscarTodosOsVotos(), HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os votos de uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se os votos forem encontrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se não for encontrada uma sessão de votação com o id informado")
    })
    @GetMapping("/sessao/{id}")
    public ResponseEntity<List<BuscarVotoDTO>> buscarVotosPorSessaoDeVotacao(@PathVariable Long id) {
        return new ResponseEntity<>(votoService.buscarVotosPorSessaoDeVotacao(id), HttpStatus.OK);
    }
}