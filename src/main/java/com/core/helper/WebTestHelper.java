package main.java.com.core.helper;

import com.codeborne.selenide.SelenideElement;
import com.testkit.actions.web.ChromeBrowserActions;
import com.testkit.actions.web.EdgeBrowserActions;
import com.testkit.actions.web.FireFoxBrowserActions;
import com.testkit.actions.web.SeleniumActions;
import com.testkit.config.reader.JsonFileReader;

import java.util.HashMap;

public class WebTestHelper {

    public static HashMap<String, String> testConfigData = new HashMap<>();
    public static HashMap<String, String> scriptTestData = new HashMap<>();

    JsonFileReader jsonFileReader = null;

    public SeleniumActions webActions = null;

    public WebTestHelper() {
        //this.webActions = createBrowserActions("CHROME");
        webActions = new SeleniumActions();

        //jsonFileReader = new JsonFileReader("", "");
    }

    public WebTestHelper(JsonFileReader jsonFileReader) {
        //this.webActions = createBrowserActions("CHROME");
        webActions = new SeleniumActions();
        this.jsonFileReader = jsonFileReader;
    }
//    private TestHelper(TestHelperBuilderClass builder){
//        this.testConfigData = builder.configTestData;
//    }

//    public static class TestHelperBuilderClass{
//
//        HashMap<String,String>  configTestData = new HashMap<>();
//        public WebActions webActions = null;
//
//        public  TestHelperBuilderClass(HashMap<String, String> configTestData){
//            this.configTestData = configTestData;
//            System.out.println("Inside TestHelperBuilderClass class");
//        }
//
//        public TestHelperBuilderClass setConfigTestData(HashMap<String, String> configTestData){
//            this.configTestData = configTestData;
//
//            this.webActions = webActions.createBrowserActions()
//            return this;
//        }
//
//        public TestHelper build(){
//            return new TestHelper(this);
//        }
//    }

    public SeleniumActions createBrowserActions(String browserType) {
        if (browserType.equalsIgnoreCase("Chrome")) {
            return new ChromeBrowserActions();
        } else if (browserType.equalsIgnoreCase("firefox")) {
            return new FireFoxBrowserActions();
        } else if (browserType.equalsIgnoreCase("edge")) {
            return new EdgeBrowserActions();
        } else {
            return new ChromeBrowserActions();
        }
    }

    public void login() {
        webActions.startBrowser("CHROME");
    }


    public String getFieldValue(String objType, SelenideElement elementName) {
        String returnValue = "";
        switch (objType) {
            case "toggle":
                getToggleValue(elementName);
                break;
            case "checkbox":
                //method to read checkbox value
            case "textbox":
            case "textarea":
                getInputFieldValue(elementName);
                break;
            case "dropdown":
                getDropdownValue(elementName);
            case "multiselect":
                //method to read textbox value
            case "radiobutton":
                getRadiobuttonValue(elementName);
            case "slider":
                //method to read textbox value
        }
        return returnValue;
    }

    private String getRadiobuttonValue(SelenideElement elementName) {
        return elementName.getText();
    }

    private String getDropdownValue(SelenideElement elementName) {
        return elementName.getText();
    }

    private String getInputFieldValue(SelenideElement elementName) {
        if (elementName.getAttribute("value") != null) {
            return elementName.getAttribute("value");
        }else{
            return elementName.getText();
        }
    }

    public boolean getToggleValue(SelenideElement element){
        return element.isSelected()?true:false;
    }
}




