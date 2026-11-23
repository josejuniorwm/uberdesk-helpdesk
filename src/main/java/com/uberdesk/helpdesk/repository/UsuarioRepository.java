package com.uberdesk.helpdesk.repository; //1. Localização lógica do ficheiro.

import com.uberdesk.helpdesk.Usuario; // 2. Importa a nossa entidade.
import org.springframework.data.jpa.repository.JpaRepository; // 3. Importa o motor do Spring Data.

import java.util.Optional;

/**
 * 4. Isto é uma INTERFACE, não uma classe normal. Funciona como um "contrato".
 * O Spring Boot vai detetar esta interface e, por baixo dos panos, vai
 * fabricar uma classe real que sabe como falar com o PostgreSQL.
 *
 * 5. JpaRepository Usuario, Long>:
 * - 'Usuario' avisa ao Spring: "Este repositório vai gerir a tabela de usuários".
 * - 'Long' avisa ao Spring: "O tipo de dado da Chave Primária (ID) do usuário é Long".
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    /**
     * 6. Só de estendermos (extends) o JpaRepository, já ganhamos dezenas de
     * comandos prontos sem escrever uma única linha de código.
     * Exemplo do que já podemos usar na aplicação:
     * - save(usuario) -> Faz o INSERT ou UPDATE.
     * - findAll() -> Faz o SELECT * FROM usuarios.
     * - findById(1L) -> Faz o SELECT * FROM usuarios WHERE id = 1.
     * - deleteById(1L) -> Faz o DELETE FROM usuarios WHERE id = 1.
     */
}
