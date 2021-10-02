package home.artreiner.tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AllureTest {
    String baseUrl = "https://github.com";

    @Test
    void searchAllureRepoOnGitHub() {
        open(baseUrl);
        $(By.name("q")).setValue("allure").pressEnter();
        $(".repo-list").shouldHave(Condition.text("allure"));
    }
}

