package export.actions;

import export.purveyors.usf.CommonUSFoods;
import org.openqa.selenium.WebDriver;

public class UsFoodsAction implements PurveyorAction {

    @Override
    public boolean process(WebDriver driver, String user, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean process(WebDriver driver, String listName, String account, String username, String password) {
        try {
            return new CommonUSFoods(driver).startUSF(listName, account, username, password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
