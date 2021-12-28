package com.de.og.export.process;

import com.de.og.export.purveyors.cheney.central.pages.HomePage;
import com.de.og.export.purveyors.cheney.central.pages.LoginPage;
import com.de.og.export.purveyors.cheney.central.pages.OrderGuideExportPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CheneyCentralProcess {
    private static final Logger logger = Logger.getLogger(CheneyCentralProcess.class);

    public WebDriver driver = null;
    HomePage homePage = new HomePage(driver);
    LoginPage loginPage = new LoginPage(driver);
    OrderGuideExportPage orderGuideExportPage = new OrderGuideExportPage(driver);

    public boolean process(String user, String password) {
        try {
            loginPage.invokeLogin();
            loginPage.doLogin(user, password);
            Assert.assertTrue(loginPage.isLoginSuccess());
            homePage.goToOrderGuide();
            orderGuideExportPage.exportExcel();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
