package edu.JavaExam.controller;

import edu.JavaExam.dao.TicketDao;
import edu.JavaExam.dao.UserDao;
import edu.JavaExam.model.Ticket;
import edu.JavaExam.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Gestion des tickets", description = "API pour la gestion des tickets")
public class TicketController {

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

    @PostMapping
    @Operation(summary = "Créer un nouveau ticket")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        if (ticketDao.existsByTitre(ticket.getTitre())) {
            return ResponseEntity.badRequest().build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return userDao.findByPseudo(auth.getName())
                .map(currentUser -> {
                    ticket.setResolu(false);
                    ticket.setSubmitter(currentUser);
                    Ticket savedTicket = ticketDao.save(ticket);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les tickets (connecté) ou tickets non résolus (non connecté)")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        List<Ticket> tickets;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            tickets = ticketDao.findAll();
        } else {
            tickets = ticketDao.findByResolu(false);
        }
        
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un ticket par son ID")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Integer id) {
        return ticketDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Marquer un ticket comme résolu (admin seulement)")
    public ResponseEntity<Ticket> resolveTicket(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return userDao.findByPseudo(auth.getName())
                .filter(User::getAdmin)
                .flatMap(currentUser -> 
                    ticketDao.findById(id)
                            .map(ticket -> {
                                ticket.setResolu(true);
                                ticket.setResolver(currentUser);
                                return ticketDao.save(ticket);
                            })
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/my-tickets")
    @Operation(summary = "Récupérer les tickets créés par l'utilisateur connecté")
    public ResponseEntity<List<Ticket>> getMyTickets() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return userDao.findByPseudo(auth.getName())
                .map(currentUser -> {
                    List<Ticket> myTickets = ticketDao.findBySubmitterId(currentUser.getId());
                    return ResponseEntity.ok(myTickets);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/resolved-by-me")
    @Operation(summary = "Récupérer les tickets résolus par l'utilisateur connecté (admin seulement)")
    public ResponseEntity<List<Ticket>> getTicketsResolvedByMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return userDao.findByPseudo(auth.getName())
                .filter(User::getAdmin)
                .map(currentUser -> {
                    List<Ticket> resolvedTickets = ticketDao.findByResolverId(currentUser.getId());
                    return ResponseEntity.ok(resolvedTickets);
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/user/{userId}/tickets")
    @Operation(summary = "Récupérer les tickets créés par un utilisateur spécifique (admin seulement)")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Integer userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return userDao.findByPseudo(auth.getName())
                .filter(User::getAdmin)
                .map(currentUser -> {
                    List<Ticket> userTickets = ticketDao.findBySubmitterId(userId);
                    return ResponseEntity.ok(userTickets);
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/resolved-by/{userId}")
    @Operation(summary = "Récupérer les tickets résolus par un utilisateur spécifique (admin seulement)")
    public ResponseEntity<List<Ticket>> getTicketsResolvedByUser(@PathVariable Integer userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return userDao.findByPseudo(auth.getName())
                .filter(User::getAdmin)
                .map(currentUser -> {
                    List<Ticket> resolvedTickets = ticketDao.findByResolverId(userId);
                    return ResponseEntity.ok(resolvedTickets);
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
} 