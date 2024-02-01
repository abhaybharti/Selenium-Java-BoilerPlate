package main.java.com.core.basetest;


import com.testkit.reportmanager.ExtentReportManager;
import com.testkit.utils.reader.INIConfigReader;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestSetup {
//  protected Logger logger = Logger.getLogger("TestSetup.class");
  public HashMap<String, String> testConfigData = new HashMap<>();
  ExtentReportManager testReport = new ExtentReportManager();
  public static String executedTestCaseListFile = System.getProperty("user.dir")+File.separator+"executedTestCaseList.txt";
  public static String failedTestCaseListFile = System.getProperty("user.dir")+File.separator+"failedTestCaseList.txt";

  @BeforeSuite
  public void beforeSuite(ITestContext iTestContext) {
//    Steps
//    Create report file
    //read testConfig.ini file
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

  /**
   * The function creates a folder with a timestamped name in the "results" directory and returns the
   * path to the created folder.
   * 
   * @param suiteName The suiteName parameter is a String that represents the name of the test suite or
   * test case.
   * @return The method is returning a String that represents the path of the newly created report
   * folder.
   */
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

/**
 * The above function is a method annotated with @BeforeClass that takes an ITestContext parameter and
 * is executed before the test class is instantiated.
 * 
 * @param context The "context" parameter in the "beforeClass" method is of type "ITestContext". It is
 * an interface provided by TestNG framework that allows access to information about the current test
 * run, such as the test suite, test methods, test parameters, etc. It can be used to perform
 */
  @BeforeClass
  public void beforeClass(ITestContext context) {}

  /**
   * The "afterSuite" function writes the test report to an HTML file after all the tests have been
   * executed.
   */
  @AfterSuite
  public void afterSuite() {
    testReport.writeToHtmlFile();
  }

  public void writeTestCaseIntoFile(String testCaseName,String fileName) {
    BufferedWriter writer = null;
    try{
      File file = new File(fileName);
      if (!file.exists()){
        file.createNewFile();
      }
      List<String> testCaseList = readTestCaseFromFile(fileName);
      if (!testCaseList.contains(testCaseName)){
        writer = new BufferedWriter(new FileWriter(file,true));
        writer.write(testCaseName);
        writer.newLine();
        writer.close();

      }
    }catch (Exception e) {
     e.printStackTrace();
    }
  }

  public void writeTestCaseIntoFile(String testCaseName,String fileName,boolean createNewFile) {
    BufferedWriter writer = null;
    try{
      File file = new File(fileName);
      List<String> testCaseList = readTestCaseFromFile(fileName);

      if (file.exists() && createNewFile){
        file.delete();
        file.createNewFile();
        Set<String> uniqueListOfTestCase = new LinkedHashSet<>();
        uniqueListOfTestCase.addAll(testCaseList);

        testCaseList.clear();

        testCaseList.addAll(uniqueListOfTestCase);
        testCaseList.remove(testCaseName);
        writer = new BufferedWriter(new FileWriter(file,true));
        for (int i = 0; i < testCaseList.size(); i++) {
          writer.write(testCaseList.get(i));
          writer.newLine();
        }
        writer.close();
      }else{
        System.out.println();
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<String> readTestCaseFromFile(String fileName) {
      List<String> lines = Collections.emptyList();
      File file = new File(fileName);
      try{
        if (!file.exists()){
          file.createNewFile();
        }
        lines = Files.readAllLines(file.toPath());
      }catch (IOException e){
        e.printStackTrace();
      }
      return lines;
  }

  public String alterSuiteName(ITestContext iTestContext){
    String suiteName = iTestContext.getSuite().getName();
    String testNgFileName = iTestContext.getCurrentXmlTest().getSuite().getFileName();
    if (testNgFileName.contains("resume")){
      return "Resume_Suite_"+suiteName;
    }else if (testNgFileName.contains("failed")){
      return "Failed_Suite_"+suiteName;
    }else{
      return suiteName;
    }
  }

}
