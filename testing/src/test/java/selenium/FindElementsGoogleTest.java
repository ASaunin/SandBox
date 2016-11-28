package selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.WebDriverFactory.BrowserType;
import utils.ThreadUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static selenium.WebDriverFactory.BrowserType.CHROME;

public class FindElementsGoogleTest {

    // TODO: 28.11.2016 Implement runner for each browser type

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
        ThreadUtils.sleep(100);
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
        final WebElement element = driver.findElement(By.linkText("\u041f\u043e\u0447\u0442\u0430"));
        final String elementRef = element.getAttribute("href");
        final String expectedText = "mail.google.com";
        assertThat(elementRef, containsString(expectedText));
    }

    @Test
    public void findElementByPartialLink() throws Exception {
        final WebElement element = driver.findElement(By.partialLinkText("\u0431\u0438\u0437\u043d\u0435\u0441"));
        final String elementRef = element.getAttribute("href");
        final String expectedText = "www.google.ru/services";
        assertThat(elementRef, containsString(expectedText));
    }

    @Test
    public void findElementByUniqueClassName() throws Exception {
        final WebElement element = driver.findElement(By.className("sbib_b"));
        final String elementId = element.getAttribute("id");
        assertThat(elementId, equalTo("sb_ifc0"));
    }

    @Test
    public void findSeveralElementsByClassName() throws Exception {
        final List<WebElement> elements = driver.findElements(By.className("_Gs"));
        final WebElement element = driver.findElement(By.linkText("\u0420\u0435\u043a\u043b\u0430\u043c\u0430"));
        assertThat(elements, hasItem(element));
    }

    @Test
    public void findSeveralElementsByTagName() throws Exception {
        final List<WebElement> elements = driver.findElements(By.tagName("a"));
        final WebElement element = driver.findElement(By.linkText("\u0420\u0435\u043a\u043b\u0430\u043c\u0430"));
        assertThat(elements, hasItem(element));
    }

    @AfterClass
    public static void finalizeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

}
