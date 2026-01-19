package io.student.rangiffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement signInBtn = $("button[type='submit']");
    private final SelenideElement formErrorText = $(".form__error");
    private final SelenideElement signUpLink = $(".form__link");

    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        signInBtn.click();

        return Selenide.page(MainPage.class).shouldBeOpened();
    }

    public LoginPage loginWithInvalidCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        signInBtn.click();

        return this;
    }

    public LoginPage formErrorShouldBeVisible() {
        formErrorText.shouldBe(visible);

        return this;
    }

    public RegistrationPage openRegistrationPage() {
        signUpLink.click();

        return Selenide.page(RegistrationPage.class).shouldBeOpened();
    }

    @Override
    protected void verifyPageOpened() {
        signInBtn.shouldBe(visible);
        usernameInput.shouldBe(visible);
    }
}
