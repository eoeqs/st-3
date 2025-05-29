package org.eoeqs;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DownloadsPage {
    @FindBy(xpath = "//a[@class='jslink'][text()='[ выбрать всё ]']")
    private WebElement selectAllLink;

    @FindBy(xpath = "//a[@class='jslink'][text()='[ снять выбор ]']")
    private WebElement deselectAllLink;

    @FindBy(xpath = "//a[@class='jslink'][text()='[ удалить выбранное ]']")
    private WebElement deleteSelectedLink;

    @FindBy(xpath = "//div[@class='thumb']")
    private List<WebElement> images;

    @FindBy(xpath = "//input[@type='checkbox']")
    private List<WebElement> checkboxes;

    public DownloadsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getSelectAllLink() {
        return selectAllLink;
    }

    public WebElement getDeselectAllLink() {
        return deselectAllLink;
    }

    public WebElement getDeleteSelectedLink() {
        return deleteSelectedLink;
    }

    public List<WebElement> getImages() {
        return images;
    }

    public List<WebElement> getCheckboxes() {
        return checkboxes;
    }
}