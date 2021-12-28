package export.process;

import export.purveyors.cheney.central.pages.HomePage;
import export.purveyors.cheney.central.pages.LoginPage;
import export.purveyors.cheney.central.pages.OrderGuideExportPage;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class CheneyCentralProcess {
    private static final Logger logger = Logger.getLogger(CheneyCentralProcess.class);

    public static WebDriver driver;

    public boolean process(String user, String password) {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        OrderGuideExportPage orderGuideExportPage = new OrderGuideExportPage(driver);
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
