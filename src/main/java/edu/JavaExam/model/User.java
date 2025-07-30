package edu.JavaExam.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import edu.JavaExam.view.UserView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(UserView.class)
    private Integer id;
    
    @NotBlank(message = "Le pseudo est obligatoire")
    @Column(unique = true, nullable = false)
    @JsonView(UserView.class)
    private String pseudo;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(nullable = false)
    private String password;
    
    @NotNull(message = "Le statut admin est obligatoire")
    @JsonView(UserView.class)
    private Boolean admin = false;
    
    @OneToMany(mappedBy = "submitter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("user-submitted-tickets")
    private List<Ticket> submittedTickets;
    
    @OneToMany(mappedBy = "resolver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("user-resolved-tickets")
    private List<Ticket> resolvedTickets;
} 