package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"application-test.properties"})
class ReviewBlockTests {

    private static ChromeDriver driver;

    @Value("Test review example")
    String text;

    @BeforeAll
    static void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:/JavaExamples/SkillboxBootMyBookShopApp/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    public void NoAccessToNewReview() throws InterruptedException {
        ReviewBlock reviewBlock = new ReviewBlock(driver);
        reviewBlock
                .callBookSlugPage()
                .pause();

        assertTrue(driver.findElements(By.xpath("/html/body/div/div/main/div/div[3]/strong")).isEmpty());
    }

    @Test
    @Sql(value = "/scripts-test/test-data-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/test-data-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void accessToAddNewReview() throws InterruptedException {
        ReviewBlock reviewBlock = new ReviewBlock(driver);
        reviewBlock
                .callMyPage()
                .pause()
                .chooseRadioButtonEmail()
                .pause()
                .setUpEmail("yayota1045@naymio.com")
                .pause()
                .submitFurther()
                .pause()
                .setUpPassword("1234567")
                .pause()
                .submitComeIn()
                .pause()
                .callBookSlugPage()
                .pause()
                .setUpTextReview(text)
                .pause()
                .submitAddReview()
                .pause()
                .callBookSlugPage();
        assertThat(driver.findElementByXPath("/html/body/div/div/main/div/div[3]").getText()).contains(text);

    }
}