package br.tec.db.votacao.dto.associadoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record CriarAssociadoDTO(
        @NotBlank(message = "Nome precisa ser informado")
        String nome,
        @NotNull(message = "CPF precisa ser informado")
        @CPF(message = "CPF inválido")
        String cpf
) {

}