package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ReviewBlock {

    private ChromeDriver driver;


    private String urlBookSlug = "http://localhost:8080/books/book-pnw-522";
    private String urlMyPage = "http://localhost:8080/my";

    public ReviewBlock(ChromeDriver driver) {
        this.driver = driver;
    }

    public ReviewBlock callBookSlugPage() {
        driver.get(urlBookSlug);
        return this;
    }

    public ReviewBlock callMyPage() {
        driver.get(urlMyPage);
        return this;
    }

    public ReviewBlock pause() throws InterruptedException {
        Thread.sleep(2000);
        return this;
    }

    public ReviewBlock setUpTextReview(String text) {
        WebElement element = driver.findElement(By.id("review"));
        element.clear();
        element.sendKeys(text);
        return this;
    }

    public ReviewBlock submitAddReview() {
        WebElement element = driver.findElementByXPath("/html/body/div/div/main/div/div[3]/div[1]/form/div[2]/button");
        element.submit();
        return this;
    }

    public ReviewBlock chooseRadioButtonEmail() {
        driver.findElement(By.id("choseEmail")).click();
        return this;
    }

    public ReviewBlock setUpEmail(String email) {
        WebElement element = driver.findElement(By.id("mail"));
        element.sendKeys(email);
        return this;
    }

    public ReviewBlock submitFurther() {
        driver.findElement(By.id("sendauth")).click();
        return this;
    }

    public ReviewBlock setUpPassword(String password) {
        WebElement element = driver.findElement(By.id("mailcode"));
        element.sendKeys(password);
        return this;
    }

    public ReviewBlock submitComeIn() {
        driver.findElement(By.id("toComeInMail")).click();
        return this;
    }
}
