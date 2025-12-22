package io.student.rangiffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitPasswordInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("button[type='submit']");
    private final SelenideElement signInBtn = $(".form_sign-in");
    private final SelenideElement registrationCompletedText = $(".form__paragraph_success");
    private final SelenideElement formError = $(".form__error");

    public RegistrationPage registrationPageShouldBeOpened() {
        signUpBtn.shouldBe(visible);

        return this;
    }

    public LandingPage registerUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitPasswordInput.setValue(password);
        signUpBtn.click();
        registrationCompletedText.shouldBe(visible);
        signInBtn.click();

        return Selenide.page(LandingPage.class);
    }

    public RegistrationPage registerUserWithInvalidCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitPasswordInput.setValue(password);
        signUpBtn.click();

        return this;
    }

    public RegistrationPage registerUserWithInvalidCredentials(String username, String password, String confirmationPassword) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitPasswordInput.setValue(confirmationPassword);
        signUpBtn.click();

        return this;
    }

    public RegistrationPage usernameAlreadyExistNotificationShouldBeVisible() {
        formError
                .shouldBe(visible)
                .shouldHave(text("already exists"));

        return this;
    }

    public RegistrationPage passwordsShouldBeEqualNotificationShouldBeVisible() {
        formError
                .shouldBe(visible)
                .shouldHave(text("Passwords should be equal"));

        return this;
    }

}
