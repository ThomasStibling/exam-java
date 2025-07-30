package edu.JavaExam;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.JavaExam.dao.CategoryDao;
import edu.JavaExam.dao.PriorityDao;
import edu.JavaExam.dao.TicketDao;
import edu.JavaExam.dao.UserDao;
import edu.JavaExam.model.Priority;
import edu.JavaExam.model.Ticket;
import edu.JavaExam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class TicketApiIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PriorityDao priorityDao;

    @Autowired
    private CategoryDao categoryDao;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        
        ticketDao.deleteAll();
        userDao.deleteAll();
        priorityDao.deleteAll();
        categoryDao.deleteAll();
    }

    @Test
    void testGetAllTickets() throws Exception {
        mvc.perform(get("/api/tickets"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreateTicket() throws Exception {
        User user = new User();
        user.setPseudo("testuser");
        user.setPassword("password");
        user.setAdmin(false);
        userDao.save(user);

        Priority priority = new Priority();
        priority.setNom("Test Priority");
        priorityDao.save(priority);

        Ticket ticket = new Ticket();
        ticket.setTitre("Test Ticket");
        ticket.setDescription("Test Description");
        ticket.setPriority(priority);

        mvc.perform(post("/api/tickets")
                .contentType("application/json")
                .content(mapper.writeValueAsString(ticket)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Ticket")));
    }

    @Test
    void testGetTicketById() throws Exception {
        User user = new User();
        user.setPseudo("testuser2");
        user.setPassword("password");
        userDao.save(user);

        Priority priority = new Priority();
        priority.setNom("Test Priority 2");
        priorityDao.save(priority);

        Ticket ticket = new Ticket();
        ticket.setTitre("Test Ticket 2");
        ticket.setDescription("Test Description 2");
        ticket.setSubmitter(user);
        ticket.setPriority(priority);
        ticket.setResolu(false);
        ticketDao.save(ticket);

        mvc.perform(get("/api/tickets/" + ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Ticket 2")));
    }

    @Test
    void testGetTicketByIdNotFound() throws Exception {
        mvc.perform(get("/api/tickets/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "usertest")
    void testCreateTicketAsUser() throws Exception {
        User user = new User();
        user.setPseudo("usertest");
        user.setPassword("password");
        user.setAdmin(false);
        userDao.save(user);

        Priority priority = new Priority();
        priority.setNom("Test Priority");
        priorityDao.save(priority);

        Ticket ticket = new Ticket();
        ticket.setTitre("Test Ticket User");
        ticket.setDescription("Test Description");
        ticket.setPriority(priority);

        mvc.perform(post("/api/tickets")
                .contentType("application/json")
                .content(mapper.writeValueAsString(ticket)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "adminuser", roles = {"ADMIN"})
    void testResolveTicketAsAdmin() throws Exception {
        User adminUser = new User();
        adminUser.setPseudo("adminuser");
        adminUser.setPassword("password");
        adminUser.setAdmin(true);
        userDao.save(adminUser);

        User user = new User();
        user.setPseudo("testuser3");
        user.setPassword("password");
        userDao.save(user);

        Priority priority = new Priority();
        priority.setNom("Test Priority 3");
        priorityDao.save(priority);

        Ticket ticket = new Ticket();
        ticket.setTitre("Test Ticket 3");
        ticket.setDescription("Test Description 3");
        ticket.setSubmitter(user);
        ticket.setPriority(priority);
        ticket.setResolu(false);
        ticketDao.save(ticket);

        mvc.perform(put("/api/tickets/" + ticket.getId() + "/resolve"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "normaluser")
    void testResolveTicketAsUser_shouldSend403() throws Exception {
        User normalUser = new User();
        normalUser.setPseudo("normaluser");
        normalUser.setPassword("password");
        normalUser.setAdmin(false);
        userDao.save(normalUser);

        User user = new User();
        user.setPseudo("testuser4");
        user.setPassword("password");
        userDao.save(user);

        Priority priority = new Priority();
        priority.setNom("Test Priority 4");
        priorityDao.save(priority);

        Ticket ticket = new Ticket();
        ticket.setTitre("Test Ticket 4");
        ticket.setDescription("Test Description 4");
        ticket.setSubmitter(user);
        ticket.setPriority(priority);
        ticket.setResolu(false);
        ticketDao.save(ticket);

        mvc.perform(put("/api/tickets/" + ticket.getId() + "/resolve"))
                .andExpect(status().isForbidden());
    }
} 