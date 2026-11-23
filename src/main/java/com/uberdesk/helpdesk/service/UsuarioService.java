package com.uberdesk.helpdesk.service;

import com.uberdesk.helpdesk.Usuario;
import com.uberdesk.helpdesk.dtos.UsuarioDTO;
import com.uberdesk.helpdesk.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // 2. Recebemos os DOIS nos parâmetros do construtor.
    // O Spring injeta automaticamente o Repository e o BCryptPasswordEncoder.
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario findById(Long id) {
        Optional<Usuario> obj = repository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Objeto não encontrado! ID: " + id));
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    /**
     * 1. MÉT0DO CRIAR (Agora protegido pelo DTO)
     * O Service recebe o DTO limpo da internet, cria um Usuario vazio (Entidade),
     * copia os dados seguros para ele e manda o repositório salvar.
     */
    public Usuario create(UsuarioDTO objDTO) {
        Usuario newObj = new Usuario();
        newObj.setNome(objDTO.getNome());
        newObj.setEmail(objDTO.getEmail());
        newObj.setPerfil(objDTO.getPerfil());

        String senhaBruta = (objDTO.getSenha() != null && !objDTO.getSenha().isBlank())
                ? objDTO.getSenha()
                : "123456";

        // 2. Criptografa a senha com BCrypt e atribui ao novo usuário antes de salvar
        newObj.setSenha(passwordEncoder.encode(senhaBruta));

        return repository.save(newObj);
    }

    /**
     * 2. MÉT0DO ATUALIZAR (PUT)
     * Recebemos o ID de quem vai ser alterado e os novos dados (no DTO).
     */
    public Usuario update(Long id, UsuarioDTO objDTO) {
        // Primeiro, verifica se o usuário existe. Se não existir, o findById já trava o processo aqui.
        Usuario oldObj = findById(id);

        // Substitui os dados antigos pelos novos que vieram da internet
        oldObj.setNome(objDTO.getNome());
        oldObj.setEmail(objDTO.getEmail());
        oldObj.setPerfil(objDTO.getPerfil());
        // Só altera a senha se o campo for preenchido na edição

        if (objDTO.getSenha() != null && !objDTO.getSenha().isBlank()){
            oldObj.setSenha(passwordEncoder.encode(objDTO.getSenha()));
        }

        // Salva por cima do registro antigo
        return repository.save(oldObj);
    }

    public void delete(Long id) {
        Usuario obj = findById(id);
        repository.deleteById(id);
    }
}