package com.uberdesk.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * /@Controller (e não @RestController): indica ao Spring que os métodos aqui
 * retornam o NOME de uma página HTML (Thymeleaf), e não um JSON.
 */
@Controller
public class LoginController {

    /**
     * Quando o Spring Security redireciona o navegador para "/login"
     * (configurado no SecurityConfig em .loginPage("/login")),
     * este mét0do responde entregando o arquivo "templates/login.html".
     *
     * Obs.: Você NÃO precisa criar um @PostMapping("/login") aqui!
     * O próprio Spring Security intercepta o POST do formulário automaticamente,
     * chama o seu UserDetailsServiceImpl e confere a senha com BCrypt.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}