package br.tec.db.votacao.controller;

import br.tec.db.votacao.dto.associadoDTO.BuscarAssociadoDTO;
import br.tec.db.votacao.dto.associadoDTO.CriarAssociadoDTO;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.service.AssociadoService;
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
            @ApiResponse(responseCode = "201", description = "Se o associado foi salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se os dados informados forem inválidos")
    })
    @PostMapping
    public ResponseEntity<Associado> salvarAssociado(@RequestBody @Valid CriarAssociadoDTO criarAssociadoDTO) {
        return new ResponseEntity<>(associadoService.salvarAssociado(criarAssociadoDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Busca um associado por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se o associado foi encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se o id informado for inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Se não for encontrado um associado com o id informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BuscarAssociadoDTO> buscarAssociadoPorID(@PathVariable Long id) {
        return new ResponseEntity<>(associadoService.buscarAssociadoPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os associados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se os associados foram encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Se nenhum associado for encontrado")
    })
    @GetMapping
    public ResponseEntity<List<BuscarAssociadoDTO>> buscarTodosOsAssociados() {
        return new ResponseEntity<>(associadoService.buscarTodosOsAssociados(), HttpStatus.OK);
    }

}