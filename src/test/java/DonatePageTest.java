
import io.github.bonigarcia.wdm.WebDriverManager;
import org.eoeqs.DonatePage;
import org.eoeqs.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DonatePageTest {
    private WebDriver driver;
    private DonatePage donatePage;

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
        driver.get(TestConstants.DONATE_URL);
        donatePage = new DonatePage(driver);
    }

    private void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.titleIs(TestConstants.DONATE_TITLE));
        assertEquals(TestConstants.DONATE_TITLE, driver.getTitle(), "Page title does not match expected: " + TestConstants.DONATE_TITLE);
    }

    private void clickAndVerifyClipboard(WebElement button, String expectedWallet) throws IOException, UnsupportedFlavorException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
        String clipboardData = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .getData(DataFlavor.stringFlavor);
        assertEquals(expectedWallet, clipboardData, "Clipboard content does not match expected wallet: " + expectedWallet);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testCopyButtons(String browser) throws IOException, UnsupportedFlavorException {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad();
        for (int i = 0; i < donatePage.getCopyButtons().size(); i++) {
            clickAndVerifyClipboard(donatePage.getCopyButtons().get(i), TestConstants.WALLET_ADDRESSES[i]);
        }
    }
}