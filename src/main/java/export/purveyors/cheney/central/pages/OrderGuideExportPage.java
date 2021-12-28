package export.purveyors.cheney.central.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderGuideExportPage extends ParentPage {
    private static final Logger logger = Logger.getLogger(ParentPage.class);

    public OrderGuideExportPage(WebDriver driver) {
        super(driver);
    }

    By btn_export = By.xpath("//div[contains(@class, 'heading')]//button[@id='dropdownExportOpt'][@title='EXPORT TO']");
    By lnk_excelExport = By.xpath("//div[contains(@class, 'heading')]//a[contains(.,'Excel')]");

    public void exportExcel() {
        waitForElementToAppear(btn_export).click();
        waitForElementToAppear(lnk_excelExport).click();
    }

}
