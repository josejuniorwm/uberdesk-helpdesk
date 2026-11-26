package com.uberdesk.helpdesk.controller;

import com.uberdesk.helpdesk.Chamado;
import com.uberdesk.helpdesk.dtos.ChamadoDTO;
import com.uberdesk.helpdesk.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /@RestController: Controlador de API REST (recebe requisições HTTP e devolve JSON).
 * /@RequestMapping("/chamados"): Caminho base de todas as rotas REST desta classe.
 */
@RestController
@RequestMapping(value = "/chamados")
public class ChamadoController {

    // Injeção de dependência via construtor: delega a regra de negócio para o ChamadoService
    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    // READ (Por ID): GET /chamados/{id} -> Retorna os dados de um chamado em JSON
    @GetMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> findById(@PathVariable Long id) {
        Chamado obj = service.findById(id);
        return ResponseEntity.ok().body(new ChamadoDTO(obj));
    }

    // READ (Todos): GET /chamados -> Lista todos os chamados em JSON
    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> findAll() {
        List<Chamado> list = service.findAll();
        List<ChamadoDTO> listDTO = list.stream()
                .map(obj -> new ChamadoDTO(obj))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    // CREATE: POST /chamados -> Cria um novo chamado e retorna status 201 (Created)
    @PostMapping
    public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO objDTO) {
        Chamado obj = service.create(objDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    // UPDATE: PUT /chamados/{id} -> Atualiza um chamado existente
    @PutMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> update(@PathVariable Long id, @Valid @RequestBody ChamadoDTO objDTO) {
        Chamado newObj = service.update(id, objDTO);
        return ResponseEntity.ok().body(new ChamadoDTO(newObj));
    }

    // DELETE: DELETE /chamados/{id} -> Exclui um chamado e retorna status 204 (No Content)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}