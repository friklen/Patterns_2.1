package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class RegistrationTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void successfulLoginIfRegisteredActiveUser() {
        var registredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registredUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void getErrorIfNotRegisteredUser() {
        var notRegistredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegistredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegistredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe((Condition.visible));
    }

    @Test
    void getErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe((Condition.visible));
    }

    @Test
    void getErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe((Condition.visible));

    }

    @Test
    void getErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }
}
