package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.service.AssembleiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Cria uma nova assembleia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assembleia criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível criar a assembleia")
    })
    @PostMapping
    public ResponseEntity<Assembleia> criarAssembleia(@RequestBody CriarAssembleiaDTO criarAssembleiaDTO) {
        return criarAssembleiaDTO == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(assembleiaService.criarAssembleia(criarAssembleiaDTO), HttpStatus.CREATED);
    }

    @Operation(description = "Busca todas as assembleias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assembleias encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar nenhuma assembleia")
    })
    @GetMapping
    public ResponseEntity<List<BuscarAssembleiaDTO>> buscarTodasAssembleias() {
        return assembleiaService.buscarTodasAssembleias().isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(assembleiaService.buscarTodasAssembleias(), HttpStatus.OK);
    }

    @Operation(description = "Busca uma assembleia por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assembleia encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar uma assembleia com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarAssembleiaDTO> buscarAssembleiaPorId(@PathVariable Long id) {
        return assembleiaService.buscarAssembleiaPorId(id) == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(assembleiaService.buscarAssembleiaPorId(id), HttpStatus.OK);
    }

    @Operation(description = "Finaliza uma assembleia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assembleia finalizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "A assembleia já foi finalizada"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar uma assembleia com o id informado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Assembleia> finalizarAssembleia(@PathVariable Long id) {
        assembleiaService.finalizarAssembleia(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
