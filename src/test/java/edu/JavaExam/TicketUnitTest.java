package edu.JavaExam;

import edu.JavaExam.model.Ticket;
import edu.JavaExam.model.User;
import edu.JavaExam.model.Priority;
import edu.JavaExam.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class TicketUnitTest {

    private Ticket ticket;
    private User user;
    private Priority priority;
    private Category category;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setPseudo("testuser");
        user.setPassword("password");
        user.setAdmin(false);

        priority = new Priority();
        priority.setId(1);
        priority.setNom("Haute");

        category = new Category();
        category.setId(1);
        category.setNom("Bug");

        ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitre("Test Ticket");
        ticket.setDescription("Description du ticket de test");
        ticket.setResolu(false);
        ticket.setSubmitter(user);
        ticket.setPriority(priority);
        ticket.setCategories(Arrays.asList(category));
    }

    @Test
    void testTicketCreation() {
        assertNotNull(ticket);
        assertEquals(1, ticket.getId());
        assertEquals("Test Ticket", ticket.getTitre());
        assertEquals("Description du ticket de test", ticket.getDescription());
        assertFalse(ticket.getResolu());
        assertEquals(user, ticket.getSubmitter());
        assertEquals(priority, ticket.getPriority());
        assertEquals(1, ticket.getCategories().size());
        assertEquals(category, ticket.getCategories().get(0));
    }

    @Test
    void testTicketResolution() {
        User adminUser = new User();
        adminUser.setId(2);
        adminUser.setPseudo("admin");
        adminUser.setAdmin(true);

        ticket.setResolu(true);
        ticket.setResolver(adminUser);

        assertTrue(ticket.getResolu());
        assertEquals(adminUser, ticket.getResolver());
    }

    @Test
    void testTicketValidation() {
        Ticket invalidTicket = new Ticket();
        invalidTicket.setTitre("");
        invalidTicket.setDescription("Description");
        invalidTicket.setResolu(false);
        invalidTicket.setPriority(priority);

        assertEquals("", invalidTicket.getTitre());
        assertNotNull(invalidTicket.getTitre());
    }

    @Test
    void testTicketWithMultipleCategories() {
        Category category2 = new Category();
        category2.setId(2);
        category2.setNom("Amélioration");
        
        ticket.setCategories(Arrays.asList(category, category2));
        
        assertEquals(2, ticket.getCategories().size());
        assertTrue(ticket.getCategories().contains(category));
        assertTrue(ticket.getCategories().contains(category2));
    }
} 