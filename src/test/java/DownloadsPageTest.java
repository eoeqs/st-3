import io.github.bonigarcia.wdm.WebDriverManager;
import org.eoeqs.DownloadsPage;
import org.eoeqs.MainPage;
import org.eoeqs.MenuTools;
import org.eoeqs.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DownloadsPageTest {
    private WebDriver driver;
    private DownloadsPage downloadsPage;
    private MenuTools menuTools;
    private MainPage mainPage;

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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void openBaseUrl() {
        driver.get(TestConstants.BASE_URL);
        mainPage = new MainPage(driver);
        menuTools = new MenuTools(driver);
        downloadsPage = new DownloadsPage(driver);
    }

    private void waitForPageLoad(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleIs(expectedTitle));
        assertEquals(expectedTitle, driver.getTitle(), "Page title does not match expected: " + expectedTitle);
    }

    private void uploadTestImages() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromLinkLink()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mainPage.getFromLinkLink());
        mainPage.getLinksInput().sendKeys(String.join("\n", TestConstants.TEST_IMAGE_LINKS));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mainPage.getSubmitButton());
        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//img[@border=0]"), 0));
        } catch (TimeoutException e) {
            throw new RuntimeException("Failed to upload images. Check links or website response.", e);
        }
    }

    private void navigateToDownloadsPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement link = driver.findElement(By.xpath("(//div[@class='menuitem'])[5]"));
                wait.until(ExpectedConditions.elementToBeClickable(link));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                waitForPageLoad(TestConstants.MY_LOADING_TITLE);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == 3) throw e;
            }
        }
    }

    private int countSelectedImages(List<WebElement> checkboxes) {
        return (int) checkboxes.stream().filter(WebElement::isSelected).count();
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testSelectAllLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        uploadTestImages();
        navigateToDownloadsPage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getSelectAllLink()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadsPage.getSelectAllLink());
        assertEquals(downloadsPage.getImages().size(), countSelectedImages(downloadsPage.getCheckboxes()),
                "Number of selected images does not match total images");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testDeselectAllLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        uploadTestImages();
        navigateToDownloadsPage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getSelectAllLink()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadsPage.getSelectAllLink());
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getDeselectAllLink()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadsPage.getDeselectAllLink());
        assertEquals(0, countSelectedImages(downloadsPage.getCheckboxes()),
                "Some images are still selected after deselecting all");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testDeleteOneImage(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        uploadTestImages();
        navigateToDownloadsPage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> checkboxes = downloadsPage.getCheckboxes();
        if (checkboxes.size() < 2) {
            throw new AssertionError("Not enough images uploaded: " + checkboxes.size());
        }
        int initialThumbCount = driver.findElements(By.xpath("//div[@class='thumb']")).size();
        wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(1)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkboxes.get(1));
        if (!checkboxes.get(1).isSelected()) {
            throw new RuntimeException("Failed to select checkbox");
        }
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getDeleteSelectedLink()));
        WebElement deleteLink = downloadsPage.getDeleteSelectedLink();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteLink);
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("Timed out waiting for alert to be present");
        }
        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.xpath("//div[@class='thumb']"), initialThumbCount - 1));
        int finalThumbCount = downloadsPage.getImages().size();
        assertEquals(2, finalThumbCount, "Image count after deleting one image is incorrect");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testDeleteAllImages(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        uploadTestImages();
        navigateToDownloadsPage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getSelectAllLink()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadsPage.getSelectAllLink());
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getDeleteSelectedLink()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadsPage.getDeleteSelectedLink());
        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.xpath("//div[@class='thumb']"), 0));
        assertEquals(0, downloadsPage.getImages().size(),
                "Images remain after deleting all");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testDeleteTwoImages(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        uploadTestImages();
        navigateToDownloadsPage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> checkboxes = downloadsPage.getCheckboxes();
        if (checkboxes.size() < 3) {
            throw new AssertionError("Not enough images uploaded: " + checkboxes.size());
        }
        wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(0)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkboxes.get(0));
        wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(2)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkboxes.get(2));
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getDeleteSelectedLink()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadsPage.getDeleteSelectedLink());
        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.xpath("//div[@class='thumb']"), 1));
        assertEquals(1, downloadsPage.getImages().size(),
                "Image count after deleting two images is incorrect");
    }
}