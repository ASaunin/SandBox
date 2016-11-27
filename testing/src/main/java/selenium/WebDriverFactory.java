package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverFactory {

    public enum BrowserType {

        FIREFOX {
            @Override
            public WebDriver getDriver() {
                System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
                return new FirefoxDriver();
            }
        },

        CHROME {
            @Override
            public WebDriver getDriver() {
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
                return new ChromeDriver();
            }
        };

        public abstract WebDriver getDriver();

    }

    public static WebDriver getDriver(BrowserType type) {
        return type.getDriver();
    }

}
