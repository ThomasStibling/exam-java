package edu.JavaExam.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.JavaExam.view.CategoryView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(CategoryView.class)
    private Integer id;
    
    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Column(unique = true, nullable = false)
    @JsonView(CategoryView.class)
    private String nom;
} 