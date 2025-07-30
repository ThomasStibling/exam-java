package edu.JavaExam;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.JavaExam.dao.UserDao;
import edu.JavaExam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
public class UserApiIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserDao userDao;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        
        userDao.deleteAll();
    }

    @Test
    void testUserRegistration() throws Exception {
        User newUser = new User();
        newUser.setPseudo("newuser");
        newUser.setPassword("password123");

        mvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("newuser")));
    }

    @Test
    void testUserRegistrationWithDuplicatePseudo() throws Exception {
        User existingUser = new User();
        existingUser.setPseudo("duplicateuser");
        existingUser.setPassword("password");
        userDao.save(existingUser);

        User duplicateUser = new User();
        duplicateUser.setPseudo("duplicateuser");
        duplicateUser.setPassword("password2");

        mvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content(mapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setPseudo("testuser1");
        user1.setPassword("password");
        userDao.save(user1);

        User user2 = new User();
        user2.setPseudo("testuser2");
        user2.setPassword("password");
        userDao.save(user2);

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("testuser1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("testuser2")));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setPseudo("testuser");
        user.setPassword("password");
        userDao.save(user);

        mvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("testuser")));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        mvc.perform(get("/api/users/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUserLogin() throws Exception {
        User user = new User();
        user.setPseudo("logintestuser");
        user.setPassword("password");
        userDao.save(user);

        String loginRequest = "{\"pseudo\":\"logintestuser\",\"password\":\"password\"}";

        mvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("token")));
    }
} 