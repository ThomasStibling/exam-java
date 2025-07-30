package edu.JavaExam.controller;

import edu.JavaExam.dao.CategoryDao;
import edu.JavaExam.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Gestion des catégories", description = "API pour la gestion des catégories")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @GetMapping
    @Operation(summary = "Récupérer toutes les catégories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryDao.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une catégorie par son ID")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        return categoryDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle catégorie")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        if (categoryDao.existsByNom(category.getNom())) {
            return ResponseEntity.badRequest().build();
        }
        
        Category savedCategory = categoryDao.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCategory);
    }


} 