package org.eoeqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PicPage {
    @FindBy(xpath = "(//div[@class='resolution text-white-50 m-2'])[1]")
    private WebElement resolution;

    public PicPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getResolution() {
        return resolution;
    }
}
