package br.tec.db.votacao.exception.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MethodArgumentNotValidExceptionDto {
    private String titulo;
    private int status;
    private LocalDateTime dataHora;
    private final String campo;
    private final String mensagemCampo;
}
