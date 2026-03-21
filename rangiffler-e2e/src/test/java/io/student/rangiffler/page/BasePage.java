package io.student.rangiffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BasePage<T extends BasePage<T>> {

    private final SelenideElement peopleLink = $("a[href='/people']");

    protected abstract void verifyPageOpened();

    public final T shouldBeOpened() {
        verifyPageOpened();
        return self();
    }

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }

    public PeoplePage openPeoplePage() {
        peopleLink.shouldBe(visible);
        peopleLink.click();

        return Selenide.page(PeoplePage.class).shouldBeOpened();
    }
}
