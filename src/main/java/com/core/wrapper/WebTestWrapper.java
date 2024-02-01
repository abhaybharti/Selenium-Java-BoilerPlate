package main.java.com.core.wrapper;

import com.codeborne.selenide.SelenideElement;
import com.testkit.config.reader.JsonFileReader;
import com.testkit.config.wait.WaitFor;
import com.testkit.helper.WebTestHelper;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;

public class WebTestWrapper {
    public HashMap<String, String> scriptTestData = new HashMap<>();
    HashMap<String, String> testConfigData = new HashMap<>();

    WebTestHelper testHelper = null;
    JsonFileReader jsonFileReader = null;

    public WebTestWrapper(HashMap<String, String> testConfigData) {
        this.testConfigData = testConfigData;

        String env = testConfigData.get("env")!=null ? testConfigData.get("env") : throwErrorWhenKeyNoExistInMap("testConfigData.get(\"env\") not passed in testConfig.ini file");
        jsonFileReader = new JsonFileReader("phptravels.json", env);
        this.testHelper = new WebTestHelper(jsonFileReader);
    }

    public String throwErrorWhenKeyNoExistInMap(String errorMessage){
        throwAssertionToStopTestExecution(errorMessage);
        return "Key Not Exist error";
    }

    public void throwAssertionToStopTestExecution(String errorMessage){
//        ITestResult iTestResult = Cache.getTestResult();
//        iTestResult.setStatus(ITestResult.FAILURE);
        Assert.fail(errorMessage);
    }

//    private TestWrapper(TestWrapperBuilderClass builder) {
//        this.configTestData = builder.scriptTestData;
//        this.testHelper = builder.testHelper;
//    }


    //Builder Class
//    public static class TestWrapperBuilderClass {
//
//        HashMap<String, String> scriptTestData = new HashMap<>();
//        TestHelper testHelper = null;
//
//        public TestWrapperBuilderClass(HashMap<String, String> configTestData) {
//            testHelper = new TestHelper.TestHelperBuilderClass(configTestData).setConfigTestData(scriptTestData).build();
//            System.out.println("Inside TestWrapperBuilderClass class");
//        }
//
//        public TestWrapperBuilderClass setConfigTestData(HashMap<String, String> scriptTestData) {
//            this.scriptTestData = scriptTestData;
//            return this;
//        }
//
//        public TestWrapper build() {
//            return new TestWrapper(this);
//        }
//    }


    public void login() {
        String browserName = testConfigData.get("browser")!=null ? testConfigData.get("browser") : throwErrorWhenKeyNoExistInMap("testConfigData.get(\"browser\") not passed in testConfig.ini file");
        String url = testConfigData.get("url")!=null ? testConfigData.get("url") : throwErrorWhenKeyNoExistInMap("testConfigData.get(\"url\") not passed in testConfig.ini file");
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

        String objName = locatorType+"="+locatorValue;
        SelenideElement selenideElement = testHelper.webActions.find(objName, WaitFor.EXPLICIT_WAIT);
        switch (objType) {
            case "toggle":
                break;
            case "checkbox":
            case "textbox":
            case "textarea":
                String currentVal = testHelper.getFieldValue(objType,selenideElement);
                //TO DO -> read savetestdata value from JSON & compare if currentVal is not equal to value, then only change
                testHelper.webActions.writeToInputBox(selenideElement, objValue);

            case "dropdown":
            case "multiselect":
            case "radiobutton":
            case "slider":
                //TO DO
                //create a store_test_variable object to store value in a map, so that we can access at run time
        }

    }



    public void verifyFieldValue() {

    }

}
