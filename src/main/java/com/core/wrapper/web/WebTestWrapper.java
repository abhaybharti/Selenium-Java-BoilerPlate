package main.java.com.core.wrapper.web;

import com.codeborne.selenide.SelenideElement;
import com.testkit.helper.web.WebTestHelper;
import com.testkit.utils.reader.JsonFileReader;
import com.testkit.utils.wait.WaitFor;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebTestWrapper {
    public HashMap<String, String> scriptTestData = new HashMap<>();
    HashMap<String, String> testConfigData = new HashMap<>();

    WebTestHelper testHelper = null;
    JsonFileReader jsonFileReader = null;
    static int stepCount = 0;

    public WebTestWrapper(HashMap<String, String> testConfigData) {
        this.testConfigData = testConfigData;

        String env =
                testConfigData.get("env") != null
                        ? testConfigData.get("env")
                        : throwErrorWhenKeyNoExistInMap(
                        "testConfigData.get(\"env\") not passed in testConfig.ini file");
        jsonFileReader = new JsonFileReader("phptravels.json", env);
        this.testHelper = new WebTestHelper(jsonFileReader);
    }

    public String throwErrorWhenKeyNoExistInMap(String errorMessage) {
        throwAssertionToStopTestExecution(errorMessage);
        return "Key Not Exist error";
    }

    public void throwAssertionToStopTestExecution(String errorMessage) {
        //        ITestResult iTestResult = Cache.getTestResult();
        //        iTestResult.setStatus(ITestResult.FAILURE);
        Assert.fail(errorMessage);
    }

    //    private TestWrapper(TestWrapperBuilderClass builder) {
    //        this.configTestData = builder.scriptTestData;
    //        this.testHelper = builder.testHelper;
    //    }

    // Builder Class
    //    public static class TestWrapperBuilderClass {
    //
    //        HashMap<String, String> scriptTestData = new HashMap<>();
    //        TestHelper testHelper = null;
    //
    //        public TestWrapperBuilderClass(HashMap<String, String> configTestData) {
    //            testHelper = new
    // TestHelper.TestHelperBuilderClass(configTestData).setConfigTestData(scriptTestData).build();
    //            System.out.println("Inside TestWrapperBuilderClass class");
    //        }
    //
    //        public TestWrapperBuilderClass setConfigTestData(HashMap<String, String> scriptTestData)
    // {
    //            this.scriptTestData = scriptTestData;
    //            return this;
    //        }
    //
    //        public TestWrapper build() {
    //            return new TestWrapper(this);
    //        }
    //    }

    public void login() {
        String browserName =
                testConfigData.get("browser") != null
                        ? testConfigData.get("browser")
                        : throwErrorWhenKeyNoExistInMap(
                        "testConfigData.get(\"browser\") not passed in testConfig.ini file");
        String url =
                testConfigData.get("url") != null
                        ? testConfigData.get("url")
                        : throwErrorWhenKeyNoExistInMap(
                        "testConfigData.get(\"url\") not passed in testConfig.ini file");
        testHelper.webActions.startBrowser(browserName);
        testHelper.webActions.goToUrl(url);
    }

    public void changeFieldValue() throws IOException {
        String elementName = "";
        String locatorType = jsonFileReader.getElementData(elementName, "locatorType");
        String locatorValue = jsonFileReader.getElementData(elementName, "locatorValue");
        String objType = jsonFileReader.getElementData(elementName, "objType");

        String objValue = "";
        if (scriptTestData.containsKey("fieldValue")) {
            objValue = scriptTestData.get("fieldValue");
        }

        String objName = locatorType + "=" + locatorValue;
        SelenideElement selenideElement = testHelper.webActions.find(objName, WaitFor.EXPLICIT_WAIT);
        if (null == selenideElement) {
            Assert.fail("Setting : " + objName + " not found on page");
        }
        StringBuilder valueBeforeChange = null;
        StringBuilder valueAfterChange = null;
        switch (objType) {
            case "toggle":
                //get current value toggle object - getToggleStatus()
                // check if current value is different than expectedValue
                // if yes, call changeToggleStatus() method
                //else do nothing

                //read new status from toggle & print in HTML report
                break;
            case "checkbox":
                //get current value toggle object - getToggleStatus()
                // check if current value is different than expectedValue
                // if yes, call changeToggleStatus() method
                //else do nothing

                //read new status from toggle & print in HTML report
            case "textbox":
            case "textarea":
                String currentVal = testHelper.getFieldValue(objType, selenideElement);
                // TO DO -> read savetestdata value from JSON & compare if currentVal is not equal to value,
                // then only change
                if (!currentVal.equals(objValue)) {
                    testHelper.webActions.writeToInputBox(selenideElement, objValue);
                }
                String changedVal = testHelper.getFieldValue(objType, selenideElement);

                //TO DO - add statement to write in HTML file new value & object type

            case "dropdown":
            case "multiselect":
            case "radiobutton":
            case "slider":
                // TO DO
                // create a store_test_variable object to store value in a map, so that we can access at run
                // time
        }
    }

    public void verifyFieldValue() {
    /* Logic    
    1. Read value from UI
    * 2. get Exepcted value
    *   1. passed from Script
    *   2. read from env variable
    * 3. Compare UI & expected value
    * 4. put assertion & write in HTML
    * */
    }

    public void stepPrint(String message, int stepNo) {
        stepCount = stepCount + 1;
        String iteration = "";
        if (stepNo != 0) {
            iteration = " Iteration [" + stepNo + "]";
        }
//    report.log("INFO","<br> Step ["+stepCount+"] ,  "iteration+message+"</b>");
    }

    public void verifyObjectIsNotDisplayed() {
// TO DO -- add code to verify element is not displayed
    }

    public void verifyObjectIsDisplayed() {
        // TO DO -- add code to verify element is displayed
    }

    public void verifyObjectIsEditable() {
// TO DO -- add code to verify element is editable

    }

    public void verifyObjectIsNotEditable() {
// TO DO -- add code to verify element is not editable


    }

    public void verifyTableHeaderNames() {

    }

    public void verifyDropDownValues() {

    }

    public void verifyFileExists() {
        //TO DO --> add code logic to check if file exists

    }


    public void deleteFile() {
        //TO DO --> add code logic to delete existing file
    }

    public void verifyGrowlMessage() {
        //TO DO --> add code logic to verify growl message
    }

    public void switchToNewBrowserWindow() {

    }

    public void switchBackToParentBrowserWindow() {

    }

    public void switchToCloseCurrentBrowserWindow() {

    }

    public void disableNetworkTrafficCapture() {

    }

    public void enableNetworkTrafficCapture() {

    }

    public void checkKeyExistsInTestDataMap() {

    }

    public void verifyToolTipIconAndToolTipMessage() {

    }

    public void verifyTableBody(String tableName) {
        SoftAssert softAssert = new SoftAssert()
        boolean result = false;
        String [] tableNameBase = tableName.split("\\.");
        String tableBodyHierarchy = tableName + "." + tableNameBase[tableNameBase.length - 1] + "Body";
        int columnNumber = Integer.parseInt("columnNumber");
        result = testHelper.verifyTableColumnValues(tableName, tableBodyHierarchy, columnNumber);

        if (result){
            rm.log("INFO", "Table ["+tableName+"] Body verification for columnNumber ["+columnNumber+"] is passed");
        }else{
            rm.log("FAIL", "Table ["+tableName+"] Body verification for columnNumber ["+columnNumber+"] is passed");
        }

        softAssert.assertAll();
    }

    public void verifyTabNames(){
        ArrayList<String> uiTabNames = testHelper.getTabNames();
        ArrayList<String> expectedTabNames = jsonFileReader.getTabNames();
        testHelper.compareList(uiTabNames, expectedTabNames,"In Application", "Tabs");
    }

    public void verifyUxFieldsInAccordion(String accordionName,String tabName){
        if (!testHelper.verifyFieldLabelsInAccordion(accordionName,tabName)){
            rm.log("FAIL", "Accordion ["+accordionName+"] verification for tab ["+tabName+"] is passed");
        }
    }

}
