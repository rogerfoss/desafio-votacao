package br.tec.db.votacao.exception.dto;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Getter
@SuperBuilder
public class MethodArgumentNotValidExceptionDto {
    private final String campo;
    private final String mensagemCampo;
    private String titulo;
    private int status;
    private LocalDateTime dataHora;
}
