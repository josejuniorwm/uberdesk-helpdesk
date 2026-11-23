package com.uberdesk.helpdesk.security;

import com.uberdesk.helpdesk.Perfil;
import com.uberdesk.helpdesk.Usuario;
import com.uberdesk.helpdesk.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * /@Component: Avisa ao Spring para gerenciar esta classe automaticamente.
 *
 * implements CommandLineRunner: Tudo o que estiver dentro do mét0do run()
 * é executado automaticamente logo após o Spring Boot terminar de subir
 * e conectar no banco de dados.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // O Spring injeta automaticamente o Repository e o BCryptPasswordEncoder (do SecurityConfig)
    public DataLoader(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String emailAdmin = "admin@uberdesk.com";

        // Verifica se o admin já existe no banco.
        // Se já existir (nas próximas vezes que você rodar o projeto), não faz nada e evita erro de e-mail duplicado.
        if (repository.findByEmail(emailAdmin).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador UberDesk");
            admin.setEmail(emailAdmin);

            // Criptografa a senha com BCrypt antes de salvar no MySQL
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setPerfil(Perfil.ADMIN);

            repository.save(admin);
            System.out.println(">>> [DataLoader] Usuário ADMIN inicial criado: admin@uberdesk.com / 123456");
        }
    }
}