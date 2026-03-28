package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PeoplePage extends BasePage<PeoplePage> {

    private final SelenideElement searchPeopleInput = $("input[placeholder='Search people']");
    private final SelenideElement noUsersTitle = $$("p").findBy(exactText("There are no users yet"));
    private final SelenideElement friendsTab = $$("button[role='tab']").findBy(exactText("Friends"));
    private final SelenideElement incomeInvitationsTab = $$("button[role='tab']").findBy(exactText("Income invitations"));
    private final SelenideElement outcomeInvitationsTab = $$("button[role='tab']").findBy(exactText("Outcome invitations"));
    private final ElementsCollection usernameColumnCells = $$("table tbody tr td:first-of-type");

    @Override
    protected void verifyPageOpened() {
        searchPeopleInput.shouldBe(visible);
    }

    public PeoplePage thereAreNoUsersTitleShouldBeVisible() {
        noUsersTitle.shouldBe(visible);

        return this;
    }

    public PeoplePage selectFriendsTab() {
        friendsTab.click();

        return this;
    }

    public PeoplePage selectIncomeInvitationsTab() {
        incomeInvitationsTab.click();

        return this;
    }

    public PeoplePage selectOutcomeInvitationsTab() {
        outcomeInvitationsTab.click();

        return this;
    }

    public PeoplePage usernameShouldBePresent(String username) {
        usernameColumnCells.findBy(exactText(username)).shouldBe(visible);
        return this;
    }
}
