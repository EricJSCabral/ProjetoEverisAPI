package com.everis.BancoAPI.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class Handler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ClienteExistente.class)
    public ResponseEntity<ErroFormulario> handle(ClienteExistente exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("CPF", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(ContaExistente.class)
    public ResponseEntity<ErroFormulario> handle(ContaExistente exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("Número", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(ClienteNaoEncontrado.class)
    public ResponseEntity<ErroFormulario> handle (ClienteNaoEncontrado exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("CPF", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(CodigoNaoEncontrado.class)
    public ResponseEntity<ErroFormulario> handle (CodigoNaoEncontrado exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("codigo", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ContaNaoEncontrada.class)
    public ResponseEntity<ErroFormulario> handle (ContaNaoEncontrada exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("Numero", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(SemRegistros.class)
    public ResponseEntity<ErroFormulario> handle (SemRegistros exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("Vazio", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(NaoHaSaldo.class)
    public ResponseEntity<ErroFormulario> handle (NaoHaSaldo exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("saldo", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ContaAtrelada.class)
    public ResponseEntity<ErroFormulario> handle (ContaAtrelada exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("codigo", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ValorNegativo.class)
    public ResponseEntity<ErroFormulario> handle (ValorNegativo exception, HttpServletRequest request){
        ErroFormulario erro = new ErroFormulario("valor", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroFormulario> handle(MethodArgumentNotValidException exception) {
        List<ErroFormulario> erros = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroFormulario erro = new ErroFormulario(e.getField(), mensagem);
            erros.add(erro);
        });

        return erros;
    }
}
