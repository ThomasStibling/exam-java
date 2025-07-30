package edu.JavaExam.controller;
import edu.JavaExam.dao.UserDao;
import edu.JavaExam.model.User;
import edu.JavaExam.model.LoginRequest;
import edu.JavaExam.security.JwtUtils;
import edu.JavaExam.security.AppUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "API pour l'authentification")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Se connecter avec pseudo et mot de passe")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPseudo(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);

            User user = userDao.findByPseudo(loginRequest.getPseudo()).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 