package br.tec.db.votacao.dto.associadoDTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CriarAssociadoDTO(
        @NotBlank(message = "Nome precisa ser informado")
        String nome,
        @CPF(message = "CPF inválido")
        String cpf
) {

}