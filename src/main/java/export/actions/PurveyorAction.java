package export.actions;

import org.openqa.selenium.WebDriver;

public interface PurveyorAction {
    boolean process(WebDriver driver, String user, String password);
    boolean process(WebDriver driver, String listname, String account, String username, String password);
}
