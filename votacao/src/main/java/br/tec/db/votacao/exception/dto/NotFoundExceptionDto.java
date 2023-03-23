package br.tec.db.votacao.exception.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotFoundExceptionDto {
    private String titulo;
    private int status;
    private String mensagem;
    private LocalDateTime dataHora;
}
