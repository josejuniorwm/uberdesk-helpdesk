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
 * @RestController: Combina @Controller + @ResponseBody. Avisa ao Spring que esta
 * classe é um controlador de API REST: ela recebe requisições HTTP e devolve os
 * dados diretamente no formato JSON (e não páginas HTML como o Thymeleaf).
 *
 * @RequestMapping("/chamados"): Define o caminho base (endpoint) de todas as rotas
 * desta classe. Qualquer requisição para "http://localhost:8080/chamados" cai aqui.
 */
@RestController
@RequestMapping(value = "/chamados")
public class ChamadoController {

    // =========================================================================
    // 1. INJEÇÃO DE DEPENDÊNCIA (Via Construtor)
    // =========================================================================
    // O Controller atua apenas como a "porta de entrada" das requisições HTTP.
    // Ele não executa regras de negócio nem acessa o banco diretamente; tudo é
    // delegado para o ChamadoService.
    private final ChamadoService service;

    // O Spring injeta automaticamente a instância de ChamadoService ao iniciar.
    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    // =========================================================================
    // 2. READ (Por ID) - BUSCAR UM CHAMADO ESPECÍFICO (GET /chamados/{id})
    // =========================================================================
    /**
     * @GetMapping("/{id}"): Atende requisições HTTP GET com um parâmetro na URL
     * (ex: GET http://localhost:8080/chamados/1).
     *
     * @PathVariable Long id: Captura o valor passado na URL ({id}) e converte
     * automaticamente para a variável Java "id".
     *
     * ResponseEntity<ChamadoDTO>: Permite controlar tanto o corpo da resposta (JSON)
     * quanto o código de status HTTP (ex: 200 OK, 404 Not Found).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> findById(@PathVariable Long id) {
        // Busca a entidade Chamado no banco através do Service
        Chamado obj = service.findById(id);

        // Converte a Entidade para ChamadoDTO antes de devolver na resposta.
        // Isso evita expor dados sensíveis da entidade e previne loops infinitos no JSON.
        return ResponseEntity.ok().body(new ChamadoDTO(obj));
    }

    // =========================================================================
    // 3. READ (Todos) - LISTAR TODOS OS CHAMADOS (GET /chamados)
    // =========================================================================
    /**
     * @GetMapping (sem caminho extra): Atende requisições GET em "/chamados".
     */
    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> findAll() {
        // 1. Busca todas as entidades Chamado cadastradas no banco
        List<Chamado> list = service.findAll();

        // 2. Usa a Stream API do Java para percorrer a lista e converter cada
        //    Entidade (Chamado) em um objeto de transferência (ChamadoDTO)
        List<ChamadoDTO> listDTO = list.stream()
                .map(obj -> new ChamadoDTO(obj))
                .collect(Collectors.toList());

        // 3. Devolve a lista em JSON com status HTTP 200 (OK)
        return ResponseEntity.ok().body(listDTO);
    }

    // =========================================================================
    // 4. CREATE - CADASTRAR NOVO CHAMADO (POST /chamados)
    // =========================================================================
    /**
     * @PostMapping: Atende requisições HTTP POST em "/chamados".
     *
     * @Valid: Aciona as validações configuradas no ChamadoDTO (ex: @NotNull, @NotBlank).
     *         Se algum campo obrigatório faltar, o Spring bloqueia e retorna erro 400.
     * @RequestBody: Pega o JSON enviado no corpo da requisição (ex: pelo Postman,
     *               React ou Angular) e transforma automaticamente em um ChamadoDTO.
     */
    @PostMapping
    public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO objDTO) {
        // 1. Envia o DTO para o Service salvar o novo chamado no banco
        Chamado obj = service.create(objDTO);

        // 2. Boa prática REST: monta a URL completa de acesso ao recurso recém-criado
        //    (ex: http://localhost:8080/chamados/5)
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();

        // 3. Retorna status HTTP 201 (Created) com a URL no cabeçalho "Location"
        return ResponseEntity.created(uri).build();
    }

    // =========================================================================
    // 5. UPDATE - ATUALIZAR CHAMADO EXISTENTE (PUT /chamados/{id})
    // =========================================================================
    /**
     * @PutMapping("/{id}"): Atende requisições HTTP PUT em "/chamados/{id}".
     * Recebe tanto o ID pela URL (@PathVariable) quanto os novos dados no corpo
     * em JSON (@RequestBody), já validados pelo @Valid.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> update(@PathVariable Long id, @Valid @RequestBody ChamadoDTO objDTO) {
        // Manda o Service localizar o chamado pelo ID e atualizar os dados no banco
        Chamado newObj = service.update(id, objDTO);

        // Devolve o chamado já atualizado (convertido em DTO) com status HTTP 200 (OK)
        return ResponseEntity.ok().body(new ChamadoDTO(newObj));
    }

    // =========================================================================
    // 6. DELETE - EXCLUIR CHAMADO (DELETE /chamados/{id})
    // =========================================================================
    /**
     * @DeleteMapping("/{id}"): Atende requisições HTTP DELETE em "/chamados/{id}".
     * ResponseEntity<Void>: Indica que a resposta não terá nenhum corpo JSON.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Solicita ao Service a remoção do registro no banco de dados
        service.delete(id);

        // Padrão REST para exclusão bem-sucedida: retorna status HTTP 204 (No Content),
        // avisando que a operação funcionou e não há mais conteúdo para retornar.
        return ResponseEntity.noContent().build();
    }
}