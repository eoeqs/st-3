
import io.github.bonigarcia.wdm.WebDriverManager;
import org.eoeqs.TestConstants;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.eoeqs.MenuTools;
import java.time.Duration;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class MenuToolsTest {
    private WebDriver driver;
    private MenuTools menuTools;

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static Stream<String> browserProvider() {
        return Stream.of("Chrome", "Firefox");
    }

    private void setupBrowser(String browser) {
        if (browser.equals("Chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equals("Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        driver.manage().window().maximize();
    }

    private void openBaseUrl() {
        driver.get(TestConstants.BASE_URL);
        menuTools = new MenuTools(driver);
    }

    private void waitForPageLoad(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.titleIs(expectedTitle));
        assertEquals(expectedTitle, driver.getTitle(), "Page title does not match expected: " + expectedTitle);
    }

    private void clickAndVerifyUrl(WebElement element, String expectedUrl) {
        element.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        assertEquals(expectedUrl, driver.getCurrentUrl(), "URL does not match expected: " + expectedUrl);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    @DisplayName("Verify main page loads")
    public void testMainPage(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    @DisplayName("Verify 'Main' link")
    public void testMainLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        clickAndVerifyUrl(menuTools.getMainLink(), TestConstants.BASE_URL);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    @DisplayName("Verify 'Rules' link")
    public void testRulesLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        clickAndVerifyUrl(menuTools.getRulesLink(), TestConstants.RULES_URL);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    @DisplayName("Verify 'FAQ' link")
    public void testFaqLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        clickAndVerifyUrl(menuTools.getFaqLink(), TestConstants.FAQ_URL);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    @DisplayName("Verify 'Uploader' link")
    public void testUploaderLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        clickAndVerifyUrl(menuTools.getUploaderLink(), TestConstants.UPLOADER_URL);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    @DisplayName("Verify 'My Loading' link")
    public void testMyLoadingLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        clickAndVerifyUrl(menuTools.getMyLoadingLink(), TestConstants.MY_LOADING_URL);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    @DisplayName("Verify 'Donate' link")
    public void testDonateLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        clickAndVerifyUrl(menuTools.getDonateLink(), TestConstants.DONATE_URL);
    }
}