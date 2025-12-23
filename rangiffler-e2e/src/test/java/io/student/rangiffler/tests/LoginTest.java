package io.student.rangiffler.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.student.rangiffler.jupiter.User;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.page.LandingPage;
import io.student.rangiffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.student.rangiffler.tests.BaseWebTest.CFG;

public class LoginTest {

    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        Configuration.browserSize = "1920x1080";
        loginPage = Selenide.open(CFG.frontUrl(), LandingPage.class)
                .openLoginPage();
    }

    @Test
    @User
    @DisplayName("User should see World Map on the main page after login")
    void existedUserShouldBeAbleToLogin(UserJson user) {
        loginPage
                .login(user.username(), user.password())
                .worldMapShouldBeVisible();
    }

    @Test
    @DisplayName("User should see validation error message if password absent")
    void validationMessageShouldBeDisplayedIfPasswordAbsent() {
        loginPage
                .loginWithInvalidCredentials("user1", "")
                .formErrorShouldBeVisible();
    }

    @Test
    @DisplayName("User should stay on login page after login with invalid credentials")
    void userShouldStayOnLoginPageAfterLoginWithInvalidCredentials() {
        loginPage
                .loginWithInvalidCredentials("username", "password")
                .shouldBeOpened();
    }

    @Test
    @DisplayName("User should be redirected to registration page after pressing 'Sign Up' link")
    void userShouldBeRedirectedToRegisterPageAfterPressingSignUpLink() {
        loginPage
                .openRegistrationPage()
                .shouldBeOpened();
    }

}
