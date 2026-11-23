package com.uberdesk.helpdesk.controller.exceptions;

/**
 * O propósito desta classe é muito simples: representar UM único erro de formulário.
 * Como um usuário pode errar vários campos ao mesmo tempo (ex: nome vazio E email errado),
 * nós precisaremos criar várias "caixinhas" destas para enviar na resposta.
 */
public class FieldMessage {

    // 1. fieldName: Vai guardar qual foi o campo do JSON que o utilizador errou. Ex: "email"
    private String fieldName;

    // 2. message: Vai guardar o texto que configuramos lá no DTO. Ex: "Formato de e-mail inválido"
    private String message;

    // 3. Construtor Vazio: Exigência do framework (Jackson) para conseguir criar o objeto na memória
    public FieldMessage() {
    }

    // 4. Construtor com Argumentos: Facilita a nossa vida para criar o objeto já preenchido numa única linha
    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    /*
     * 5. GETTERS: Essenciais para o Spring!
     * Quando o Spring for transformar este objeto em texto JSON para devolver no Postman,
     * ele vai chamar internamente o getFieldName() e o getMessage() para extrair os valores.
     * Se esquecermos os getters, o JSON volta vazio!
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}