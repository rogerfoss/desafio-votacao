package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.assembleiaDTO.BuscarAssembleiaDTO;
import br.tec.db.votacao.dto.assembleiaDTO.CriarAssembleiaDTO;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.service.AssembleiaService;
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
@RequestMapping("/assembleias")
@Tag(name = "Assembleia")
public class AssembleiaController {

    private final AssembleiaService assembleiaService;

    @Autowired
    public AssembleiaController(AssembleiaService assembleiaService) {
        this.assembleiaService = assembleiaService;
    }

    @Operation(summary = "Cria uma nova assembleia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se a assembleia for criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se os dados informados forem inválidos")
    })
    @PostMapping
    public ResponseEntity<Assembleia> criarAssembleia(@RequestBody @Valid CriarAssembleiaDTO criarAssembleiaDTO) {
        return new ResponseEntity<>(assembleiaService.criarAssembleia(criarAssembleiaDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todas as assembleias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se as assembleias forem encontradas com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<BuscarAssembleiaDTO>> buscarTodasAssembleias() {
        return new ResponseEntity<>(assembleiaService.buscarTodasAssembleias(), HttpStatus.OK);
    }

    @Operation(summary = "Busca uma assembleia por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se a assembleia for encontrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se não for encontrada uma assembleia com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarAssembleiaDTO> buscarAssembleiaPorId(@PathVariable Long id) {
        return new ResponseEntity<>(assembleiaService.buscarAssembleiaPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Finaliza uma assembleia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se a assembleia for finalizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se a assembleia já estiver finalizada " +
                    "ou se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se não for encontrada uma assembleia com o id informado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Assembleia> finalizarAssembleia(@PathVariable Long id) {
        assembleiaService.finalizarAssembleia(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
