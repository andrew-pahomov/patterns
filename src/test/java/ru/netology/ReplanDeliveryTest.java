package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.Data;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ReplanDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Successful planning and re-planning meetings")
    void successfulPlanAndReplanMeet() {

        var user = Data.Registration.generateUser("ru"); // задаем переменную user, которая берет свои данные в классе DataGenerator в классе Registration методом generateUser
        int addDaysFirstMeet = 5; // задаем первую числовую переменную и ее значение (на сколько будет сдвигаться оперделяемая дата от текущей даты) для работы метода generateDate в классе DataGenerator
        int addDaysSecondMeet = 3; // задаем вторую числовую переменную и ее значение (на сколько будет сдвигаться оперделяемая дата от текущей даты) для работы метода generateDate в классе DataGenerator
        String firstDateMeet = Data.generateDate(addDaysFirstMeet); // задаем текстовую переменную хранящую информацию о первой дате, которая берет свои данные в классе DataGenerator методе generateDate
        String secondDateMeet = Data.generateDate(addDaysSecondMeet); // задаем текстовую переменную хранящую информацию о второй дате, которая берет свои данные в классе DataGenerator методе generateDate
        Object test;
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstDateMeet);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstDateMeet))
                .shouldBe(visible);

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondDateMeet);
        $(byText("Запланировать")).click();
        $(byText("Необходимо подтверждение")).shouldBe(visible);
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondDateMeet))
                .shouldBe(visible);


    }
}