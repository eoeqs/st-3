

import io.github.bonigarcia.wdm.WebDriverManager;

import org.eoeqs.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.*;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;
    private MenuTools menuTools;
    private DownloadsPage downloadsPage;
    private PicPage picPage;

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
        mainPage = new MainPage(driver);
        menuTools = new MenuTools(driver);
        downloadsPage = new DownloadsPage(driver);
        picPage = new PicPage(driver);
    }

    private void waitForPageLoad(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.titleIs(expectedTitle));
        assertEquals(expectedTitle, driver.getTitle(), "Page title does not match expected: " + expectedTitle);
    }

    private void navigateToDownloadsPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(menuTools.getMyLoadingLink()));
        menuTools.getMyLoadingLink().click();
        waitForPageLoad(TestConstants.MY_LOADING_TITLE);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddImageFromComputer(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromComputerLink()));
        mainPage.getFromComputerLink().click();
        File file = new File(TestConstants.LOCAL_IMAGE_PATHS[0]);
        if (!file.exists()) {
            fail("Image file not found: " + file.getAbsolutePath());
        }
        mainPage.getFileInput().sendKeys(file.getAbsolutePath());
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        wait.until(ExpectedConditions.visibilityOf(mainPage.getFirstImageName()));
        assertEquals("1.png", mainPage.getFirstImageName().getText(),
                "First image name does not match expected");
        assertNotNull(driver.findElement(By.xpath("//img[@border=0]")),
                "Uploaded image not found on page");
        navigateToDownloadsPage();
        assertEquals(1, downloadsPage.getImages().size(),
                "Expected one image in downloads page");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddThreeImagesFromComputer(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromComputerLink()));
        mainPage.getFromComputerLink().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getAddFieldLink()));
        mainPage.getAddFieldLink().click();
        mainPage.getAddFieldLink().click();
        File file1 = new File(TestConstants.LOCAL_IMAGE_PATHS[0]);
        File file2 = new File(TestConstants.LOCAL_IMAGE_PATHS[1]);
        File file3 = new File(TestConstants.LOCAL_IMAGE_PATHS[2]);
        if (!file1.exists() || !file2.exists() || !file3.exists()) {
            fail("One or more image files not found");
        }
        mainPage.getFileInput().sendKeys(file1.getAbsolutePath());
        mainPage.getFile2Input().sendKeys(file2.getAbsolutePath());
        mainPage.getFile3Input().sendKeys(file3.getAbsolutePath());
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        wait.until(ExpectedConditions.visibilityOf(mainPage.getFirstImageName()));
        assertEquals("1.png", mainPage.getFirstImageName().getText(),
                "First image name does not match expected");
        assertEquals("2.png", mainPage.getSecondImageName().getText(),
                "Second image name does not match expected");
        assertEquals("3.png", mainPage.getThirdImageName().getText(),
                "Third image name does not match expected");
        List<WebElement> images = driver.findElements(By.xpath("//img[@border=0]"));
        assertEquals(3, images.size(), "Expected three images on page");
        navigateToDownloadsPage();
        assertEquals(3, downloadsPage.getImages().size(),
                "Expected three images in downloads page");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddImageFromLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromLinkLink()));
        mainPage.getFromLinkLink().click();
        mainPage.getLinksInput().sendKeys(TestConstants.TEST_IMAGE_LINKS[0]);
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        wait.until(ExpectedConditions.visibilityOf(mainPage.getFirstImageName()));
        assertEquals(TestConstants.TEST_IMAGE_LINKS[0], mainPage.getFirstImageName().getText(),
                "First image name does not match expected");
        assertNotNull(driver.findElement(By.xpath("//img[@border=0]")),
                "Uploaded image not found on page");
        navigateToDownloadsPage();
        assertEquals(1, downloadsPage.getImages().size(),
                "Expected one image in downloads page");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddThreeImagesFromLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> thumbs = driver.findElements(By.xpath("//div[@id='mypics']//div[@class='thumb']"));

        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromLinkLink()));
        mainPage.getFromLinkLink().click();

        String inputText = String.join("\n", TestConstants.TEST_IMAGE_LINKS);
        mainPage.getLinksInput().sendKeys(inputText);
        String textareaValue = mainPage.getLinksInput().getAttribute("value");
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//img[@border=0]")));

        List<WebElement> images = driver.findElements(By.xpath("//img[@border=0]"));
        Assertions.assertEquals(3, images.size(), "Expected three images on main page");

        navigateToDownloadsPage();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//div[@id='mypics']//div[@class='thumb']"), 3));
        thumbs = driver.findElements(By.xpath("//div[@id='mypics']//div[@class='thumb']"));
        Assertions.assertEquals(3, thumbs.size(), "Expected three images in Downloads page");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddImageFromComputerWithResize(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromComputerLink()));
        mainPage.getFromComputerLink().click();

        String imagePath = "images/test.jpg";
        File file = new File(imagePath);
        Assertions.assertTrue(file.exists(), "Image file not found: " + file.getAbsolutePath());
        System.out.println("Uploading file: " + file.getAbsolutePath());
        mainPage.getFileInput().sendKeys(file.getAbsolutePath());

        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getResizeCheckbox()));
        mainPage.getResizeCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getResizeSelect()));
        mainPage.getResizeSelect().findElement(By.xpath("//option[text()='500 (стандарт)']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();

        wait.until(ExpectedConditions.visibilityOf(mainPage.getFirstImageName()));
        String imageName = mainPage.getFirstImageName().getText();
        System.out.println("Displayed image name: " + imageName);
        Assertions.assertEquals("test.jpg", imageName, "First image name does not match expected");
        Assertions.assertNotNull(driver.findElement(By.xpath("//img[@border=0]")), "Uploaded image not found on page");

        wait.until(ExpectedConditions.elementToBeClickable(menuTools.getMyLoadingLink()));
        menuTools.getMyLoadingLink().click();

        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getImages().get(0)));
        downloadsPage.getImages().get(0).click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        String originalWindow = driver.getWindowHandle();
        List<String> windowHandles = List.copyOf(driver.getWindowHandles());
        for (String window : windowHandles) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        WebElement resolutionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@class='resolution text-white-50 m-2'])[1]")));
        String resolutionText = resolutionElement.getText();
        System.out.println("Resolution text: " + resolutionText);
        String[] resolution = resolutionText.split(" ")[1].split("x");
        int width = Integer.parseInt(resolution[0]);
        int height = Integer.parseInt(resolution[1]);
        System.out.println("Parsed width: " + width + ", height: " + height);
        Assertions.assertTrue(width == 500 || height == 500,
                "Image resolution does not have width 500px or height 500px: width=" + width + ", height=" + height);

    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddImageFromComputerWithRotate(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromComputerLink()));
        mainPage.getFromComputerLink().click();
        File file = new File(TestConstants.LOCAL_IMAGE_PATHS[0]);
        if (!file.exists()) {
            fail("Image file not found: " + file.getAbsolutePath());
        }
        int initialWidth = 0, initialHeight = 0;
        try {
            BufferedImage image = ImageIO.read(file);
            initialWidth = image.getWidth();
            initialHeight = image.getHeight();
        } catch (IOException e) {
            fail("Failed to load image file: " + e.getMessage());
        }
        mainPage.getFileInput().sendKeys(file.getAbsolutePath());
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getRotateCheckbox()));
        mainPage.getRotateCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getRotateSelect()));
        mainPage.getRotateSelect().findElement(By.xpath(".//option[text()='90° против часовой']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        wait.until(ExpectedConditions.visibilityOf(mainPage.getFirstImageName()));
        assertEquals("1.png", mainPage.getFirstImageName().getText(),
                "First image name does not match expected");
        assertNotNull(driver.findElement(By.xpath("//img[@border=0]")),
                "Uploaded image not found on page");
        navigateToDownloadsPage();
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getImages().get(0)));
        downloadsPage.getImages().get(0).click();
        String originalWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String window : driver.getWindowHandles()) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOf(picPage.getResolution()));
        String[] resolution = picPage.getResolution().getText().split(" ")[1].split("x");
        int width = Integer.parseInt(resolution[0]);
        int height = Integer.parseInt(resolution[1]);
        assertEquals(initialHeight, width, "Width does not match expected after rotation");
        assertEquals(initialWidth, height, "Height does not match expected after rotation");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddImageFromLinkWithResize(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromLinkLink()));
        mainPage.getFromLinkLink().click();
        mainPage.getLinksInput().sendKeys(TestConstants.TEST_IMAGE_LINKS[0]);
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getResizeCheckbox()));
        mainPage.getResizeCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getResizeSelect()));
        mainPage.getResizeSelect().findElement(By.xpath(".//option[text()='500 (стандарт)']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        wait.until(ExpectedConditions.visibilityOf(mainPage.getFirstImageName()));
        assertEquals(TestConstants.TEST_IMAGE_LINKS[0], mainPage.getFirstImageName().getText(),
                "First image name does not match expected");
        assertNotNull(driver.findElement(By.xpath("//img[@border=0]")),
                "Uploaded image not found on page");
        navigateToDownloadsPage();
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getImages().get(0)));
        downloadsPage.getImages().get(0).click();
        String originalWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String window : driver.getWindowHandles()) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOf(picPage.getResolution()));
        String[] resolution = picPage.getResolution().getText().split(" ")[1].split("x");
        int width = Integer.parseInt(resolution[0]);
        int height = Integer.parseInt(resolution[1]);
        assertTrue(width == 500 || height == 500,
                "Image resolution does not have width 500px or height 500px");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddImageFromLinkWithRotate(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromLinkLink()));
        mainPage.getFromLinkLink().click();
        mainPage.getLinksInput().sendKeys(TestConstants.TEST_IMAGE_LINKS[0]);
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getRotateCheckbox()));
        mainPage.getRotateCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getRotateSelect()));
        mainPage.getRotateSelect().findElement(By.xpath(".//option[text()='90° против часовой']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        wait.until(ExpectedConditions.visibilityOf(mainPage.getFirstImageName()));
        assertEquals(TestConstants.TEST_IMAGE_LINKS[0], mainPage.getFirstImageName().getText(),
                "First image name does not match expected");
        assertNotNull(driver.findElement(By.xpath("//img[@border=0]")),
                "Uploaded image not found on page");
        navigateToDownloadsPage();
        wait.until(ExpectedConditions.elementToBeClickable(downloadsPage.getImages().get(0)));
        downloadsPage.getImages().get(0).click();
        String originalWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String window : driver.getWindowHandles()) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOf(picPage.getResolution()));
        String[] resolution = picPage.getResolution().getText().split(" ")[1].split("x");
        int width = Integer.parseInt(resolution[0]);
        int height = Integer.parseInt(resolution[1]);
        assertEquals(735, width, "Width does not match expected after rotation");
        assertEquals(623, height, "Height does not match expected after rotation");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddingTenFieldsTriggersAlert(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromComputerLink()));
        mainPage.getFromComputerLink().click();
        for (int i = 0; i < 10; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(mainPage.getAddFieldLink()));
            mainPage.getAddFieldLink().click();
        }
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertEquals(TestConstants.ALERT_MESSAGE, alert.getText(),
                "Alert message does not match expected");
        alert.accept();
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testHelpServiceLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getHelpServiceLink()));
        mainPage.getHelpServiceLink().click();
        wait.until(ExpectedConditions.urlToBe(TestConstants.DONATE_URL));
        assertEquals(TestConstants.DONATE_URL, driver.getCurrentUrl(),
                "URL does not match expected: " + TestConstants.DONATE_URL);
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testRestrictionsLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getRestrictionsLink()));
        mainPage.getRestrictionsLink().click();
        wait.until(ExpectedConditions.visibilityOf(mainPage.getRestrictionsText()));
        assertTrue(mainPage.getRestrictionsText().isDisplayed(),
                "Restrictions text is not visible");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testDisableAllEffectsLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getResizeCheckbox()));
        mainPage.getResizeCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getRotateCheckbox()));
        mainPage.getRotateCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getOptimizationCheckbox()));
        mainPage.getOptimizationCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getPosterCheckbox()));
        mainPage.getPosterCheckbox().click();
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getDisableAllEffectsLink()));
        mainPage.getDisableAllEffectsLink().click();
        assertFalse(mainPage.getResizeCheckbox().isSelected(), "Resize checkbox is still selected");
        assertFalse(mainPage.getRotateCheckbox().isSelected(), "Rotate checkbox is still selected");
        assertFalse(mainPage.getOptimizationCheckbox().isSelected(), "Optimization checkbox is still selected");
        assertFalse(mainPage.getPosterCheckbox().isSelected(), "Poster checkbox is still selected");
    }

    @ParameterizedTest
    @MethodSource("browserProvider")
    public void testAddImageFromInvalidLink(String browser) {
        setupBrowser(browser);
        openBaseUrl();
        waitForPageLoad(TestConstants.MAIN_TITLE);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFromLinkLink()));
        mainPage.getFromLinkLink().click();
        mainPage.getLinksInput().sendKeys(TestConstants.INVALID_IMAGE_LINK);
        wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSubmitButton()));
        mainPage.getSubmitButton().click();
        WebElement error = driver.findElement(By.xpath("//div[contains(text(), 'В запросе')]"));
        wait.until(ExpectedConditions.visibilityOf(error));
        assertTrue(error.isDisplayed(), "Error message is not visible");
    }
}