package br.tec.db.votacao.handler;

import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.exception.dto.BadRequestExceptionDto;
import br.tec.db.votacao.exception.dto.NotFoundExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundExceptionDto> handlerNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(NotFoundExceptionDto.builder()
                .dataHora(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .titulo(HttpStatus.NOT_FOUND.getReasonPhrase())
                .mensagem(exception.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDto> handlerBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(BadRequestExceptionDto.builder()
                .dataHora(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .titulo(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .mensagem(exception.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

}
