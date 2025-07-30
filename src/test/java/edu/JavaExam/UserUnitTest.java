package edu.JavaExam;

import edu.JavaExam.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setPseudo("testuser");
        user.setPassword("password123");
        user.setAdmin(false);
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("testuser", user.getPseudo());
        assertEquals("password123", user.getPassword());
        assertFalse(user.getAdmin());
    }

    @Test
    void testUserAdminRole() {
        user.setAdmin(true);
        assertTrue(user.getAdmin());
    }

    @Test
    void testUserValidation() {
        User invalidUser = new User();
        invalidUser.setPseudo("");
        invalidUser.setPassword("password");

        assertEquals("", invalidUser.getPseudo());
        assertNotNull(invalidUser.getPseudo());
    }



    @Test
    void testUserPasswordChange() {
        String newPassword = "newpassword123";
        user.setPassword(newPassword);
        assertEquals(newPassword, user.getPassword());
    }
} 