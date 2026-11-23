package com.uberdesk.helpdesk.repository;

import com.uberdesk.helpdesk.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    // Toda a magia de CRUD (Create, Read, Update, Delete) para os chamados
    // já está garantida apenas por esta linha de código.
}
