package export.actions;

import export.purveyors.cheney.central.pages.HomePage;
import export.purveyors.cheney.central.pages.LoginPage;
import export.purveyors.cheney.central.pages.OrderGuideExportPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class CheneyCentralAction implements PurveyorAction {
    private static final Logger logger = Logger.getLogger(CheneyCentralAction.class);

    @Override
    public boolean process(WebDriver driver, String user, String password) {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        OrderGuideExportPage orderGuideExportPage = new OrderGuideExportPage(driver);
        try {
            loginPage.invokeLogin();
            loginPage.doLogin(user, password);
            Thread.sleep(20000);
            if (loginPage.isLoginSuccess()) {
                homePage.goToOrderGuide();
                orderGuideExportPage.exportExcel();
            } else {
                logger.error("Failed at Login");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean process(WebDriver driver, String listname, String account, String username, String password) {
        throw new UnsupportedOperationException();
    }
}
