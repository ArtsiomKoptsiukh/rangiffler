package io.student.rangiffler.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.student.rangiffler.page.LandingPage;
import io.student.rangiffler.page.RegistrationPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegisterUserTest extends BaseWebTest {

    RegistrationPage registrationPage;

    @BeforeEach
    void setUp() {
        Configuration.browserSize = "1920x1080";
        registrationPage = Selenide.open(CFG.frontUrl(), LandingPage.class)
                .openRegistrationPage();
    }

    @Test
    @DisplayName("New user should be registered")
    void newUserShouldBeRegistered() {
        registrationPage
                .registerUser(FAKER.name().firstName(), FAKER.internet().password(3, 12));
    }

    @Test
    @DisplayName("User with existed username should not be registered")
    void userWithExistedUsernameShouldNotBeRegistered() {
        registrationPage
                .registerUserWithInvalidCredentials("user1", "user1")
                .usernameAlreadyExistNotificationShouldBeVisible();
    }

    @Test
    @DisplayName("User should see validation error if password and confirmation password are different")
    void userShouldSeeValidationErrorIfPasswordAndConfirmationPasswordAreDifferent() {
        registrationPage
                .registerUserWithInvalidCredentials("user1", "user1", "user2")
                .passwordsShouldBeEqualNotificationShouldBeVisible();
    }

}
