package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.service.AssociadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associados")
@Tag(name = "Associados")
public class AssociadoController {

    private final AssociadoService associadoService;

    @Autowired
    public AssociadoController(AssociadoService associadoService) {
        this.associadoService = associadoService;
    }

    @Operation(summary = "Salva um novo associado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associado salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível salvar o associado")
    })
    @PostMapping
    public ResponseEntity<Associado> salvarAssociado(@RequestBody CriarAssociadoDTO criarAssociadoDTO) {
        return criarAssociadoDTO == null
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(associadoService.salvarAssociado(criarAssociadoDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca um associado por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associado encontrado com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar um associado com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarAssociadoDTO> buscarAssociadoPorID(@PathVariable Long id) {
        return associadoService.buscarAssociadoPorId(id) == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(associadoService.buscarAssociadoPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os associados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associados encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi possível encontrar nenhum associado")
    })
    @GetMapping
    public ResponseEntity<List<BuscarAssociadoDTO>> buscarTodosOsAssociados() {
        return associadoService.buscarTodosOsAssociados().isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(associadoService.buscarTodosOsAssociados(), HttpStatus.OK);
    }

}