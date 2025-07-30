package edu.JavaExam.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import edu.JavaExam.view.TicketView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(TicketView.class)
    private Integer id;
    
    @NotBlank(message = "Le titre est obligatoire")
    @Column(unique = true, nullable = false)
    @JsonView(TicketView.class)
    private String titre;
    
    @JsonView(TicketView.class)
    private String description;
    
    @NotNull(message = "Le statut résolu est obligatoire")
    @JsonView(TicketView.class)
    private Boolean resolu = false;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "submitter_id")
    @JsonBackReference("user-submitted-tickets")
    private User submitter;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resolver_id")
    @JsonBackReference("user-resolved-tickets")
    private User resolver;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority_id", nullable = false)
    private Priority priority;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ticket_categories",
        joinColumns = @JoinColumn(name = "ticket_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
} 