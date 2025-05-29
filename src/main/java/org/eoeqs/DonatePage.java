package org.eoeqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DonatePage {
    @FindBy(xpath = "//button[@class='btn-copy-textarea']")
    private List<WebElement> copyButtons;

    public DonatePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getCopyButtons() {
        return copyButtons;
    }
}