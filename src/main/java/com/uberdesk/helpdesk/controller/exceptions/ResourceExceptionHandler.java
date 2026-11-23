package com.uberdesk.helpdesk.controller.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * @ControllerAdvice
 * Esta anotação é como colocar um "espião" ou "guarda de trânsito" em volta de todos os seus @RestController.
 * Se algum Controller (ex: UsuarioController) tentar devolver um erro, esta classe "sequestra" o erro,
 * formata-o de forma bonita, e só depois o deixa ir para a internet.
 */
@ControllerAdvice
public class ResourceExceptionHandler {

    /**
     * @ExceptionHandler(MethodArgumentNotValidException.class)
     * Este é o "gatilho". Ele avisa o Spring: "Se o erro que rebentou lá no Controller for
     * exatamente do tipo MethodArgumentNotValidException (que é o erro que o @Valid do DTO dispara),
     * então acione este mét0do aqui!"
     *
     * Parâmetros do mét0do:
     * 1. O 'ex' contém a lista feia e complexa de erros que o Spring gerou por padrão.
     * 2. O 'request' contém informações de quem fez a chamada (para pegarmos a URL que falhou).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // 1. INSTANCIAR A NOSSA CLASSE BONITA
        // Criamos o nosso ValidationError (que herda do StandardError) e preenchemos os campos padrão:
        // Data atual, Status 400 (Bad Request), o título do erro, a mensagem geral e a URL que o utilizador tentou acessar.
        ValidationError errors = new ValidationError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Erro na validação dos campos",
                request.getRequestURI()
        );

        /*
         * 2. O LOOPING (FOR) DE CONVERSÃO
         * O "ex.getBindingResult().getFieldErrors()" devolve uma lista cheia de dados inúteis gerados pelo Spring.
         * Então, fazemos um 'for' para percorrer cada erro (x) dessa lista complexa.
         *
         * Para cada erro, pegamos só o que importa (o nome do campo e a mensagem que fizemos no DTO)
         * e adicionamos na nossa lista limpa usando aquele método 'addError' que criamos no passo anterior.
         */
        for (FieldError x : ex.getBindingResult().getFieldErrors()) {
            errors.addError(x.getField(), x.getDefaultMessage());
        }

        // 3. DEVOLVER A RESPOSTA
        // Agora devolvemos o JSON para o Postman/Navegador, forçando o Status 400 e colocando no corpo (body)
        // o nosso objeto 'errors', que já está todo formatado e preenchido.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}