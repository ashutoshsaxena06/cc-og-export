package executor;

import export.actions.PurveyorAction;
import export.actions.UsFoodsAction;
import export.common.selenium.ExcelFunctions;
import export.common.selenium.RandomAction;
import export.common.selenium.SendMailSSL;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;


public class TestUSFoodsExecutor extends BaseExecutor {

    private final static Logger logger = Logger.getLogger(TestUSFoodsExecutor.class);
    public static String project = "USF";
    PurveyorAction purveyorAction = new UsFoodsAction();

    @BeforeTest
    public static void beforeData() throws Exception {
        System.out.println("before data.");
    }

    @AfterTest
    public static void closeResources() throws SQLException, IOException {
        System.out.println("Closing the resources!");

        if (out != null) {
            System.out.println("Closing file output stream object!");
            out.close();
        }
        if (driver != null) {
            System.out.println("Closing the browser!");
            // TestCases.driver.close();
            driver.quit();
        }

        if (exportworkbook != null) {
            exportworkbook.close();
        }
    }

    @BeforeMethod
    public static void setUp() throws IOException {
        // to get the browser on which the UI test has to be performed.
        System.out.println("***********StartTest*********");
//        RandomAction.deleteFiles("C:\\Users\\Edge\\Downloads");
        driver = RandomAction.openBrowser("Chrome", chromePath);
        System.out.println("Invoked browser .. ");
    }

    @AfterMethod
    public static void writeExcel() throws IOException {
        System.out.println("Running Excel write method!");
        out = new FileOutputStream(new File(reportFile));
        exportworkbook.write(out);
        acno++;
        try {
            driver.quit();
        } catch (Exception e) {
            System.out.println("already closed");
        }
    }

    @DataProvider(name = "testData")
    public static Object[][] testData() throws IOException {
        exportworkbook = ExcelFunctions.openFile(inputFile);
        System.out.println("Test data read.");
        inputsheet = exportworkbook.getSheet(project);
        AcColStatus = ExcelFunctions.getColumnNumber("Export Status", inputsheet);
        AcColdetail = ExcelFunctions.getColumnNumber("Detailed Export Status", inputsheet);
        System.out.println("Inside Dataprovider. Creating the Object Array to store test data inputs.");
        Object[][] td = null;
        try {
            // Get TestCase sheet data
            int totalNoOfCols = inputsheet.getRow(inputsheet.getFirstRowNum()).getPhysicalNumberOfCells();
            totalNoOfRows = inputsheet.getLastRowNum();
            System.out.println(totalNoOfRows + " Accounts and Columns are: " + totalNoOfCols);
            td = new String[totalNoOfRows][totalNoOfCols];
            for (int i = 1; i <= totalNoOfRows; i++) {
                for (int j = 0; j < totalNoOfCols; j++) {
                    td[i - 1][j] = ExcelFunctions.getCellValue(inputsheet, i, j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Test Cases captured in the Object Array. Exiting dataprovider.");
        return td;
    }

    ////////////////////////////////////////////////
    @AfterClass
    public static void sendMail() {
        try {
            String emailMsg = "Daily " + project + " OG Export Status: " + RandomAction.getDate();

            SendMailSSL.sendReport(emailMsg, reportFile);
            System.out.println("Email Sent with Attachment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "testData")
    public void Export_Mail_OG(String active, String accountID, String purveyor, String restaurant_name,
                               String username, String password, String account, String listname, String exportstatus,
                               String detailedstatus) {
        Boolean result;
        System.out.println("Inside OG Export : Started exporting OG for different accounts");
        XSSFCell cell1, cell2;
        TestUSFoodsExecutor.rowIndex = Math.floorMod(TestUSFoodsExecutor.acno, TestUSFoodsExecutor.totalNoOfRows) + 1;

        System.out.println("Test Case test #" + TestUSFoodsExecutor.rowIndex);

        cell1 = TestUSFoodsExecutor.exportworkbook.getSheet(project).getRow(TestUSFoodsExecutor.rowIndex)
                .createCell(TestUSFoodsExecutor.AcColStatus);
        cell1.setCellValue("");

        cell2 = TestUSFoodsExecutor.exportworkbook.getSheet(project).getRow(TestUSFoodsExecutor.rowIndex)
                .createCell(TestUSFoodsExecutor.AcColdetail);
        cell2.setCellValue("");

        exportstatus = cell1.getStringCellValue();
        detailedstatus = cell2.getStringCellValue();

        try {
            if (active.equalsIgnoreCase("Yes")) {
                // if list is not empty
                System.out.println(restaurant_name + " for purveryor " + purveyor + " is Active !!");
                if (listname != null && listname.length() != 0) {
                    result = purveyorAction.process(driver, listname.trim(), account, username.trim(), password.trim());
                    if (result.equals(true)) {
                        emailMessageExport = "Pass";
                        exportstatus = "Pass";
                        detailedstatus = "OG exported succesfully";
                    } else {
                        emailMessageExport = "Failed";
                        exportstatus = "Failed";
                        detailedstatus = "Some technical issue ocurred during export";
                    }
                } else { // default OG
                    // result = startSysco(driver, username.trim(), password.trim());
                    exportstatus = "Failed";
                    detailedstatus = "Error : Please provide valid List name";
                }
                Thread.sleep(5000);
                SendMailSSL.sendMailActionPDF(purveyor.trim(), restaurant_name.trim());
            } else {
                System.out.println(restaurant_name + " for purveryor " + purveyor + " is not Active !!");
                exportstatus = "Not Active";
            }
            cell1.setCellValue(exportstatus);
            cell2.setCellValue(detailedstatus);

            System.out.println("Exiting test method");

        } catch (Exception e) {
            e.printStackTrace();
            exportstatus = "Failed";
            detailedstatus = "Some technical issue ocurred during export";
            cell1.setCellValue(exportstatus);
            cell2.setCellValue(detailedstatus);
            System.out.println("Technical issue occured during export for restaurant - " + restaurant_name);
            Assert.assertTrue(false);
        }
        System.out.println(emailMessageExport.trim());
    }
    ////////////////////////////////////////////////////

}