package com.uberdesk.helpdesk.controller;

import com.uberdesk.helpdesk.Usuario;
import com.uberdesk.helpdesk.dtos.UsuarioDTO;
import com.uberdesk.helpdesk.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        Usuario obj = service.findById(id);
        return ResponseEntity.ok().body(new UsuarioDTO(obj));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<Usuario> list = service.findAll();
        List<UsuarioDTO> listDTO = list.stream().map(obj -> new UsuarioDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    /**
     * 3. ROTA POST PROTEGIDA
     * Agora recebemos @RequestBody UsuarioDTO em vez de Usuario.
     * // MÉT0DO CREATE: O @Valid obriga o Spring a checar as regras do DTO antes de entrar no método.
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO objDTO) {
        Usuario novoObj = service.create(objDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(novoObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * 4. A NOVA ROTA PUT (Atualizar)
     * Ela pede o ID na URL (/{id}) e os novos dados no corpo (RequestBody).
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioDTO objDTO) {
        Usuario obj = service.update(id, objDTO);

        // Retorna status 200 OK com os dados novos confirmados
        return ResponseEntity.ok().body(new UsuarioDTO(obj));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}