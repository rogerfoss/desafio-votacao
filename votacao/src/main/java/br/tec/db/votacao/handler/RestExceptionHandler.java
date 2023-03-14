package br.tec.db.votacao.handler;

import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.exception.NotFoundException;
import br.tec.db.votacao.exception.dto.BadRequestExceptionDto;
import br.tec.db.votacao.exception.dto.MethodArgumentNotValidExceptionDto;
import br.tec.db.votacao.exception.dto.NotFoundExceptionDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String campos = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String mensagemCampo = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                MethodArgumentNotValidExceptionDto.builder()
                        .dataHora(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .titulo("Bad Request Exception, Campos Inválidos")
                        .campo(campos)
                        .mensagemCampo(mensagemCampo)
                        .build(), HttpStatus.BAD_REQUEST);

    }

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
