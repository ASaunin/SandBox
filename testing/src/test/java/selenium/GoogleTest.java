package selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.WebDriverFactory.BrowserType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static selenium.WebDriverFactory.BrowserType.*;

public class GoogleTest {

    private static final BrowserType type = CHROME;
    private WebDriver driver;

    @Before
    public void initDriver() {
        driver = WebDriverFactory.getDriver(type);
    }

    @Test
    public void searchReturnsValue() throws Exception {
        final String url = "http://google.com";
        driver.get(url);

        final WebElement searchElement = driver.findElement(By.name("q"));
        assertThat(searchElement.getAttribute("id"), is(equalTo("lst-ib")));
    }

    @After
    public void finalizeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

}
