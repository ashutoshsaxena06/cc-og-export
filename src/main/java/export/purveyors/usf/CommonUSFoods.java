package export.purveyors.usf;

import export.common.selenium.RandomAction;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class CommonUSFoods {
    static final int maxtry = 3;
    public static WebDriverWait wait;
    public static String url = "https://www3.usfoods.com/order/faces/oracle/webcenter/portalapp/pages/login.jspx";
    public static CommonUSFoods com;
    public static String listUrl = "https://www3.usfoods.com/order/faces/oracle/webcenter/portalapp/pages/lists/myLists.jspx?";
    static int retry = 0;
    public static WebDriver driver;
    @FindBy(xpath = ".//*[@id='it9::content']")
    WebElement txt_UserName;
    // password
    @FindBy(xpath = ".//*[@id='it1::content']")
    WebElement txt_Password;
    // login page
    // username
    // login button
    @FindBy(xpath = ".//*[@id='cb1']")
    WebElement btn_Submit;
    // home page
    // List - to use for order
    @FindBy(xpath = ".//*[@id='dgfSPT:pt_i3:0:pt_sfm1:pt_cil7']")
    WebElement li_ListIcon;
    // download
    @FindBy(xpath = "//span[contains(text(),'DOWNLOAD')]")
    WebElement lnk_Download;
    // Download icon
    @FindBy(xpath = "//td/*[@id='r1:0:pt1:cil15']/img")
    WebElement btn_ListIcon;
    // Download icon
    @FindBy(xpath = "//*[@id='dgfSPT:pt_ot4']/span")
    WebElement lnk_accountName;
    // list of accounts
    @FindBy(xpath = "//div[@id='dgfSPT:pt_pgl117']/*")
    WebElement li_Accounts;
    // Download pop-up
    // FileName
    @FindBy(xpath = "//table[@class='poNumber af_inputText']//input")
    WebElement txt_FileName;
    // Format
    @FindBy(xpath = "//table[@class='fieldLabelSOC af_panelLabelAndMessage']//span/div/div/div")
    WebElement txt_Format;
    // Format
    @FindBy(xpath = ".//*[@id='r1:0:pt1:r5:0:soc4']/..//div/a")
    WebElement lnk_FormatSelectIcon;
    // Download button
    @FindBy(xpath = ".//*[@id='r1:0:pt1:r5:0:cb3']")
    WebElement btn_download;
    // format to CSV
    @FindBy(xpath = ".//*[@id='r1:0:pt1:r5:0:soc4']/..//div/ul/li/a[text()='PDF']")
    WebElement lnk_FormatSelectPDF;
    // format to CSV
    @FindBy(xpath = "//span[contains(text(),'Sign Out')]")
    WebElement btn_SignOut;
    public CommonUSFoods(WebDriver driver) {
        CommonUSFoods.driver = driver;
    }

    public CommonUSFoods() {
        PageFactory.initElements(driver, this);
    }

    // List selection
    public WebElement setOrderGuide(String OGName) {
        // "//table[@class='af_panelGroupLayout']//a[contains(.,'" + OGName + "')]"
        return wait
                .until(ExpectedConditions.elementToBeClickable(By.linkText(OGName)));
        // By.xpath("//div[@class='x2ml dropDownMenu-UtilityMenu
        // x1a']/*/*/*/a[contains(.,'" + OGName + "')]"))); //
        // div[@id='r1:0:pt1:pt_i3:0:pt_sfm1:pt_pgl44']ss
    }

    public WebElement setOptions(String OGName) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@class='af_panelGroupLayout']//a[contains(.,'" + OGName
                + "')]/ancestor::td[2]/following-sibling::td/..//a[starts-with(.,'Options')]")));
    }

    public Boolean startUSF(String listname, String account, String username, String password)
            throws InterruptedException {
        // driver = RandomAction.openBrowser("Chrome",
        // "C:\\Users\\my\\Downloads\\chromedriver_win32_new\\chromedriver.exe");
        com = new CommonUSFoods();
        wait = new WebDriverWait(driver, 30);

        try {
            com.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login Failed ! ");
            return false;
        }

        try {
            Thread.sleep(3000);

            if (account != null) {
                changeAccount(account);
            } else {
                System.out.println("account change not required");
            }

            Thread.sleep(3000);

            clickList(listname);

            Thread.sleep(5000);
            // pop-up
            downloadFile(listname + "-" + RandomAction.getDate());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to download file");
            return false;
        } finally {
            try {
                Thread.sleep(8000);
                com.btn_SignOut.click();
            } catch (Exception e2) {
                System.out.println("not able to Logout successfully !");
            }
        }

    }

    public void changeAccount(String account) {
//		wait = new WebDriverWait(driver, 30);

        wait.until(ExpectedConditions.visibilityOf(com.lnk_accountName));
        String currentAccountName = com.lnk_accountName.getText();
        if (!currentAccountName.contains(account)) {
            chooseAccount(account);
        } else {
            System.out.println("required account already selected !");
        }
    }

    public void login(String username, String password) throws InterruptedException {
        wait = new WebDriverWait(driver, 30);

        driver.get(url);
        while (retry < maxtry) {
            if (!driver.getCurrentUrl().contains("www3.usfoods.com")) {
                Thread.sleep(3000);
            } else {
                break;
            }
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Thread.sleep(3000);
        WebElement userName = wait.until(ExpectedConditions.visibilityOf(com.txt_UserName));
        userName.sendKeys(username);
        WebElement pwd = wait.until(ExpectedConditions.visibilityOf(com.txt_Password));
        pwd.sendKeys(password);
        WebElement submit = wait.until(ExpectedConditions.visibilityOf(com.btn_Submit));
        submit.click();
    }

    public void clickList(String listname) throws InterruptedException {
//		wait = new WebDriverWait(driver, 30);
        try {
            Actions act = new Actions(driver);
            driver.get(listUrl);
            System.out.println("On View Lists page");
            WebElement orderGuide = com.setOrderGuide(listname);
            jsScrollIntoView(orderGuide);
            Thread.sleep(200);
            jsClick(orderGuide);
//            WebElement options = setOptions(listname);
//            options.click();
            Thread.sleep(2000);
            com.lnk_Download.click();
            System.out.println("Order Guide name found on list download page");
        } catch (Exception e) {
            System.out.println("Order guide select failed");
            e.printStackTrace();
        }
    }

    public void jsClick(WebElement ele) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", ele);
    }

    public void jsScrollIntoView(WebElement ele) {
        JavascriptExecutor je = (JavascriptExecutor) driver;
        je.executeScript("arguments[0].scrollIntoView(true);", ele);
    }

    public void downloadFile(String filename) {
        wait = new WebDriverWait(driver, 30);

        WebElement In_filename = wait.until(ExpectedConditions.visibilityOf(com.txt_FileName));
        In_filename.clear();
        In_filename.sendKeys(filename);
        if (!wait.until(ExpectedConditions.visibilityOf(com.txt_Format)).getText().equalsIgnoreCase("PDF")) {
            wait.until(ExpectedConditions.visibilityOf(com.lnk_FormatSelectIcon)).click();
            wait.until(ExpectedConditions.visibilityOf(com.lnk_FormatSelectPDF)).click();
        }
        wait.until(ExpectedConditions.visibilityOf(com.btn_download)).click();
    }

    public void chooseAccount(String account) {
//		wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("dgfSPT:pt_cil4::icon")))).click();
        WebElement accountSelect = wait.until(ExpectedConditions.elementToBeClickable(
                (By.xpath("//div[@id='dgfSPT:pt_pgl117']/div/ .. //span[contains(.,'" + account + "')]"))));
        accountSelect.click();
        System.out.println("select account - " + account);
    }
}