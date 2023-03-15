package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.pautaDTO.BuscarPautaDTO;
import br.tec.db.votacao.dto.pautaDTO.CriarPautaDTO;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.service.PautaService;
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
@RequestMapping("/pautas")
@Tag(name = "Pautas")
public class PautaController {

    private final PautaService pautaService;

    @Autowired
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @Operation(summary = "Cria uma nova pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se a pauta for criada com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Se a assembleia já estiver encerrada ou os dados informados forem inválidos"),
            @ApiResponse(responseCode = "404", description = "Se a assembleia não for encontrada")
    })
    @PostMapping
    public ResponseEntity<Pauta> criarPauta(@RequestBody @Valid CriarPautaDTO criarPautaDTO) {
        return new ResponseEntity<>(pautaService.criarPauta(criarPautaDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca uma pauta por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se a pauta for encontrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404", description = "Se não for encontrada uma pauta com o id informado")

    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarPautaDTO> buscarPautaPorID(@PathVariable Long id) {
        return new ResponseEntity<>(pautaService.buscarPautaPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Busca todas as pautas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se as pautas forem encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Se nenhuma pauta for encontrada")
    })
    @GetMapping
    public ResponseEntity<List<BuscarPautaDTO>> buscarTodasAsPautas() {
        return new ResponseEntity<>(pautaService.buscarTodasAsPautas(), HttpStatus.OK);
    }

    @Operation(summary = "Busca todas as pautas de uma assembleia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se as pautas forem encontradas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se nenhuma pauta for encontrada para a assembleia informada")
    })
    @GetMapping("/assembleia/{id}")
    public ResponseEntity<List<BuscarPautaDTO>> buscarPautasPorAssembleia(@PathVariable Long id) {
        return new ResponseEntity<>(pautaService.buscarPautasPorAssembleia(id), HttpStatus.OK);
    }
}