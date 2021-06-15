package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NavigateSectionsOfStoreTests {

    private static ChromeDriver driver;

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
    public void testGenresPageAccess() throws InterruptedException {
        NavigateSectionsOfStore navigateSectionsOfStore = new NavigateSectionsOfStore(driver);
        navigateSectionsOfStore
                .callMainPage()
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[2]/a")
                .pause();

        assertTrue(driver.findElementByXPath("/html/body/div/div/main/div/div/div[1]/div[1]/div/a")
                .getText()
                .contains("Лёгкое чтение"));
    }

    @Test
    public void testRecentPageAccess() throws InterruptedException {
        NavigateSectionsOfStore navigateSectionsOfStore = new NavigateSectionsOfStore(driver);
        navigateSectionsOfStore
                .callMainPage()
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[3]/a")
                .pause();
        assertTrue(driver.findElementByXPath("/html/body/div/div/main/ul/li[3]/span").getText().contains("Новинки"));
    }

    @Test
    public void testPopularPageAccess() throws InterruptedException {
        NavigateSectionsOfStore navigateSectionsOfStore = new NavigateSectionsOfStore(driver);
        navigateSectionsOfStore
                .callMainPage()
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[4]/a")
                .pause();
        assertEquals("Популярное",
                driver.findElementByXPath("/html/body/div/div/main/ul/li[3]/span").getText());
    }

    @Test
    public void testAuthorsPageAccess() throws InterruptedException {
        NavigateSectionsOfStore navigateSectionsOfStore = new NavigateSectionsOfStore(driver);
        navigateSectionsOfStore
                .callMainPage()
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[5]/a")
                .pause();
        assertEquals("Birkby Skippie",
                driver.findElementByXPath("/html/body/div/div/main/div/div/div[2]/div/div[1]/a").getText());
    }

    @Test
    public void testAllPageAccess() throws InterruptedException {
        NavigateSectionsOfStore navigateSectionsOfStore = new NavigateSectionsOfStore(driver);
        navigateSectionsOfStore
                .callMainPage()
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[2]/a")
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[3]/a")
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[4]/a")
                .pause()
                .nextPage("//*[@id=\"navigate\"]/ul/li[5]/a")
                .pause();
    }
}