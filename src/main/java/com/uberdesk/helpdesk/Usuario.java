package com.uberdesk.helpdesk; // 1. O 'package' define a localização lógica do arquivo dentro do projeto.

import jakarta.persistence.*; // 2. Importa todas as anotações (*) do JPA necessárias para mapear esta classe para o banco de dados.

/**
 * 3. A anotação @Entity avisa ao Spring Boot e ao Hibernate o seguinte:
 * "Esta não é uma classe Java comum. É uma Entidade. Eu quero que você pegue
 * a estrutura desta classe e transforme em uma tabela no banco de dados."
 */
@Entity
/**
 * 4. A anotação @Table é opcional, mas uma boa prática.
 * Se não a usássemos, o Hibernate criaria a tabela com o nome exato da classe ("usuario").
 * Usando o name="usuarios", forçamos o nome da tabela no plural, seguindo o padrão de bancos de dados.
 */
@Table(name = "usuarios")
public class Usuario {

    /**
     * 5. @Id diz ao banco de dados que este atributo ('id') será a nossa
     * Chave Primária (Primary Key). É o identificador único de cada registro.
     */
    @Id
    /**
     * 6. @GeneratedValue define COMO esse ID será gerado.
     * A estratégia IDENTITY diz para o banco de dados (PostgreSQL, no nosso caso)
     * assumir a responsabilidade de criar esse número usando o recurso de Auto-Incremento (1, 2, 3...).
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 7. Como não colocamos nenhuma anotação aqui, o Hibernate vai assumir as
     * configurações padrão: vai criar uma coluna chamada "nome" do tipo VARCHAR(255).
     */
    private String nome;

    /**
     * 8. @Column permite customizar as regras da coluna no banco de dados.
     * unique = true: Diz ao banco para nunca aceitar dois cadastros com o mesmo e-mail.
     * nullable = false: Torna o preenchimento deste campo obrigatório no banco (NOT NULL).
     */
    @Column(unique = true, nullable = false)
    private String email;
    /**
     * 8.1. Armazena a senha já criptografada com BCrypt (nunca em texto puro).
     * Em cadastros futuros, lembre-se de passar o passwordEncoder.encode(...) antes de salvar.
     */
    @Column(nullable = false)
    private String senha;

    /**
     * 9. @Enumerated é essencial quando lidamos com Enums.
     * Se não usarmos isso, o banco vai salvar a POSIÇÃO do Enum (0 para ADMIN, 1 para TECNICO).
     * O EnumType.STRING obriga o banco a salvar a PALAVRA ("ADMIN", "CLIENTE").
     * Isso deixa o banco de dados muito mais fácil de ler se você precisar acessá-lo manualmente.
     */
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    /**
     * 10. Construtor Vazio (Default Constructor).
     * Por que ele está aqui sem fazer nada?
     * O Hibernate exige, por baixo dos panos, que toda Entidade tenha um construtor vazio.
     * Sem isso, quando ele buscar os dados no banco, não saberá como "instanciar" o objeto Usuario.
     */
    public Usuario() {
    }

    /* ====================================================================================
     * 11. GETTERS E SETTERS
     * Em Java, é uma boa prática de segurança deixar os atributos 'private' (Encapsulamento).
     * Como eles estão trancados, precisamos criar "portas de acesso" públicas para
     * ler os valores (Getters) e alterar os valores (Setters) desses atributos.
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}