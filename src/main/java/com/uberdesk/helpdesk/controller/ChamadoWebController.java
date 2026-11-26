package com.uberdesk.helpdesk.controller;

// Importa as entidades do sistema (estão no pacote raiz)
import com.uberdesk.helpdesk.Chamado;
import com.uberdesk.helpdesk.Interacao;
import com.uberdesk.helpdesk.Usuario;
// Importa os repositórios para consultar e salvar dados no MySQL
import com.uberdesk.helpdesk.repository.ChamadoRepository;
import com.uberdesk.helpdesk.repository.InteracaoRepository;
import com.uberdesk.helpdesk.repository.UsuarioRepository;
// Importa a anotação @Controller (usada para renderizar páginas HTML com Thymeleaf)
import org.springframework.stereotype.Controller;
// Model é o objeto usado para enviar variáveis do Java para o arquivo HTML
import org.springframework.ui.Model;
// Anotações de mapeamento de rotas HTTP
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.uberdesk.helpdesk.Status;
import java.time.LocalDate;

// Principal representa o usuário autenticado atualmente na sessão do Spring Security
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

// @Controller (sem "Rest"): avisa ao Spring que os métodos retornam nomes de páginas HTML (Thymeleaf)
@Controller
public class ChamadoWebController {

    // Repositórios declarados como final (boa prática de injeção via construtor)
    private final ChamadoRepository chamadoRepository;
    private final InteracaoRepository interacaoRepository;
    private final UsuarioRepository usuarioRepository;

    // O Spring injeta automaticamente os 3 repositórios ao iniciar a aplicação
    public ChamadoWebController(ChamadoRepository chamadoRepository,
                                InteracaoRepository interacaoRepository,
                                UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.interacaoRepository = interacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================================================================
    // 1. EXIBIR DETALHES E TIMELINE DO CHAMADO (GET /chamados/{id}/detalhes)
    // =========================================================================
    @GetMapping("/chamados/{id}/detalhes")
    public String detalhesChamado(@PathVariable Long id, Model model) {

        // Busca o chamado pelo ID no MySQL; se não existir, lança uma exceção
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado: " + id));

        // Busca todas as interações vinculadas a este chamado em ordem cronológica (mais antiga -> mais recente)
        List<Interacao> interacoes = interacaoRepository.findByChamadoIdOrderByDataHoraAsc(id);

        // Coloca o chamado e a lista de interações no Model para o Thymeleaf ler no HTML
        model.addAttribute("chamado", chamado);
        model.addAttribute("interacoes", interacoes);

        // Renderiza o arquivo src/main/resources/templates/chamado-detalhes.html
        return "chamado-detalhes";
    }

    // =========================================================================
    // 2. SALVAR NOVA INTERAÇÃO NO CHAMADO (POST /chamados/{id}/interacoes)
    // =========================================================================
    @PostMapping("/chamados/{id}/interacoes")
    public String adicionarInteracao(@PathVariable Long id,
                                     @RequestParam("mensagem") String mensagem,
                                     @RequestParam(value = "encerrar", defaultValue = "false") boolean encerrar,
                                     Principal principal) {

        // Validação básica: se a mensagem vier vazia ou só com espaços, apenas recarrega a tela sem salvar
        if (mensagem == null || mensagem.trim().isEmpty()) {
            return "redirect:/chamados/" + id + "/detalhes";
        }

        // Busca o chamado no banco para vincular a nova interação a ele
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado: " + id));

        // Pega o e-mail (username) de quem está logado no Spring Security
        String emailLogado = principal.getName();

        // Busca o objeto Usuario completo no banco para saber o nome e o perfil (CLIENTE, TECNICO, ADMIN)
        Usuario autor = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + emailLogado));

        // Cria o objeto Interacao removendo espaços extras nas pontas (.trim()) e vincula ao chamado e ao autor
        Interacao novaInteracao = new Interacao(mensagem.trim(), chamado, autor);
        interacaoRepository.save(novaInteracao);
        // Se o checkbox "Encerrar chamado" foi marcado, fecha o chamado e grava a data de hoje

        if (encerrar) {
            chamado.setStatus(Status.ENCERRADO);
            chamado.setDataFechamento(LocalDate.now());
            chamadoRepository.save(chamado);
        }

        // Salva a nova mensagem na tabela "interacoes" do MySQL


        // Redireciona (padrão Post-Redirect-Get) para a tela de detalhes, evitando reenvio duplicado se o usuário der F5
        return "redirect:/chamados/" + id + "/detalhes";
    }
}