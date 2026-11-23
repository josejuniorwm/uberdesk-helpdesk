package com.uberdesk.helpdesk;

public enum Prioridade {
    // 1. Em vez de apenas escrever BAIXA, associamos um número e um texto a ela.
    BAIXA(0, "BAIXA"),
    MEDIA(1, "MÉDIA"),
    ALTA(2, "ALTA");

    // 2. Variáveis internas do Enum
    private Integer codigo;
    private String descricao;

    // 3. Construtor do Enum (no Java, o construtor de Enum é sempre privado por padrão)
    Prioridade(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    // 4. O GETTER QUE ESTAVA A FALTAR!
    // É este mét0do que o ChamadoDTO vai chamar para pegar o número (0, 1 ou 2)
    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}