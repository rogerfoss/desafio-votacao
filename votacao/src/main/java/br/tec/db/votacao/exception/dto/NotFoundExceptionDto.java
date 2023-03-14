package br.tec.db.votacao.exception.dto;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Getter
@SuperBuilder
public class NotFoundExceptionDto {
    private String titulo;
    private int status;
    private String mensagem;
    private LocalDateTime dataHora;
}
