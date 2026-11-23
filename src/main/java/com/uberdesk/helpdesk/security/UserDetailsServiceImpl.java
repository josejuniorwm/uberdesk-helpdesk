package com.uberdesk.helpdesk.security;

import com.uberdesk.helpdesk.Usuario;
import com.uberdesk.helpdesk.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
    /**
     * /@Service: Avisa ao Spring para gerenciar esta classe automaticamente.
     *
     * implements UserDetailsService: É a interface oficial do Spring Security.
     * Quando o usuário digita e-mail e senha na tela de login e clica em "Entrar",
     * o Spring Security procura automaticamente uma classe que implemente esta interface
     * e chama o mét0do loadUserByUsername().
     */
@Service
    public class UserDetailsServiceImpl implements UserDetailsService {

        // =========================================================================
        // 1. INJEÇÃO DE DEPENDÊNCIA
        // =========================================================================
        // Precisamos do UsuarioRepository para consultar a tabela "usuarios" no banco.

        private final UsuarioRepository repository;

        public UserDetailsServiceImpl(UsuarioRepository repository) {
            this.repository = repository;

    }

    // =========================================================================
    // 2. BUSCA DO USUÁRIO NO BANCO DURANTE O LOGIN
    // =========================================================================
    /**
     * /@param email O Spring chama esse parâmetro de "username" por padrão, mas no
     *              UberDesk o login é feito pelo e-mail digitado no campo name="username".
     * /@return UserDetails: Um objeto padronizado que o Spring Security entende,
     *         contendo login, senha criptografada (BCrypt) e permissões (Roles).
     */
@Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // 1. Busca o usuário no banco pelo e-mail.
    //    Se não encontrar, lança a exceção padrão do Spring Security, que faz
    //    a tela de login exibir a mensagem de erro (?error).
    Usuario usuario = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado com o email: " + email));
    // 2. Converte o seu Enum Perfil (ADMIN, TECNICO, CLIENTE) para o formato de permissão do Spring.
    //    ATENÇÃO AO PREFIXO "ROLE_": No SecurityConfig usamos .hasRole("ADMIN").
    //    Por baixo dos panos, o Spring sempre procura por "ROLE_ADMIN".
    //    Sem esse prefixo "ROLE_", o bloqueio de rotas não reconheceria o perfil!

    SimpleGrantedAuthority permissao = new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name());

    // 3. Retorna o objeto User do próprio Spring Security.
    //    A partir daqui, o próprio Spring pega a senha digitada na tela,
    //    compara com usuario.getSenha() usando o BCryptPasswordEncoder
    //    e decide se libera ou bloqueia o acesso.

    return new User(
            usuario.getEmail(),// Login (username)
            usuario.getSenha(),// Senha já criptografada vinda do banco
            Collections.singletonList(permissao)// Lista de permissões (no nosso caso, 1 perfil)
    );


    }


}
