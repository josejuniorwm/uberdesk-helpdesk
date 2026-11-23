package com.uberdesk.helpdesk.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uberdesk.helpdesk.Chamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ChamadoDTO {
    // 1. O identificador único do chamado.
    private Long id;

    // 2. @JsonFormat: Garante que a data seja exibida e recebida no formato brasileiro.
    // O "LocalDate.now()" faz com que, ao instanciar o DTO, ele já pegue a data atual.
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAbertura = LocalDate.now();

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFechamento;

    // 3. @NotNull: Valida que a prioridade e o status não venham vazios no JSON.
    // Usamos Integer aqui para transitar o código numérico do Enum (ex: 0, 1, 2) pela rede.
    @NotNull(message = "O campo Prioridade é obrigadotorio")
    private Integer prioridade;

    @NotNull(message = "O campo titulo é obrigadorio")
    private String titulo;

    @NotNull(message = "O campo STATUS é obrigatório")
    private Integer status;

    // 4. @NotBlank: Impede que o título ou as observações venham nulos ou apenas com espaços.
    @NotBlank(message = "O campo observações é obrigatorio")
    private String observacoes;

    /* ====================================================================================
     * 5. O SEGREDO DOS RELACIONAMENTOS NO DTO
     * Em vez de colocar "private Usuario tecnico;" (a Entidade inteira), nós colocamos
     * apenas o ID e o Nome.
     * POR QUÊ?
     * Se trouxermos a Entidade, o Spring vai tentar converter o Técnico para JSON. Mas o Técnico
     * tem uma lista de Chamados, que por sua vez têm Técnicos, criando um "Loop Infinito"
     * (StackOverflowError). Trafegar apenas IDs resolve isso e deixa a API extremamente leve.
     * ==================================================================================== */
    @NotNull(message = "O campo técnico é obrigatório")
    private Long tecnico;
    private String nomeTecnico; // Opcional no envio, mas muito útil para o Frontend exibir na tela

    @NotNull(message = "O campo cliente é obrigatório")
    private Long cliente;
    private String nomeCliente;

    // 6. Construtor Vazio: Exigência do Jackson para desserializar o JSON.

    public ChamadoDTO(){
    }

    // 7. Construtor "Tradutor": Converte a Entidade Chamado (do banco) num ChamadoDTO (para a rede).

    public ChamadoDTO(Chamado obj) {

        this.id = obj.getId();
        this.dataAbertura = obj.getDataAbertura();
        this.dataFechamento = obj.getDataFechamento();
        this.prioridade = obj.getPrioridade().getCodigo();// Extrai apenas o número do Enum
        this.status = obj.getStatus().getCodigo(); // Extrai apenas o número do Enum
        this.titulo = obj.getTitulo();
        this.observacoes = obj.getObservacoes();

        // Aqui pegamos a entidade Usuario que está dentro do Chamado e extraímos só o que importa

        this.tecnico = obj.getTecnico().getId();
        this.nomeTecnico = obj.getTecnico().getNome();
        this.cliente = obj.getCliente().getId();
        this.nomeCliente = obj.getCliente().getNome();

    }

    /* ====================================================================================
     * 8. GETTERS E SETTERS
     * Essenciais para que o Spring consiga ler e injetar os dados neste "envelope".
     * ==================================================================================== */

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDate dataFechamento) { this.dataFechamento = dataFechamento; }

    public Integer getPrioridade() { return prioridade; }
    public void setPrioridade(Integer prioridade) { this.prioridade = prioridade; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Long getTecnico() { return tecnico; }
    public void setTecnico(Long tecnico) { this.tecnico = tecnico; }

    public String getNomeTecnico() { return nomeTecnico; }
    public void setNomeTecnico(String nomeTecnico) { this.nomeTecnico = nomeTecnico; }

    public Long getCliente() { return cliente; }
    public void setCliente(Long cliente) { this.cliente = cliente; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

}