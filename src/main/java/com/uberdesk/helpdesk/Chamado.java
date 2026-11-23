package com.uberdesk.helpdesk; // 1. O 'package' define a localização deste arquivo dentro da estrutura do projeto.

import jakarta.persistence.*; // 2. Importa todas as anotações do JPA (Jakarta Persistence API) usadas para mapear o banco de dados.
import java.time.LocalDate; // 3. Importa a classe moderna do Java para lidar com datas (sem horas).

/**
 * 4. @Entity avisa ao Spring e ao Hibernate que esta classe não é comum,
 * ela deve ser transformada numa tabela real no banco de dados.
 */
@Entity
/**
 * 5. @Table(name = "chamados") força o nome da tabela no plural no banco de dados.
 * Sem isso, a tabela se chamaria "chamado" (o nome exato da classe).
 */
@Table(name = "chamados")
public class Chamado {

    /**
     * 6. @Id informa que este campo é a Chave Primária.
     * É a "identidade" única de cada chamado (ex: Chamado número 1, número 2...).
     */
    @Id
    /**
     * 7. @GeneratedValue com a estratégia IDENTITY diz ao banco (PostgreSQL)
     * para gerar esse número automaticamente usando o recurso de Auto-Incremento.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 8. O tipo Long (inteiro longo) é ideal para IDs pois suporta números gigantescos.

    /**
     * 9. LocalDate.now() garante que, assim que um Chamado for criado no Java,
     * ele já receba a data atual do sistema automaticamente. O banco já vai salvar a data de hoje.
     */
    private LocalDate dataAbertura = LocalDate.now();

    // 10. Data em que o chamado for finalizado. Começa vazia (null).
    private LocalDate dataFechamento;

    // 11. Colunas de texto padrão. Como não tem anotações, viram VARCHAR(255) no banco de dados.
    private String titulo;
    private String observacoes;

    /**
     * 12. @Enumerated(EnumType.STRING) é vital para salvar Enums corretamente.
     * Ele manda o banco salvar o texto (ex: "ALTA", "BAIXA") em vez da posição numérica invisível (0, 1).
     */
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    /**
     * 13. O mesmo conceito acima, aplicado ao status do chamado ("ABERTO", "ENCERRADO").
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * 14. RELACIONAMENTOS (Chaves Estrangeiras - Foreign Keys)
     * @ManyToOne significa "Muitos para Um". A lógica de leitura é:
     * "MUITOS chamados podem ser atendidos por UM único técnico".
     */
    @ManyToOne
    /**
     * 15. @JoinColumn(name = "tecnico_id") avisa ao banco:
     * "Crie uma coluna chamada 'tecnico_id' nesta tabela de chamados.
     * Ela vai guardar apenas o ID do usuário, criando o vínculo entre as duas tabelas."
     */
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico; // 16. No Java usamos o objeto completo, mas o banco salva só o ID numérico dele.

    /**
     * 17. A mesma lógica acima, mas para o Cliente.
     * "MUITOS chamados podem ser abertos por UM único cliente".
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    /**
     * 18. Construtor Vazio (Default Constructor).
     * É obrigatório no JPA/Hibernate! Sem ele, o Hibernate não consegue "montar"
     * o objeto quando puxar os dados do banco de dados.
     */
    public Chamado() {
    }

    /* ====================================================================================
     * 19. GETTERS E SETTERS
     * Como os atributos são privados (private), precisamos de métodos públicos para
     * ler (Get) e modificar (Set) cada um deles. Isso se chama Encapsulamento.
     * ==================================================================================== */

    public Long getId() {
        return id;
    } // 20. Devolve o ID do chamado.

    public void setId(Long id) {
        this.id = id;
    } // 21. Altera o ID (geralmente só o Hibernate usa isso).

    public LocalDate getDataAbertura() {
        return dataAbertura;
    } // 22. Devolve a data de abertura.

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    } // 23. Altera a data de abertura.

    public LocalDate getDataFechamento() {
        return dataFechamento;
    } // 24. Devolve a data de fechamento.

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    } // 25. Altera a data de fechamento quando o técnico finalizar o serviço.

    public String getTitulo() {
        return titulo;
    } // 26. Devolve o título do problema.

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    } // 27. Altera o título.

    public String getObservacoes() {
        return observacoes;
    } // 28. Devolve as observações detalhadas (descrição do problema).

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    } // 29. Altera as observações.

    public Prioridade getPrioridade() {
        return prioridade;
    } // 30. Devolve a prioridade (ALTA, MEDIA, BAIXA).

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    } // 31. Altera a prioridade.

    public Status getStatus() {
        return status;
    } // 32. Devolve o status atual (ABERTO, ANDAMENTO, ENCERRADO).

    public void setStatus(Status status) {
        this.status = status;
    } // 33. Altera o status.

    public Usuario getTecnico() {
        return tecnico;
    } // 34. Devolve o objeto Usuario que é o técnico responsável.

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    } // 35. Atribui um técnico a este chamado.

    public Usuario getCliente() {
        return cliente;
    } // 36. Devolve o objeto Usuario que é o cliente dono do chamado.

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    } // 37. Atribui quem foi o cliente que abriu o problema.
}