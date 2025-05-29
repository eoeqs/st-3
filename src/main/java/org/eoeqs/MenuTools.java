package org.eoeqs;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MenuTools {
    @FindBy(xpath = "(//div[@class='menuitem'])[1]")
    public WebElement mainLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[2]")
    public WebElement rulesLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[3]")
    public WebElement faqLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[4]")
    public WebElement uploaderLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[5]")
    public WebElement myLoadingLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[6]")
    public WebElement donateLink;

    public MenuTools(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}