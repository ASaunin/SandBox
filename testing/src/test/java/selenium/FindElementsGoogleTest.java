package selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.WebDriverFactory.BrowserType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static selenium.WebDriverFactory.BrowserType.CHROME;

public class FindElementsGoogleTest {

    private static final BrowserType type = CHROME;
    private static final String GOOGLE = "google";
    private static final String GOOGLE_URL = "http://google.ru";
    private static WebDriver driver;

    @BeforeClass
    public static void initDriver() throws Exception {
        driver = WebDriverFactory.getDriver(type);
    }

    @Before
    public void initPage() {
        driver.get(GOOGLE_URL);
    }

    @Test
    public void checkPageTitle() throws Exception {
        assertThat(driver.getTitle(), equalToIgnoringCase(GOOGLE));
    }

    @Test
    public void findElementById() throws Exception {
        final WebElement element = driver.findElement(By.id("lst-ib"));
        final String elementName = element.getAttribute("name");
        assertThat(elementName, equalTo("q"));
    }

    @Test
    public void findElementByName() throws Exception {
        final WebElement element = driver.findElement(By.name("q"));
        final String elementId = element.getAttribute("id");
        assertThat(elementId, equalTo("lst-ib"));
    }

    @Test
    public void findElementByXpath() throws Exception {
        final WebElement element = driver.findElement(By.xpath("//*[@id=\"lst-ib\"]"));
        final String elementId = element.getAttribute("id");
        assertThat(elementId, equalTo("lst-ib"));
    }

    @Test
    public void findElementByCssSelector() throws Exception {
        final WebElement element = driver.findElement(By.cssSelector("#lst-ib"));
        final String elementId = element.getAttribute("id");
        assertThat(elementId, equalTo("lst-ib"));
    }

    @Test
    public void findElementByLink() throws Exception {
        final WebElement element = driver.findElement(By.linkText("Почта"));
        final String elementRef = element.getAttribute("href");
        final String expectedText = "mail.".concat(GOOGLE).concat(".com");
        assertThat(elementRef, containsString(expectedText));
    }

    @AfterClass
    public static void finalizeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

}
