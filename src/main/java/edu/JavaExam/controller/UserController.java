package edu.JavaExam.controller;

import edu.JavaExam.dao.UserDao;
import edu.JavaExam.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Gestion des utilisateurs", description = "API pour la gestion des utilisateurs")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Créer un nouvel utilisateur")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (userDao.existsByPseudo(user.getPseudo())) {
            return ResponseEntity.badRequest().build();
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAdmin(false);
        User savedUser = userDao.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les utilisateurs")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userDao.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par son ID")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 