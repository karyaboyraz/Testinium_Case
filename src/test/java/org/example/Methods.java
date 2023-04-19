package org.example;
import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public class Methods extends Driver {
    Logger logger = Logger.getLogger(String.valueOf(Methods.class));
    WebDriverWait webDriverWait;
    WebElement webElement;

    public Methods() {
        WebDriver getWebDriver = getWebDriver();
        this.webDriverWait = new WebDriverWait(getWebDriver, Duration.ofSeconds(10));
    }

    public WebElement findElementByXpath(String xpath) {
        try {
            webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        } catch (Exception e) {
            logger.info(xpath + " not found!");
        }
        return webElement;
    }

    public int getPriceFromTxt() throws IOException {
        File file = new File("randomProd.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String priceString = reader.readLine().split(": ")[1];
        reader.close();

        String[] parts = priceString.split(" ");
        String price = parts[0].replaceAll("[^0-9]", "");

        return Integer.parseInt(price);
    }
}