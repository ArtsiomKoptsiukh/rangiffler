package io.student.rangiffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LandingPage {

    private final SelenideElement loginBtn = $("button.MuiButtonBase-root");
    private final SelenideElement registerBtn = $("a.MuiButtonBase-root");

    public LandingPage shouldBeOpened() {
        loginBtn.shouldBe(visible);
        registerBtn.shouldBe(visible);

        return this;
    }

    public LoginPage openLoginPage() {
        loginBtn.click();

        return Selenide.page(LoginPage.class);
    }

    public RegistrationPage openRegistrationPage() {
        registerBtn.click();

        return Selenide.page(RegistrationPage.class);
    }
}
