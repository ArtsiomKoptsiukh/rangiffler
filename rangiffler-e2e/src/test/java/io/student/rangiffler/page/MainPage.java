package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement addPhotoBtn = $$("button").findBy(exactText("Add photo"));
    private final SelenideElement worldMapElem = $(".worldmap__figure-container");

    public MainPage shouldBeOpened() {
        addPhotoBtn.shouldBe(visible);

        return this;
    }

    public MainPage worldMapShouldBeVisible() {
        worldMapElem.shouldBe(visible);

        return this;
    }
}
