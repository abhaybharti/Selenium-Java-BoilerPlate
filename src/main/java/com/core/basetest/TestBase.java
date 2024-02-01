package main.java.com.core.basetest;

import com.testkit.config.reader.INIConfigReader;
import com.testkit.reportmanager.ExtentReportManager;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TestBase {
    public HashMap<String, String> testConfigData = new HashMap<>();
    ExtentReportManager testReport = new ExtentReportManager();

    @BeforeSuite
    public void beforeSuite(ITestContext iTestContext) {
        String suiteName = iTestContext.getSuite().getName();
        testReport.initExtentReport(createReportFolder(suiteName));
        loadTestConfigIniFileDataToMap();
    }

    public void loadTestConfigIniFileDataToMap() {
        String iniFilePath = System.getProperty("user.dir") + "\\testConfig.ini";
        if (new File(iniFilePath).exists()) {
            INIConfigReader iniConfigReader = new INIConfigReader(iniFilePath);
            testConfigData.putAll(iniConfigReader.getDataFromSection("testEnvConfig"));
        } else {
            Assert.fail(System.getProperty("user.dir") + "\\testConfig.ini does not exist");
        }
    }

    private String createReportFolder(String suiteName) {
        DateFormat dateFormat = new SimpleDateFormat(("yyyy_MM_dd_HH_mm_ss_a"));
        Date date = new Date();
        String timeStamp = suiteName + "_" + dateFormat.format(date).toString();
        String resultPath = System.getProperty("user.dir") + "\\results" + File.separator + timeStamp;
        new File(resultPath).mkdir();
        new File(resultPath).setExecutable(true, true);
        new File(resultPath).setWritable(true, true);
        return resultPath;
    }

    @BeforeClass
    public void beforeClass(ITestContext context) {

    }

    @AfterSuite
    public void afterSuite() {
        testReport.writeToHtmlFile();
    }
}
