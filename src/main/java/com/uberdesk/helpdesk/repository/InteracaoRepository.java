package com.uberdesk.helpdesk.repository;

// Importa a entidade Interacao que está no pacote raiz
import com.uberdesk.helpdesk.Interacao;
// Importa a interface do Spring Data JPA que já traz métodos prontos (save, findById, delete, etc.)
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Ao estender JpaRepository<Interacao, Long>, o Spring cria a implementação em tempo de execução
public interface InteracaoRepository extends JpaRepository<Interacao, Long> {

    // Consulta derivada (Derived Query): o Spring gera o SQL automaticamente pelo nome do mét0do.
    // Busca todas as interações de um chamado específico ordenadas da mais antiga para a mais recente (formato de chat/timeline)
    List<Interacao> findByChamadoIdOrderByDataHoraAsc(Long chamadoId);
}