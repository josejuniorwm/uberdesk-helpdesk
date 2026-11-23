package com.uberdesk.helpdesk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Configuration e @EnableWebSecurity avisam ao Spring que esta classe
 * define todas as regras de autenticação e autorização do sistema.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 1. CRIPTOGRAFIA DE SENHAS (BCrypt)
     * Este @Bean ensina ao Spring Security qual algoritmo usar para criptografar
     * novas senhas e para comparar a senha digitada no login com o hash salvo no banco.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 2. CORRENTE DE FILTROS DE SEGURANÇA (SecurityFilterChain)
     * Toda requisição HTTP passa por aqui antes de chegar nos seus Controllers.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Desabilita o CSRF por enquanto para facilitar testes da API REST (Postman/Insomnia)
                // junto com o Thymeleaf sem precisar configurar tokens extras nas requisições.
                .csrf(AbstractHttpConfigurer::disable)

                // 3. REGRAS DE AUTORIZAÇÃO (Quem pode acessar o quê)
                .authorizeHttpRequests(auth -> auth
                        // Libera arquivos estáticos (CSS, JS, imagens) e a própria tela de login
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()
                        // Apenas usuários com perfil ADMIN podem gerenciar usuários
                        .requestMatchers("/ui/usuarios/**", "/usuarios/**").hasRole("ADMIN")
                        // Qualquer outra rota do sistema exige que o usuário esteja logado
                        .anyRequest().authenticated()
                )

                // 4. CONFIGURAÇÃO DO LOGIN (Tela personalizada)
                .formLogin(form -> form
                        // Indica a rota da nossa tela HTML de login (em vez da tela padrão do Spring)
                        .loginPage("/login")
                        // Para onde o usuário é redirecionado assim que faz login com sucesso
                        .defaultSuccessUrl("/ui/chamados", true)
                        .permitAll()
                )

                // 5. CONFIGURAÇÃO DO LOGOUT (Sair do sistema)
                .logout(logout -> logout
                        // Rota acionada ao clicar no botão "Sair"
                        .logoutUrl("/logout")
                        // Para onde o usuário volta após encerrar a sessão
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}