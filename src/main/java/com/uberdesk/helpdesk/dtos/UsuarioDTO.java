package com.uberdesk.helpdesk.dtos; // 1. O pacote lógico onde os DTOs ficam isolados.

import com.uberdesk.helpdesk.Perfil;
import com.uberdesk.helpdesk.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 2. O QUE É UM DTO? (Data Transfer Object / Objeto de Transferência de Dados)
 * Repare que esta classe NÃO tem a anotação @Entity nem @Table.
 * Isso significa que o banco de dados (PostgreSQL e Hibernate) ignora esta classe completamente.
 * Ela serve apenas como um "entregador" ou "envelope" para carregar dados do Java
 * para a internet (Postman/Navegador), sem expor a verdadeira Entidade.
 */
public class UsuarioDTO {

    // 3. Atributos simples. Não usamos @Id ou @Column aqui porque isto não é uma tabela.
    private Long id;
    // @NotBlank: Garante que a String não venha nula e nem vazia (só com espaços).
    @NotBlank(message = "O campo NOME é obrigatório")
    private String nome;


    // Exige que não seja vazio E verifica se a String tem formato válido (ex: tem '@' e '.com')
    @NotBlank(message = "O campo email é obraigatorio")
    @Email(message = "Formato de email invalido")
    private String email;


    private String senha;

    // @NotNull: Como Perfil é um Enum (e não um texto/String), usamos NotNull em vez de NotBlank.
    @NotNull(message = " O campo perfil é obrigatorio")
    private Perfil perfil;


    /*
     * 4. POR QUE USAR DTO?
     * Se o usuário tivesse uma 'String senha' ou 'String cpf' na classe principal (@Entity),
     * nós simplesmente NÃO colocaríamos esses campos aqui no DTO.
     * Assim, quando o Spring converter este DTO para JSON, a senha nunca vai vazar para a internet.
     */

    /**
     * 5. Construtor Vazio.
     * O Spring (através de uma biblioteca chamada Jackson) precisa deste construtor
     * para conseguir transformar o formato JSON que vem da internet de volta em um objeto Java.
     */
    public UsuarioDTO() {
    }

    /**
     * 6. O CONSTRUTOR "TRADUTOR"
     * Este é o segredo do DTO. Ele recebe como parâmetro a Entidade original que veio
     * do banco de dados (o objeto 'Usuario' completo) e copia apenas os dados
     * que consideramos seguros para as variáveis do DTO.
     */
    public UsuarioDTO(Usuario obj) {
        this.id = obj.getId();         // Copia o ID do banco
        this.nome = obj.getNome();     // Copia o Nome
        this.email = obj.getEmail();   // Copia o E-mail
        this.perfil = obj.getPerfil(); // Copia o Perfil
        // A senha original ficaria para trás e estaria segura!
    }

    /* ====================================================================================
     * 7. GETTERS E SETTERS
     * Como este objeto vai ser convertido em JSON (texto) para ser enviado pela internet,
     * o Spring precisa dos Getters para ler os valores que estão aqui dentro.
     * ==================================================================================== */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {return senha;}

    public void setSenha(String senha) {this.senha = senha; }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }


}