package edu.JavaExam.controller;

import edu.JavaExam.dao.PriorityDao;
import edu.JavaExam.model.Priority;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
@Tag(name = "Gestion des priorités", description = "API pour la gestion des priorités")
public class PriorityController {

    @Autowired
    private PriorityDao priorityDao;

    @GetMapping
    @Operation(summary = "Récupérer toutes les priorités")
    public ResponseEntity<List<Priority>> getAllPriorities() {
        List<Priority> priorities = priorityDao.findAll();
        return ResponseEntity.ok(priorities);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une priorité par son ID")
    public ResponseEntity<Priority> getPriorityById(@PathVariable Integer id) {
        return priorityDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle priorité")
    public ResponseEntity<Priority> createPriority(@Valid @RequestBody Priority priority) {
        if (priorityDao.existsByNom(priority.getNom())) {
            return ResponseEntity.badRequest().build();
        }
        
        Priority savedPriority = priorityDao.save(priority);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedPriority);
    }
} 