package org.example;

import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.*;
import java.io.*;
import java.util.List;
import java.util.Random;


public class BaseSteps extends Methods {
    String searchBar = "//input[@placeholder='Ürün, Marka Arayın']";

    @Step("Wait <value> seconds")
    public void waitSeconds(int seconds) throws InterruptedException {
        if(seconds >= 5) logger.info("Waiting " + seconds + " Seconds");
        Thread.sleep(seconds * 1000L);
    }

    @Step("Log This <Log> Text")
    public void loggerMethod(String Log) throws InterruptedException {
        logger.info(Log);
    }

    @Step("Check element text <elementName> with type <elementType>")
    public void checkElement(String elementName, String elementType) {
        String elementPath = "//"+elementType+"[normalize-space(text())='" + elementName + "']";
        logger.info("Path = "+ elementPath);
        try {
            WebElement element = getWebDriver().findElement(By.xpath(elementPath));
            Assert.assertTrue(element.isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element '" + elementName + "' not found on the page.");
        }
    }

    @Step("Click element text <elementName> with type <elementType>")
    public void clickElement(String elementName, String elementType) {

        String elementPath = "//"+elementType+"[normalize-space(text())='" + elementName + "']";
        logger.info("Path = "+ elementPath);
        try {
            WebElement element = getWebDriver().findElement(By.xpath(elementPath));
            if (element.isDisplayed()) {
                element.click();
            } else {
                Assert.fail("Element '" + elementName + "' not displayed on the page.");
            }
        } catch (NoSuchElementException e) {
            Assert.fail("Element '" + elementName + "' not found on the page.");
        }
    }

    @Step("Get Excel Value Row <rowNumber> Col <colNumber> and Search <search>")
    public void searchWithExcelValue(int rowNumber, int colNumber , boolean search) throws InterruptedException, IOException {
        ExcelReader excelReader = new ExcelReader("src/resource/excelTable1.xlsx");
        String searchValue = excelReader.getValue("Database", rowNumber -1, colNumber -1);
        logger.info("Returned Excel Value = " + searchValue);

        excelReader.close();

        WebElement searchBox = null;
        try {
            searchBox = findElementByXpath(searchBar);
        } catch (Exception e) {
            logger.severe("Search box element not found.");
        }

        if (searchBox != null) {
            searchBox.sendKeys(searchValue);
            if(search) searchBox.sendKeys(Keys.ENTER);
        } else {
            logger.severe("Search box element is null.");
        }
    }

    @Step("Select a random product, write its information to a text file, add it to cart with a random size")
    public void selectProductAndAddToCart() throws IOException, InterruptedException {
        String cardPath = "//div[contains(@class, 'o-productList__itemWrapper')]" ;
        List<WebElement> productCards = getWebDriver().findElements(By.xpath(cardPath));

        // Select a random product card
        if (productCards.isEmpty()) {
            logger.warning("No product cards found on the page.");
            return;
        }

        Random rand = new Random();
        int randomIndex = rand.nextInt(productCards.size());
        WebElement randomProductCard = productCards.get(randomIndex);

        // Extract the product information

        String brand = randomProductCard.findElement(By.xpath(".//span[@class='m-productCard__title']"))
                .getText();
        String productName = randomProductCard.findElement(By.xpath(".//h3[@class='m-productCard__desc']"))
                .getText();
        String price = randomProductCard.findElement(By.xpath(".//span[@class='m-productCard__newPrice']"))
                .getText();

        logger.info("Product Brand " + brand + "Product Name " + productName + "Product Price " + price);

        // Write the information to a text file
        File file = new File("randomProd.txt");
        FileWriter writer = new FileWriter(file);
        writer.write("Price: " + price + "\n");
        writer.write("Brand: " + brand + "\n");
        writer.write("Product Name: " + productName + "\n");
        writer.close();

        // Click the product card to open the modal
        randomProductCard.click();

        // Select a random size
        String sizePath = "//div[@id='sizes']//span[contains(@class, 'm-variation__item') and not(contains(@class, '-disabled'))]";

        List<WebElement> sizeOptions = getWebDriver().findElements(By.xpath(sizePath));

        WebElement randomSizeOption = sizeOptions.get(rand.nextInt(sizeOptions.size()));
        String selectedSize = randomSizeOption.getText();
        logger.info(selectedSize);


        // Select the selected size from the modal and add the product to cart
        WebElement sizeOption = findElementByXpath("//div[@data-variation-size='']//span[text()='" + selectedSize + "']");
        sizeOption.click();

        WebElement addToCartButton = findElementByXpath("//button[@id='addBasket']");
        addToCartButton.click();

        waitSeconds(5);
    }

    @Step("Verify product price from file is same as on website")
    public void verifyProductPrice() throws IOException {
        String pricePath = "//span[text()='Ödenecek Tutar']/..//span[@class='m-orderSummary__value']";
        int expectedPrice = getPriceFromTxt();
        WebElement priceElement = findElementByXpath(pricePath);

        int actualPrice = Integer.parseInt(priceElement.getText().replaceAll("[^0-9]", ""));
        logger.info("Product Price = " + actualPrice + "/n" +
                          "Expected Price = " + expectedPrice + "/n" );

        Assert.assertEquals(expectedPrice, actualPrice);
    }

    @Step("Change product quantity to 2 and Check")
    public void changeQuantity() throws IOException, InterruptedException {
        WebElement dropdown = findElementByXpath("//*[@id='quantitySelect0-key-0']");
        String pricePath = "//span[text()='Toplam Tutar (KDV Dahil)']/..//span[@class='m-orderSummary__value']";
        dropdown.sendKeys("2");
        dropdown.sendKeys(Keys.ENTER);
        waitSeconds(4);

        WebElement priceElement = findElementByXpath(pricePath);

        int expectedPrice = getPriceFromTxt();
        int actualPrice = Integer.parseInt(priceElement.getText().replaceAll("[^0-9]", ""));
        Assert.assertEquals(expectedPrice*2, actualPrice);
    }

}
