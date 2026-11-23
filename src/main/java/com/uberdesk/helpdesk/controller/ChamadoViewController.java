package com.uberdesk.helpdesk.controller;

import com.uberdesk.helpdesk.Chamado;
import com.uberdesk.helpdesk.dtos.ChamadoDTO;
import com.uberdesk.helpdesk.service.ChamadoService;
import com.uberdesk.helpdesk.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Controller: Diferente do @RestController (que devolve JSON para APIs),
 * o @Controller indica ao Spring que esta classe gerencia TELAS (Views).
 * As Strings retornadas pelos métodos aqui representam o nome dos arquivos HTML
 * localizados dentro de "src/main/resources/templates/".
 *
 * @RequestMapping("/ui/chamados"): Define o prefixo de URL para todas as rotas
 * desta classe. Usamos "/ui/" para separar as rotas visuais (Thymeleaf)
 * das rotas da API REST ("/chamados").
 */
@Controller
@RequestMapping(value = "/ui/chamados")
public class ChamadoViewController {

    // =========================================================================
    // 1. INJEÇÃO DE DEPENDÊNCIAS (Via Construtor)
    // =========================================================================
    // O Controller não acessa o banco de dados diretamente; ele delega as
    // consultas e regras de negócio para a camada de Service.
    // O modificador "final" garante que essas referências não mudem após a criação.
    private final ChamadoService service;
    private final UsuarioService usuarioService;

    // O Spring identifica este construtor e injeta automaticamente as instâncias
    // de ChamadoService e UsuarioService quando a aplicação sobe.
    public ChamadoViewController(ChamadoService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    // =========================================================================
    // 2. READ - LISTAR TODOS OS CHAMADOS (GET /ui/chamados)
    // =========================================================================
    /**
     * @GetMapping (sem caminho extra): Atende requisições GET em "/ui/chamados".
     *
     * @param model É uma "sacola" de dados enviada do Java para o HTML.
     *              Tudo o que colocamos no Model fica acessível no Thymeleaf via ${...}.
     * @return "chamados" -> O Spring procura e renderiza "templates/chamados.html".
     */
    @GetMapping
    public String listarChamados(Model model) {
        // Busca todos os chamados no banco e envia na variável "listaChamados".
        // No HTML, o th:each="chamado : ${listaChamados}" percorre essa lista para montar a tabela.
        model.addAttribute("listaChamados", service.findAll());

        // Retorna o nome do arquivo HTML (sem a extensão .html)
        return "chamados";
    }

    // =========================================================================
    // 3. CREATE (Passo 1) - ABRIR FORMULÁRIO VAZIO (GET /ui/chamados/novo)
    // =========================================================================
    /**
     * @GetMapping("/novo"): Atende requisições GET em "/ui/chamados/novo".
     * Acionado quando o usuário clica no botão "+ Novo Chamado".
     */
    @GetMapping("/novo")
    public String novoChamadoForm(Model model) {
        // Envia um ChamadoDTO vazio para o formulário fazer o vínculo (th:object="${chamado}").
        // Como o ID desse objeto é null, a tela saberá que se trata de um cadastro novo.
        model.addAttribute("chamado", new ChamadoDTO());

        // Envia a lista de usuários cadastrados para preencher os menus <select>
        // de Técnico Responsável e Cliente dinamicamente.
        model.addAttribute("usuarios", usuarioService.findAll());

        // Abre o arquivo "templates/chamado-form.html"
        return "chamado-form";
    }

    // =========================================================================
    // 4. UPDATE (Passo 1) - ABRIR FORMULÁRIO PREENCHIDO (GET /ui/chamados/editar/{id})
    // =========================================================================
    /**
     * @GetMapping("/editar/{id}"): Atende GET em "/ui/chamados/editar/1", por exemplo.
     * @PathVariable Long id: Captura o número que veio na URL ({id}) e joga na variável "id".
     */
    @GetMapping("/editar/{id}")
    public String editarChamadoForm(@PathVariable Long id, Model model) {
        // 1. Busca o chamado existente no banco pelo ID
        Chamado obj = service.findById(id);

        // 2. Converte a Entidade para DTO e envia para o formulário.
        // Como agora o objeto tem ID e dados preenchidos, o Thymeleaf preenche
        // automaticamente todos os inputs, selects e textareas da tela.
        model.addAttribute("chamado", new ChamadoDTO(obj));

        // 3. Envia novamente a lista de usuários para os <select> funcionarem na edição
        model.addAttribute("usuarios", usuarioService.findAll());

        // Reaproveita exatamente o mesmo arquivo HTML usado na criação
        return "chamado-form";
    }

    // =========================================================================
    // 5. CREATE / UPDATE (Passo 2) - SALVAR NO BANCO (POST /ui/chamados/salvar)
    // =========================================================================
    /**
     * @PostMapping("/salvar"): Atende o envio (POST) do formulário em "/ui/chamados/salvar".
     *
     * @ModelAttribute("chamado"): O Spring pega todos os campos preenchidos pelo usuário
     * no HTML (th:field="*{...}") e monta automaticamente este objeto ChamadoDTO.
     */
    @PostMapping("/salvar")
    public String salvarChamado(@ModelAttribute("chamado") ChamadoDTO objDTO) {
        // Verifica se o campo oculto <input type="hidden" th:field="*{id}"> veio preenchido:
        if (objDTO.getId() != null) {
            // Se já tem ID, estamos EDITANDO um chamado existente
            service.update(objDTO.getId(), objDTO);
        } else {
            // Se o ID é null, estamos CRIANDO um chamado novo
            service.create(objDTO);
        }

        // "redirect:" instrui o navegador a fazer uma nova requisição GET para "/ui/chamados".
        // Isso evita o problema de reenviar o formulário duplicado se o usuário apertar F5.
        return "redirect:/ui/chamados";
    }

    // =========================================================================
    // 6. DELETE - EXCLUIR CHAMADO (GET /ui/chamados/deletar/{id})
    // =========================================================================
    /**
     * @GetMapping("/deletar/{id}"): Atende o clique no botão "Excluir" da tabela.
     * (Em sistemas com segurança avançada, exclusões costumam usar POST/DELETE,
     * mas via GET é o padrão direto para links simples no Thymeleaf).
     */
    @GetMapping("/deletar/{id}")
    public String deletarChamado(@PathVariable Long id) {
        // Solicita ao Service que remova o registro do banco de dados
        service.delete(id);

        // Redireciona de volta para a listagem já atualizada (sem o item excluído)
        return "redirect:/ui/chamados";
    }
}