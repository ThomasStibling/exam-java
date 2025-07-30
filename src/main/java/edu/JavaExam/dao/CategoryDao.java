package edu.JavaExam.dao;

import edu.JavaExam.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
    
    boolean existsByNom(String nom);
} 