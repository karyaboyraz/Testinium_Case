package driver;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebDriver;

import java.util.logging.Logger;

public class Driver {
    private static WebDriver webDriver;
    private final String baseUrl = "https://www.beymen.com";
    public Logger logger = Logger.getLogger(String.valueOf(Driver.class));

    public WebDriver getWebDriver() {
        return webDriver;
    }

    @BeforeScenario
    public void start() {
        webDriver = DriverFactory.createWebDriver();
    }

    @AfterScenario
    public void stop() throws InterruptedException {
        if (webDriver != null) {
            Thread.sleep( 3000L);
            webDriver.quit();

        }
    }

    @Step("Open the Login Page")
    public void openLoginPage()  {
        webDriver.manage().window().maximize();
        webDriver.get(baseUrl);
        try {
            Thread.sleep( 4000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
