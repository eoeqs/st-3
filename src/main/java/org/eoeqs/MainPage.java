package org.eoeqs;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage {
    @FindBy(xpath = "(//a[@href = '/donate'])[2]")
    private WebElement helpServiceLink;

    @FindBy(xpath = "//a[text()='[ добавить поле ]'][@class='jslink']")
    private WebElement addFieldLink;

    @FindBy(xpath = "//a[text()='[ с компьютера ]'][@class='jslink']")
    private WebElement fromComputerLink;

    @FindBy(xpath = "//a[text()='[ по ссылке ]'][@class='jslink']")
    private WebElement fromLinkLink;

    @FindBy(xpath = "//a[@class='jslink']//b[text()='[ посмотреть ограничения ]']")
    private WebElement restrictionsLink;

    @FindBy(xpath = "(//div[@class='center rounded-corners']//p)[3]")
    private WebElement restrictionsText;

    @FindBy(xpath = "//a[text() = '[ отключить все эффекты ]'][@class='jslink']")
    private WebElement disableAllEffectsLink;

    @FindBy(xpath = "//input[@class='upfile']")
    private WebElement fileInput;

    @FindBy(xpath = "//input[@name='file2']")
    private WebElement file2Input;

    @FindBy(xpath = "//input[@name='file3']")
    private WebElement file3Input;

    @FindBy(xpath = "//input[@name='check_orig_resize']")
    private WebElement resizeCheckbox;

    @FindBy(xpath = "//select[@class='lmar_28']")
    private WebElement resizeSelect;

    @FindBy(xpath = "//input[@name='check_orig_rotate']")
    private WebElement rotateCheckbox;

    @FindBy(xpath = "//select[@name='orig_rotate']")
    private WebElement rotateSelect;

    @FindBy(xpath = "//input[@name='check_optimization']")
    private WebElement optimizationCheckbox;

    @FindBy(xpath = "//input[@name='check_poster']")
    private WebElement posterCheckbox;

    @FindBy(xpath = "//input[@name='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "(//h3)[2]")
    private WebElement firstImageName;

    @FindBy(xpath = "(//h3)[3]")
    private WebElement secondImageName;

    @FindBy(xpath = "(//h3)[4]")
    private WebElement thirdImageName;

    @FindBy(xpath = "//textarea[@name='files']")
    private WebElement linksInput;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getHelpServiceLink() {
        return helpServiceLink;
    }

    public WebElement getAddFieldLink() {
        return addFieldLink;
    }

    public WebElement getFromComputerLink() {
        return fromComputerLink;
    }

    public WebElement getFromLinkLink() {
        return fromLinkLink;
    }

    public WebElement getRestrictionsLink() {
        return restrictionsLink;
    }

    public WebElement getRestrictionsText() {
        return restrictionsText;
    }

    public WebElement getDisableAllEffectsLink() {
        return disableAllEffectsLink;
    }

    public WebElement getFileInput() {
        return fileInput;
    }

    public WebElement getFile2Input() {
        return file2Input;
    }

    public WebElement getFile3Input() {
        return file3Input;
    }

    public WebElement getResizeCheckbox() {
        return resizeCheckbox;
    }

    public WebElement getResizeSelect() {
        return resizeSelect;
    }

    public WebElement getRotateCheckbox() {
        return rotateCheckbox;
    }

    public WebElement getRotateSelect() {
        return rotateSelect;
    }

    public WebElement getOptimizationCheckbox() {
        return optimizationCheckbox;
    }

    public WebElement getPosterCheckbox() {
        return posterCheckbox;
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

    public WebElement getFirstImageName() {
        return firstImageName;
    }

    public WebElement getSecondImageName() {
        return secondImageName;
    }

    public WebElement getThirdImageName() {
        return thirdImageName;
    }

    public WebElement getLinksInput() {
        return linksInput;
    }
}
