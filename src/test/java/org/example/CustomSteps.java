package org.example;

import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class CustomSteps extends Methods{
    String searchBar = "//input[@placeholder='Ürün, Marka Arayın']";

    @Step("Go to Cart")
    public void goToCart() {
        String elementPath = "//a[normalize-space(@title)=\"Sepetim\"]";

        try {
            WebElement element = getWebDriver().findElement(By.xpath(elementPath));
            if (element.isDisplayed()) {
                element.click();
            } else {
                Assert.fail("Cart not displayed on the page.");
            }
        } catch (NoSuchElementException e) {
            Assert.fail("Cart not found on the page.");
        }
    }

    @Step("Cookie Accept")
    public void cookieAccept() {
        String elementPath = "//button[text()=\"Tüm Çerezleri Kabul Et\"][@id=\"onetrust-accept-btn-handler\"]";
        WebElement element = findElementByXpath(elementPath);

        element.click();
    }

    @Step("Clear Search Bar")
    public void clearSearchBar() {
        WebElement searchBox = findElementByXpath(searchBar);
        webElement.sendKeys(Keys.CONTROL + "a");
        webElement.sendKeys(Keys.DELETE);
    }
}
