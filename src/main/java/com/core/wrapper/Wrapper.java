package main.java.com.core.wrapper;

import com.testkit.helper.Helper;
import com.testkit.reportmanager.ExtentReportManager;
import com.testkit.utils.store.Cache;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
public class Wrapper {
    public ExtentReportManager report = null;
    public Logger logger = Logger.getLogger(Wrapper.class.getName());
    public HashMap<String, String> testData = new HashMap<>();
    Integer stepCount = 0;
    Helper helper = null;

    public Wrapper(HashMap<String,String> testData, Logger logger, ExtentReportManager reportManager, String jsonFileName) {
     this.testData = testData;
     this.report = reportManager;
    }

    public String throwErrorWhenKeyNoExistInMap(String errorMessage) {
        ITestResult testResult =  Cache.getCacheITestResult();
        testResult.setStatus(2);
        report.log("FAIL", errorMessage);
        return errorMessage;
    }

    /***
     * This method is used to print step in Extent report
     * @param message
     * @param iteration
     */
    public void stepPrint(String message, int iteration) {
        stepCount = stepCount + 1;
        String Iteration = "";
        if (iteration != 0) {
            iteration = Integer.parseInt(" Iteration [" + iteration + "] ");
        }
        report.log("INFO", "<br> Step [" + stepCount + "] ,  " + iteration + message + "</b>");
    }

    public void verifyObjectIsEditable(){

    }

    public void verifyObjectIsNotEditable(){

    }

    public void compareNumericValue(int firstValue, int secondValue, String comparatorValue){
        SoftAssert softAssert = new SoftAssert();
        switch (comparatorValue){
            case "EQUAL":
                if (firstValue == secondValue){
                    System.out.println("First Value :[" + firstValue + "] is equal to Second Value : [" + secondValue + "]");
                }else{
                    rm.log("FAIL","First Value :[" + firstValue + "] is not equal to Second Value : [" + secondValue + "]");
                }
                break;
            case "NOT_EQUAL":
                if (firstValue != secondValue){
                    System.out.println("First Value :[" + firstValue + "] is not equal to Second Value : [" + secondValue + "]");
                }else{
                    rm.log("FAIL","First Value :[" + firstValue + "] is equal to Second Value : [" + secondValue + "]");
                }
                break;
            case "GREATER_THAN":
                if (firstValue > secondValue){
                    System.out.println("First Value :[" + firstValue + "] is greater than Second Value : [" + secondValue + "]");
                }else{
                    rm.log("FAIL","First Value :[" + firstValue + "] is greater than Second Value : [" + secondValue + "]");
                }
                break;
            case "LESS_THAN":
                if (firstValue < secondValue){
                    System.out.println("First Value :[" + firstValue + "] is less than Second Value : [" + secondValue + "]");
                }else{
                    rm.log("FAIL","First Value :[" + firstValue + "] is less than Second Value : [" + secondValue + "]");
                }
                break;
            case "GREATER_THAN_EQUAL":
                if (firstValue >= secondValue){
                    System.out.println("First Value :[" + firstValue + "] is greater than or equal to Second Value : [" + secondValue + "]");
                }else{
                    rm.log("FAIL","First Value :[" + firstValue + "] is not greater than or equal to Second Value : [" + secondValue + "]");
                }
                break;
            case "LESS_THAN_EQUAL":
                if (firstValue <= secondValue){
                    System.out.println("First Value :[" + firstValue + "] is less than or equal to Second Value : [" + secondValue + "]");
                }else{
                    rm.log("FAIL","First Value :[" + firstValue + "] is not less than or equal to Second Value : [" + secondValue + "]");
                }
                break;
        }
    }

    /***
     * This wrapper method will verify if file exists or not in a given directory
     */
    public void verifyFileExists(){
        String directoryName = System.getProperty("user.home")+"\\Downloads";
        String fileName = null;
        String fileNameStartsWith = null;
        String fileNameEndsWith = null;
        boolean isExactMatch = Cache.getCacheBoolean("isExactMatch")!= null ?Cache.getCacheBoolean("isExactMatch"):true;
        boolean status = false;

        ArrayList<String> fileList = helper.getFileList(directoryName);

        for (String file: fileList){
            if (isExactMatch){
                if (file.equalsIgnoreCase(fileName)){
                    status = true;
                    break;
                }
            }else{
                if (file.startsWith(fileNameStartsWith) && file.endsWith(fileNameEndsWith)){
                    fileName = file;
                    status = true;
                    break;
                }
            }
        }
        if (status){
            rm.log("FAIL","fileName : ["+fileName+"] exists");
        }else {
            rm.log("FAIL","fileName : ["+fileName+"] does not exists");
            Assert.fail("fileName : ["+fileName+"] does not exists");
        }
    }


    public void deleteFilesBasedOnRegex(String fileNameRegex){
        SoftAssert softAssert = new SoftAssert();
        String errMsg = null;
        String directoryName = System.getProperty("USERPROFILE")+"\\Downloads";
        File dir = new File(directoryName);
        String [] files = dir.list((dirPath,name)->{
            return name.matches(fileNameRegex);
        });

        //Delete files if exists
        if (files.length!=0){
            for (String file : files){
                String filePath = directoryName+"\\"+file;
                File downloadFileName = new File(filePath);
                if (downloadFileName.exists()){
                    if (helper.deleteFile(filePath)){
                        System.out.println("fileName : ["+filePath+"] deleted successfully");
                    }else{
                        softAssert.fail("Delete fileName : ["+filePath+"] failed");
                    }
                }
            }
        }else{
            System.out.println("No File exists given match pattern");
        }
        softAssert.assertAll();
    }
}
