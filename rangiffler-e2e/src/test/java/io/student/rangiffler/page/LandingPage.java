package io.student.rangiffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LandingPage extends BasePage<LandingPage>{

    private final SelenideElement loginBtn = $("button.MuiButtonBase-root");
    private final SelenideElement registerBtn = $("a.MuiButtonBase-root");

    @Override
    protected void verifyPageOpened() {
        loginBtn.shouldBe(visible);
        registerBtn.shouldBe(visible);
    }

    public LoginPage openLoginPage() {
        loginBtn.click();

        return Selenide.page(LoginPage.class).shouldBeOpened();
    }

    public RegistrationPage openRegistrationPage() {
        registerBtn.click();

        return Selenide.page(RegistrationPage.class).shouldBeOpened();
    }
}
