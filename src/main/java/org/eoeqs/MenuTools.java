package org.eoeqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MenuTools {
    @FindBy(xpath = "(//div[@class='menuitem'])[1]")
    private WebElement mainLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[2]")
    private WebElement rulesLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[3]")
    private WebElement faqLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[4]")
    private WebElement uploaderLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[5]")
    private WebElement myLoadingLink;

    @FindBy(xpath = "(//div[@class='menuitem'])[6]")
    private WebElement donateLink;

    public MenuTools(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getMainLink() {
        return mainLink;
    }

    public WebElement getRulesLink() {
        return rulesLink;
    }

    public WebElement getFaqLink() {
        return faqLink;
    }

    public WebElement getUploaderLink() {
        return uploaderLink;
    }

    public WebElement getMyLoadingLink() {
        return myLoadingLink;
    }

    public WebElement getDonateLink() {
        return donateLink;
    }
}