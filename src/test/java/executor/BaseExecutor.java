package executor;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseExecutor {

    public static final String userHome = System.getProperty("user.home");
    public static final String currDir = System.getProperty("user.dir");
    public static final String projectPath = currDir;
    public static final String inputFile = userHome + "/Desktop/ExportEngineInput.xlsx";
    public static final String chromePath = userHome + "/Downloads/chromedriver_win32/chromedriver.exe";
    public static final String extentReport = currDir + File.separator + "extentsReport" + File.separator + "Report.html";
    private final static Logger logger = Logger.getLogger(BaseExecutor.class);
    public static int rowIndex;
    // projectPath + "/config/ExportEngineInput.xlsx";
    public static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
  // for Edge -
    // "C:/Users/Edge/Desktop/Reports/CheneyOG_report/ExportSummary_Cheney_" +
    // PageAction.getDate().toString().replace(" ", "_");
    // + new Date().toString().replace(":", "").replace(" ", "") + ".xlsx";
    // projectPath+ "/Output_Summary/ExportSummary_Cheney_" + new
    // Date().toString().replace(":", "").replace(" ", "")+".xlsx";
    public static int acno;
    public static XSSFWorkbook exportworkbook;
    public static XSSFSheet inputsheet;
    public static int AcColStatus, AcColdetail;
    public static FileOutputStream out;
    public static int totalNoOfRows;
    public static String folderDate;
    public static String currList = "";
    public static String emailMessageExport = "";
    public static ExtentReports er;
    public static ExtentTest et;
    public static int retry = 0;
    public final int maxtry = 3;
    public static WebDriver driver;
}
