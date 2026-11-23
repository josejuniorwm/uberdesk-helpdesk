package com.uberdesk.helpdesk.controller.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A HERANÇA DO JAVA (extends)
 * Quando dizemos "extends StandardError", estamos a avisar o Java:
 * "Olha, a classe ValidationError TEM TUDO o que a StandardError tem (timestamp, status, path...),
 * mas ela tem uma coisa extra que a StandardError não tem: uma LISTA DE ERROS."
 */
public class ValidationError extends StandardError {

    // 1. A Lista: Criamos uma lista vazia que vai guardar vários objetos do tipo "FieldMessage" (a classe acima)
    private List<FieldMessage> errors = new ArrayList<>();

    // 2. Construtor: O "super()" chama o construtor da classe mãe (StandardError) para prepará-la.
    public ValidationError() {
        super();
    }

    /**
     * CONSTRUTOR COM ARGUMENTOS
     * O "super" pega nestes 5 dados e envia-os para o construtor da classe mãe (StandardError)
     * para que ela preencha os campos.
     */
    public ValidationError(LocalDateTime timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    // 3. Getter da Lista: É graças a este mét0do que a lista "errors" vai aparecer no JSON final do Postman
    public List<FieldMessage> getErrors() {
        return errors;
    }

    /**
     * 4. MÉT0DO FACILITADOR (addError)
     * Em vez de termos de criar um FieldMessage e depois adicioná-lo à lista manualmente sempre,
     * criamos este mét0do auxiliar. Ele recebe as duas Strings, monta a "caixinha" (new FieldMessage)
     * e já a adiciona na nossa lista de erros de uma só vez.
     */
    public void addError(String fieldName, String message) {
        this.errors.add(new FieldMessage(fieldName, message));
    }
}