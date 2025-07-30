package edu.JavaExam.dao;

import edu.JavaExam.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDao extends JpaRepository<Ticket, Integer> {
    
    List<Ticket> findByResolu(Boolean resolu);
    
    List<Ticket> findBySubmitterId(Integer submitterId);
    
    List<Ticket> findByResolverId(Integer resolverId);
    
    boolean existsByTitre(String titre);
} 