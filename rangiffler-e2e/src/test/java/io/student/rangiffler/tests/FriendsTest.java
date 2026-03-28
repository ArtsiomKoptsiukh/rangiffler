package io.student.rangiffler.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.student.rangiffler.jupiter.extention.UsersQueueExtension;
import io.student.rangiffler.page.LandingPage;
import io.student.rangiffler.page.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.student.rangiffler.jupiter.extention.UsersQueueExtension.StaticUser;
import static io.student.rangiffler.jupiter.extention.UsersQueueExtension.UserType;
import static io.student.rangiffler.jupiter.extention.UsersQueueExtension.UserType.Type.*;
import static io.student.rangiffler.tests.BaseWebTest.CFG;

public class FriendsTest {

    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        Configuration.browserSize = "1920x1080";
        loginPage = Selenide.open(CFG.frontUrl(), LandingPage.class)
                .openLoginPage();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser emptyUser) {
        loginPage
                .login(emptyUser.name(), emptyUser.password())
                .openPeoplePage()
                .thereAreNoUsersTitleShouldBeVisible();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser userWithFriend) {
        loginPage
                .login(userWithFriend.name(), userWithFriend.password())
                .openPeoplePage()
                .selectFriendsTab()
                .usernameShouldBePresent(userWithFriend.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationShouldBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser userWithIncomeRequest) {
        loginPage
                .login(userWithIncomeRequest.name(), userWithIncomeRequest.password())
                .openPeoplePage()
                .selectIncomeInvitationsTab()
                .usernameShouldBePresent(userWithIncomeRequest.incomeRequest());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void twoEmptyUsersShouldEachSeeEmptyFriendsTable(@UserType(EMPTY) StaticUser emptyUser1, @UserType(EMPTY) StaticUser emptyUser2) {
        Assertions.assertNotEquals(emptyUser1.name(), emptyUser2.name(), "Разные пользователи");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationShouldBePresentInFriendsTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser userWithOutcomeRequest) {
        loginPage
                .login(userWithOutcomeRequest.name(), userWithOutcomeRequest.password())
                .openPeoplePage()
                .selectOutcomeInvitationsTab()
                .usernameShouldBePresent(userWithOutcomeRequest.outcomeRequest());
    }

}
