package executor;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import export.actions.PurveyorAction;
import export.common.selenium.ExcelFunctions;
import export.common.selenium.RandomAction;
import export.common.selenium.SendMailSSL;
import export.actions.CheneyCentralAction;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.*;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCheneyCentralExecutor extends BaseExecutor {
    private final static Logger logger = Logger.getLogger(TestCheneyCentralExecutor.class);
    public static String reportFile = userHome + "/Desktop/Reports/CheneyCentral_OG_report/ExportSummary_Cheney_Central"
            + new Date().toString().replace(":", "").replace(" ", "") + ".xlsx";
    private final PurveyorAction purveyorAction = new CheneyCentralAction();

    private static final String project = "CheneyCentral";

    @BeforeSuite
    public static void set() throws IOException {
        er = new ExtentReports(System.getProperty("user.dir") + File.separator + "extentsReport/Report.html", true);
        er.addSystemInfo("Host Name", "Edge").addSystemInfo("Environment", "Windows Server")
                .addSystemInfo("User Name", "Ashutosh Saxena").addSystemInfo("Project", project);
        er.loadConfig(new File(System.getProperty("user.dir") + File.separator + "extents-config.xml"));
        er.assignProject(project + " Online OG Export");
    }

    @BeforeMethod
    public static void setUp() throws IOException {
        // to get the browser on which the UI test has to be performed.
        System.out.println("***********StartTest*********");
        RandomAction.deleteFiles(System.getProperty("user.home") + "\\Downloads");
        driver = RandomAction.openBrowser("Chrome", chromePath);
        logger.info("Invoked browser .. ");
    }

    @AfterMethod
    public static void writeExcel() throws IOException {
        logger.info("Running Excel write method!");
        out = new FileOutputStream(reportFile);
        exportworkbook.write(out);
        er.endTest(et);
        acno++;
        try {
            driver.quit();
        } catch (Exception e) {
            System.out.println("already closed");
        }
    }

    @DataProvider(name = "testData")
    public static Object[][] testData() throws IOException {
        logger.info("Inside Dataprovider. Creating the Object Array to store test data inputs.");
        Object[][] td = null;
        try {
            // Get TestCase sheet data
            int totalNoOfCols = inputsheet.getRow(inputsheet.getFirstRowNum()).getPhysicalNumberOfCells();
            totalNoOfRows = inputsheet.getLastRowNum();
            logger.info(totalNoOfRows + " Accounts and Columns are: " + totalNoOfCols);
            td = new String[totalNoOfRows][totalNoOfCols];
            for (int i = 1; i <= totalNoOfRows; i++) {
                for (int j = 0; j < totalNoOfCols; j++) {
                    td[i - 1][j] = ExcelFunctions.getCellValue(inputsheet, i, j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Test Cases captured in the Object Array. Exiting dataprovider.");
        return td;
    }

    @AfterSuite
    public static void sendMailAndclose() {
        try {
            String emailMsg = "Daily " + project + " OG Export Status: " + RandomAction.getDate();

            SendMailSSL.sendReports(emailMsg, reportFile);
            logger.info("Email Sent with Attachment");

            er.flush();
            er.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeTest
    public void beforeData() throws Exception {
        // read excel data

        // get active accounts
        // launch browser

        // -- fail ->
        exportworkbook = ExcelFunctions.openFile(inputFile);
        logger.info("Test data read.");
        inputsheet = exportworkbook.getSheet(project);
        AcColStatus = ExcelFunctions.getColumnNumber("Export Status", inputsheet);
        AcColdetail = ExcelFunctions.getColumnNumber("Detailed Export Status", inputsheet);

        logger.info("Exiting before data.");
        // copy config file to report folder
        // ExcelFunctions.copySheet(exportworkbook, , );
    }

    @AfterTest
    public void closeResources() throws SQLException, IOException {
        logger.info("Closing the resources!");

        if (out != null) {
            logger.info("Closing file output stream object!");
            out.close();
        }
        if (driver != null) {
            logger.info("Closing the browser!");
            // TestCases.driver.close();
            driver.quit();
        }

        if (exportworkbook != null) {
            exportworkbook.close();
        }
    }

    ////////////////////////////////////////////////
//	@AfterClass
//	public static void sendMail() {
//
//	}

    @Test(dataProvider = "testData")
    public void Export_Mail_OG(String active, String accountID, String purveyor, String restaurant_name,
                               String username, String password, String listname, String exportstatus, String detailedstatus) {
        Boolean result;
        logger.info("Inside OG Export : Started exporting OG for different accounts");
        XSSFCell cell1, cell2;
        TestCheneyCentralExecutor.rowIndex = Math.floorMod(TestCheneyCentralExecutor.acno, TestCheneyCentralExecutor.totalNoOfRows) + 1;

        System.out.println("Test Case test #" + TestCheneyCentralExecutor.rowIndex);
        cell1 = TestCheneyCentralExecutor.exportworkbook.getSheet(project).getRow(TestCheneyCentralExecutor.rowIndex)
                .createCell(TestCheneyCentralExecutor.AcColStatus);
        cell1.setCellValue("");
        cell2 = TestCheneyCentralExecutor.exportworkbook.getSheet(project).getRow(TestCheneyCentralExecutor.rowIndex)
                .createCell(TestCheneyCentralExecutor.AcColdetail);
        cell2.setCellValue("");
        // if((cell1=TestCheneyExecutor.exportworkbook.getSheet(project).getRow(TestCheneyExecutor.rowIndex).getCell(TestCheneyExecutor.AcColStatus))==null){
        // cell1 =
        // TestCheneyExecutor.exportworkbook.getSheet(project).getRow(TestCheneyExecutor.rowIndex).createCell(TestCheneyExecutor.AcColStatus);
        // cell1.setCellValue("");
        // }
        // if((cell2=TestCheneyExecutor.exportworkbook.getSheet(project).getRow(TestCheneyExecutor.rowIndex).getCell(TestCheneyExecutor.AcColdetail))==null){
        // cell2 =
        // TestCheneyExecutor.exportworkbook.getSheet(project).getRow(TestCheneyExecutor.rowIndex).createCell(TestCheneyExecutor.AcColdetail);
        // cell2.setCellValue("");
        // }
        exportstatus = cell1.getStringCellValue();
        detailedstatus = cell2.getStringCellValue();
        et = er.startTest(restaurant_name);
        try {
            if (active.equalsIgnoreCase("Yes")) {
                // if list is not empty
                logger.info(restaurant_name + " for purveryor " + purveyor + " is Active !!");
                et.log(LogStatus.INFO, restaurant_name + " and purveryor " + purveyor + " and listname is" + listname);
                result = purveyorAction.process(driver, username.trim(), password.trim());
                if (result.equals(true)) {
                    emailMessageExport = "Pass";
                    exportstatus = "Pass";
                    detailedstatus = "OG exported succesfully";
                    et.log(LogStatus.PASS, detailedstatus);
                    Thread.sleep(8000);
                    SendMailSSL.sendMailActionXlsx(purveyor.trim(), restaurant_name.trim());
                } else {
                    emailMessageExport = "Failed";
                    exportstatus = "Failed";
                    detailedstatus = "OG export Failed";
                    et.log(LogStatus.FAIL, detailedstatus);
                }
            } else {
                logger.info(restaurant_name + " for purveryor " + purveyor + " is not Active !!");
                exportstatus = "Not Active";
                et.log(LogStatus.SKIP, detailedstatus);
            }
            cell1.setCellValue(exportstatus);
            cell2.setCellValue(detailedstatus);

            logger.info("Exiting test method");

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            exportstatus = "Failed";
            detailedstatus = "Some technical issue ocurred during export";
            cell1.setCellValue(exportstatus);
            cell2.setCellValue(detailedstatus);
            logger.info("Technical issue occured during export for restaurant - " + restaurant_name);
            et.log(LogStatus.FAIL, exportstatus + " - " + detailedstatus);
        }
        logger.info(emailMessageExport.trim());
    }

    ////////////////////////////////////////////////////

}