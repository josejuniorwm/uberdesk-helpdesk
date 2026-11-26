// Define o pacote raiz onde a classe está localizada (o mesmo de Chamado e Usuario)
package com.uberdesk.helpdesk;

// Importa as anotações do JPA (Jakarta Persistence) para mapear a classe no banco de dados
import jakarta.persistence.*;
// Importa a classe para trabalhar com data e hora exatas (sem fuso horário)
import java.time.LocalDateTime;

// Diz ao Spring/Hibernate que esta classe é uma entidade (vai virar uma tabela no MySQL)
@Entity
// Define o nome exato da tabela que será criada no banco de dados
@Table(name = "interacoes")
public class Interacao {

    // Marca este atributo como a Chave Primária (Primary Key) da tabela
    @Id
    // Diz ao MySQL para gerar o ID automaticamente (AUTO_INCREMENT: 1, 2, 3...)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cria a coluna "mensagem": não pode ser nula (NOT NULL) e usa tipo TEXT no MySQL para aceitar textos longos
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    // Cria a coluna "data_hora": não pode ser nula e já inicia preenchida com a data/hora atual do servidor
    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    // Relacionamento N:1 -> Muitas interações pertencem a UM único Chamado
    // FetchType.LAZY: só carrega os dados completos do Chamado no banco se o código pedir explicitamente (melhor performance)
    @ManyToOne(fetch = FetchType.LAZY)
    // Cria a coluna de chave estrangeira "chamado_id" na tabela "interacoes" (obrigatória)
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;

    // Relacionamento N:1 -> Muitas interações podem ser escritas por UM único Usuario (autor)
    // FetchType.EAGER: já traz os dados do autor (nome, perfil) junto com a interação para exibir direto na tela
    @ManyToOne(fetch = FetchType.EAGER)
    // Cria a coluna de chave estrangeira "usuario_id" na tabela "interacoes" (obrigatória)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autor;

    // Construtor vazio: obrigatório pelo JPA/Hibernate para ele conseguir instanciar o objeto ao ler do banco
    public Interacao() {
    }

    // Construtor prático: facilita criar uma nova interação no código passando mensagem, chamado e autor
    public Interacao(String mensagem, Chamado chamado, Usuario autor) {
        this.mensagem = mensagem;
        this.chamado = chamado;
        this.autor = autor;
        this.dataHora = LocalDateTime.now(); // Garante o registro do momento exato da criação
    }

    // --- Getters e Setters: permitem que o Spring, Hibernate e Thymeleaf leiam e alterem os atributos privados ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Chamado getChamado() { return chamado; }
    public void setChamado(Chamado chamado) { this.chamado = chamado; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }
}