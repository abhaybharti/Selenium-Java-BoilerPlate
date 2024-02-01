package main.java.com.core.helper.web;

import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.testkit.actions.web.ChromeBrowserActions;
import com.testkit.actions.web.EdgeBrowserActions;
import com.testkit.actions.web.FireFoxBrowserActions;
import com.testkit.actions.web.SeleniumActions;
import com.testkit.helper.Helper;
import com.testkit.utils.reader.JsonFileReader;
import com.testkit.utils.wait.WaitFor;
import io.restassured.mapper.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WebTestHelper extends Helper {
    //  protected Logger logger = Logger.getLogger(SafariBrowserActions.class);
    public static HashMap<String, String> testConfigData = new HashMap<>();
    public static HashMap<String, String> scriptTestData = new HashMap<>();
    ArrayList<JsonObject> fieldsUnderAccordion = null;

    JsonFileReader jsonFileReader = null;

    public SeleniumActions webActions = null;

    public WebTestHelper() {
        // this.webActions = createBrowserActions("CHROME");
        webActions = new SeleniumActions();

        // jsonFileReader = new JsonFileReader("", "");
    }

    public WebTestHelper(JsonFileReader jsonFileReader) {
        // this.webActions = createBrowserActions("CHROME");
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

    /**
     * The function creates and returns an instance of a specific browser's actions based on the given
     * browser type.
     *
     * @param browserType The parameter "browserType" is a String that represents the type of browser for
     *                    which the SeleniumActions object needs to be created. It can have values such as "Chrome",
     *                    "firefox", or "edge".
     * @return The method is returning an object of type SeleniumActions.
     */
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

    /**
     * The login function starts the Chrome browser.
     */
    public void login() {
        webActions.startBrowser("CHROME");
    }

    /**
     * The function `getFieldValue` takes an object type and a SelenideElement and returns the value of the
     * corresponding field.
     *
     * @param objType     The objType parameter is a String that represents the type of element you want to get
     *                    the field value from. It can have the following values:
     * @param elementName The elementName parameter is of type SelenideElement, which represents an element
     *                    on a web page. It can be used to interact with and retrieve information from the element.
     * @return The method is returning a String value.
     */
    public String getFieldValue(String objType, SelenideElement elementName) {
        String returnValue = "";
        switch (objType) {
            case "toggle":
                getToggleValue(elementName);
                break;
            case "checkbox":
                getCheckBoxCurrentStatus(elementName);
            case "textbox":
            case "textarea":
                getInputFieldValue(elementName);
                break;
            case "dropdown":
                getDropdownValue(elementName);
            case "multiselect":
                // method to read textbox value
            case "radiobutton":
                getRadiobuttonValue(elementName);
            case "slider":
                // method to read textbox value
        }
        return returnValue;
    }

    /**
     * The function returns the text value of a given SelenideElement representing a radio button.
     *
     * @param elementName The parameter `elementName` is of type `SelenideElement`, which is a class
     *                    provided by the Selenide framework for interacting with web elements in a more convenient way. It
     *                    represents a single element on a web page and provides various methods for interacting with it, such
     *                    as clicking, typing
     * @return The method is returning the text value of the given SelenideElement.
     */
    private String getRadiobuttonValue(SelenideElement elementName) {
        return elementName.getText();
    }


    public String changeRadioButtonStatus(SelenideElement element) {
        //add code to chagne radio button value
        return "valie";
    }

    /**
     * The function "getDropdownValue" returns the text value of a given SelenideElement.
     *
     * @param elementName The parameter `elementName` is of type `SelenideElement`, which is a class
     *                    provided by the Selenide framework for interacting with web elements in automated tests.
     * @return The method is returning the text value of the given SelenideElement.
     */
    private String getDropdownValue(SelenideElement elementName) {
        return elementName.getText();
    }

    /**
     * The function returns the value of an input field or the text content of an element.
     *
     * @param elementName The parameter `elementName` is of type `SelenideElement`, which is a class
     *                    provided by the Selenide framework for interacting with web elements in automated tests. It
     *                    represents a single element on a web page and provides methods for interacting with and retrieving
     *                    information from that element.
     * @return The method is returning the value of the input field. If the input field has a "value"
     * attribute, it will return the value of that attribute. Otherwise, it will return the text content of
     * the input field.
     */
    private String getInputFieldValue(SelenideElement elementName) {
        if (elementName.getAttribute("value") != null) {
            return elementName.getAttribute("value");
        } else {
            return elementName.getText();
        }
    }

    /**
     * The function returns true if the given SelenideElement is selected, otherwise it returns false.
     *
     * @param element The parameter "element" is of type SelenideElement, which is a class provided by the
     *                Selenide framework for interacting with web elements in automated tests.
     * @return The method is returning a boolean value.
     */
    public boolean getToggleValue(SelenideElement element) {
        return element.isSelected() ? true : false;
    }


    public boolean changeToggleValue(SelenideElement element) {
        //TO DO
        // write code to change toggle value
        return false;
    }


    /**
     * The function `changeCheckBoxStatus` changes the status of a checkbox element until it matches the
     * expected status.
     *
     * @param element        The element is a SelenideElement, which represents a checkbox element on a web page.
     * @param expectedStatus The expected status is a string that represents the desired status of the
     *                       checkbox. It can be either "True" or "False".
     * @return The method is returning a boolean value.
     */
    public boolean changeCheckBoxStatus(SelenideElement element, String expectedStatus) {
        boolean retVal = false;
        String actualStatus = getCheckBoxCurrentStatus(element) ? "True" : "False";
        int iCount = 1;
        while (!actualStatus.equals(expectedStatus)) {
            if (expectedStatus.equals("True")) {
                selectCheckBox(element);
            } else if (expectedStatus.equals("False")) {
                unSelectCheckBox(element);
            }
            actualStatus = getCheckBoxCurrentStatus(element) ? "True" : "False";
            if (actualStatus.equals(expectedStatus)) {
                retVal = true;
                break;
            }

            if (iCount == WaitFor.APPLICATION_RETRY_MIN_COUNT) {
                break;
            }
        }
        return retVal;
    }

    /**
     * The function is used to select a checkbox element in Selenide.
     *
     * @param element The element parameter is a SelenideElement object that represents a checkbox element
     *                on a web page.
     */
    public void selectCheckBox(SelenideElement element) {


    }

    /**
     * The function unSelectCheckBox takes a SelenideElement as a parameter and does not have any
     * implementation.
     *
     * @param element The element parameter is a SelenideElement object that represents a checkbox element
     *                on a web page.
     */
    public void unSelectCheckBox(SelenideElement element) {

    }

    /**
     * The function returns the current status (selected or not selected) of a checkbox element.
     *
     * @param element The SelenideElement parameter represents a checkbox element on a web page.
     * @return The method is returning a boolean value, which indicates the current status of a checkbox.
     */
    public boolean getCheckBoxCurrentStatus(SelenideElement element) {
        return element.isSelected();
    }

    /**
     * The function is intended to close a growl message if it is present.
     *
     * @return The method is returning a boolean value of true.
     */
    public boolean closeGrowlIfPresent() {
        //TO DO -- add code to close growl message
        return true;
    }

    /**
     * The function captureScreenshotFullPage is a placeholder that currently returns an empty string and
     * needs to be implemented to capture a screenshot of the full page.
     *
     * @return An empty string is being returned.
     */
    public String captureScreenshotFullPage() {
        //TO DO -- add code to capture screenshot of full page
        return "";
    }

    /**
     * The function is a placeholder for capturing a screenshot of a specific element on a webpage.
     *
     * @return An empty string is being returned.
     */
    public String captureScreenshotOfElement() {
        //TO DO -- add code to capture screenshot of full page
        return "";
    }

    public ArrayList<String> getAccordionNames(String tabName) {
        StringBuilder locatorType = new StringBuilder(jsonFileReader.getElementData("Application.CommonElements.Accordions", "locatorType"));
        StringBuilder locatorValue = new StringBuilder(jsonFileReader.getElementData("Application.CommonElements.Accordions", "locatorValue"));
        String locator = locatorType + "=" + locatorValue;
        List<SelenideElement> selenideElements = seleniumActions.finds(locator, 10);
        ArrayList<String> out = new ArrayList<>();
        for (SelenideElement se : selenideElements) {
            String accordionName = se.getText().trim();
            accordionName = accordionName.split("\\r?\\n")[0];

            if (!StringUtils.isEmpty(accordionName)) {
                out.add(accordionName);
            }
        }
        return out;
    }

    /***
     * This method returns the list of Fields Displayed at Accordion level
     * @param jsonObjectsList
     * @return
     */
    public ArrayList<String> getElementsLabelsInAccordion(ArrayList<JsonObject> jsonObjectsList) {
        ArrayList<String> labelName = new ArrayList<>();

        for (JsonObject jsonObj : jsonObjectsList) {
            StringBuilder locatorType = new StringBuilder(jsonFileReader.getElementFromJsonObject(jsonObj, "locatorType"));
            StringBuilder locatorValue = new StringBuilder(jsonFileReader.getElementFromJsonObject(jsonObj, "locatorValue"));

            SelenideElement selenideElement = seleniumActions.find(locatorType + "=" + locatorValue, 10);
            if (selenideElement != null) {
                labelName.add(selenideElement.getText().trim());
            } else {
                labelName.add("Element [" + locatorType + "=" + locatorValue + "] Not Found");
            }
        }
        return labelName;
    }

    /***
     * This method verifies element labels in Accordion
     * @param accordionName
     * @param tabName
     * @return
     */
    public boolean verifyFieldLabelsInAccordion(String accordionName, String tabName) {
        boolean retVal = false;
        try {
            String hierarchy = "Application." + tabName + "." + accordionName.replaceAll("\\s", "");
            if (tabName.contains("\\.")) {
                hierarchy = tabName + "." + accordionName.replaceAll("\\s", "");
            }
            fieldsUnderAccordion = jsonFileReader.getElementLabels(hierarchy);
            if (fieldsUnderAccordion.isEmpty()) {
                return true;
            }

            retVal = setDependentValue("Accordion");
            if (!retVal) {
                retVal = true;
                System.out.println("Setting Dependent Value fail while enabling toggle");
            }

            for (JsonObject jsonObject : fieldsUnderAccordion) {
                StringBuilder labelName = new StringBuilder(jsonFileReader.getElementFromJsonObject(jsonObject, "labelName"));
                StringBuilder fieldHierarchy = new StringBuilder(hierarchy + "." + labelName.toString().replaceAll("\\s", ""));
            }

            ArrayList<String> uiFieldsUnderAccordion = getElementsLabelsInAccordion(fieldsUnderAccordion);
            String inMessage = "in Accordion [" + accordionName + "]";

            retVal = compareUiValues(fieldsUnderAccordion, inMessage) && retVal;
            retVal = compareList(uiFieldsUnderAccordion, jsonFileReader.getElementsFromJsonObject(fieldsUnderAccordion, "labelName"), inMessage, "Field Label", getElementsType(fieldsUnderAccordion, "UI"), getElementsType(fieldsUnderAccordion, "JSON") && retVal)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }


    public String getElementTypeFromPortal(String locatorType, String element, String elementType) {

    }

    /**
     * This method return element type based on the tag name
     *
     * @param tagName
     * @return
     */

    public String getElementType(String tagName) {
        String elementType = null;
        switch (tagName) {
            case "input":
            case "textarea":
                elementType = "textbox";
                break;
            case "p-spinner":
                elementType = "spinner";
                break;
            case "p-inputswitch":
                elementType = "toggle";
                break;
            case "dropdown":
                elementType = "dropdown";
                break;
            case "p-radiobutton":
                elementType = "radiobutton";
                break;
            case "p-checkbox":
                elementType = "checkbox";
                break;
            case "a":
                elementType = "link";
                break;
            default:
                elementType = "Element Type not found for Tag name " + tagName;
        }
        return elementType;
    }

    public boolean poolForMessage(String fieldName, String message, String appearOrDisappear, boolean isTable, HashMap<String, String> map) {
        boolean retVal = false;
        int startTime = 0;
        int endTime = WaitFor.MAX_IMPLICIT_TIME_SEC;
        StringBuilder actualMsg = new StringBuilder("");

        if (appearOrDisappear.equalsIgnoreCase("appear")) {
            while (startTime <= endTime) {
                actualMsg = seleniumActions.getText(seleniumActions.find(fieldName, WaitFor.EXPLICIT_WAIT));
                if (!actualMsg.toString().equalsIgnoreCase(message)) {
                    retVal = true;
                    break;
                }
                startTime = startTime + WaitFor.POOLING_TIME_SEC;
                waitForSeconds(WaitFor.POOLING_TIME_SEC);
            }
        }
        if (appearOrDisappear.equalsIgnoreCase("disappear")) {
            while (startTime <= endTime) {
                actualMsg = seleniumActions.getText(seleniumActions.find(fieldName, WaitFor.EXPLICIT_WAIT));
                if (actualMsg.toString().equalsIgnoreCase(message)) {
                    retVal = true;
                    break;
                }
                startTime = startTime + WaitFor.POOLING_TIME_SEC;
                waitForSeconds(WaitFor.POOLING_TIME_SEC);
            }
        }
        if (!retVal) {
            System.out.println("Pooling for message - " + message + " to be " + appearOrDisappear + " for time - " + endTime + " seconds, Failed");
        }
        return retVal;
    }

    /***
     * This method returns list of table header names
     * @param tableName
     * @return
     */
    public ArrayList<String> getTableHeader(String tableName) {
        ArrayList<String> headerName = new ArrayList<>();
        String[] tableBaseName = tableName.split("\\.");
        String tableHeaderPath = tableName + "." + tableBaseName[tableBaseName.length - 1] + "Header";
        String tableHeaderLocator = jsonFileReader.getElementData(tableHeaderPath, "locatorType") + "=" + jsonFileReader.getElementData(tableHeaderPath, "locatorValue");
        tableHeaderLocator = tableHeaderLocator + "//th[not (contains(@class,'hidden'))][not(@hidden]";
        List<SelenideElement> selenideElementList = seleniumActions.finds(tableHeaderLocator, WaitFor.APPLICATION_RETRY_MID_COUNT);
        for (SelenideElement se : selenideElementList) {
            headerName.add(se.getText());
        }
        return headerName;
    }

    /***
     * This method returns element type from UI
     * @param fieldName
     * @return
     */
    public String getFiledTypeFromUi(String fieldName) {
        String locatorType = jsonFileReader.getElementData(fieldName, "locatorType");
        String locatorValue = jsonFileReader.getElementData(fieldName, "locatorValue");
        String elementType = jsonFileReader.getElementData(fieldName, "elementType");
        String el = locatorType + "=" + locatorValue;
        String labelType = null;
        List<SelenideElement> labelList = seleniumActions.finds(el, 10);
        if (!labelList.isEmpty()) {
            for (SelenideElement se : labelList) {
                labelType = getElementTypeFromPortal(labelType, locatorValue, elementType);
            }
        }
        return labelType;
    }

    /***
     * This method return type of the element
     * @param locatorType
     * @param locatorValue
     * @return
     */
    public String getElementType(String locatorType, String locatorValue) {
        SelenideElement se = seleniumActions.find(locatorType + "=" + locatorValue, 10);
        String tagName = "";
        String className = "";
        String elementType = "";
        if (se != null) {
            tagName = se.getTagName();
            if (tagName.equalsIgnoreCase("div")) {
                className = se.getAttribute("class");
            }
            elementType = getElementType(tagName);
        }
        return elementType;
    }

    /***
     * This method get element Type directly under tab and validates it with test value (JSON Data)
     * @param tabName
     * @return
     */
    public boolean verifyFieldElementsTypeInTab(String tabName) {
        String jsonPath = null;
        if (tabName.contains("\\.")) {
            jsonPath = tabName;
        } else {
            jsonPath = "Application." + tabName.replaceAll("\\s", "");
        }

        ArrayList<JsonObject> jsonElement = jsonFileReader.getElementLabels(jsonPath);
        ArrayList<String> expectedLabel = jsonFileReader.getElementsFromJsonObject(jsonElement, "elementType");
        ArrayList<String> actualLabel = new ArrayList<>();
        String locatorType = null;
        String locatorValue = null;
        for (JsonObject jsonObject : jsonElement) {
            locatorType = jsonObject.get("locatorType").getAsString();
            locatorValue = jsonObject.get("locatorValue").getAsString();
            actualLabel.add(getElementType(locatorType, locatorValue));
        }
        return compareList(actualLabel, expectedLabel, " in " + tabName, "Field Element Type") && compareUiValues(jsonElement, " In Tab [" + tabName + "]");
    }

    /***
     * This method will get the headers of the table and validate it with the test data
     * @param tabName
     * @param tableHeaderPath
     * @return
     */
    public boolean verifyTableHeaders(String tabName, String tableHeaderPath) {
        ArrayList<JsonObject> jsonElement = jsonFileReader.getElementLabels(tableHeaderPath);
        ArrayList<String> expectedLabel = jsonFileReader.getElementsFromJsonObject(jsonElement, "lableName");
        ArrayList<String> actualLabel = jsonFileReader.getElementsFromJsonObject(jsonElement, "lableName");
        String locatorType = null;
        String locatorValue = null;
        for (JsonObject jsonObject : jsonElement) {
            locatorType = jsonObject.get("locatorType").getAsString();
            locatorValue = jsonObject.get("locatorValue").getAsString();
            SelenideElement se = seleniumActions.find(locatorType + "=" + locatorValue, WaitFor.EXPLICIT_WAIT);
            StringBuilder tempText = new StringBuilder(se.getText().split("\\r?\\n")[0]);
            actualLabel.add(tempText.toString());
        }

        return compareList(actualLabel, expectedLabel, " in " + tabName + " Table ", "Table Header");
    }


    /***
     * This method gets the value of text in all the rows of the specified column(columnNumber) and validate it with test value (JSON Data)
     * @param tableName
     * @param bodyHierarchy
     * @param columnNumber
     * @return
     */
    public boolean verifyTableColumnValues(String tableName, String bodyHierarchy, int columnNumber) {
        ArrayList<JsonObject> jsonElement = jsonFileReader.getElementLabels(bodyHierarchy);
        int countOfRows = jsonElement.size();
        String columnPath = null;
        ArrayList<String> expectedLabel = new ArrayList<>();
        ArrayList<String> actualLabel = new ArrayList<>();
        String locatorType = null;
        String locatorValue = null;

        for (int iCount = 0; iCount < countOfRows; iCount++) {
            columnPath = bodyHierarchy + ".Row" + iCount + ".column" + columnNumber;
            if (!verifySupportedModelAndUxVerification(columnPath)) {
                continue;
            }
            locatorType = jsonFileReader.getElementData(columnPath, "locatorType");
            locatorValue = jsonFileReader.getElementData(columnPath, "locatorType");
            String labelName = jsonFileReader.getElementData(columnPath, "lableName");
            expectedLabel.add(labelName);
            SelenideElement se = seleniumActions.find(locatorType + "=" + locatorValue, WaitFor.EXPLICIT_WAIT);
            actualLabel.add(se.getText());
        }
        Collections.sort(actualLabel);
        Collections.sort(expectedLabel);
        return compareList(actualLabel, expectedLabel, " in " + tableName + " Table Body ", "");
    }


    /***
     * Verify UX supported model and isUxVerification
     * @param hierarchy
     * @return
     */
    public boolean verifySupportedModelAndUxVerification(String hierarchy) {
        boolean retVal = true;
        if (jsonFileReader.getElementData(hierarchy, "NotSupportedModels").contains(testConfigData.get("deviceType"))) {
            return false;
        }
        if (jsonFileReader.getElementData(hierarchy, "UXVerification").contains("NO")) {
            return false;
        }
        return retVal;
    }

    /***
     * This function will return the HashMap of all fileds [name as  hierarchy] and values for the given Fields List
     * @param fieldNameList
     * @param tabName
     * @return
     */

    public HashMap<String, String> getFieldValuesMap(ArrayList<String> fieldNameList, String tabName) {
        HashMap<String, String> fieldValuesMap = new HashMap<>();
        for (String fieldName : fieldNameList) {
            String fieldValue = getFieldValue(fieldName, tabName);
            if (fieldValue != null) {
                fieldValuesMap.put(fieldName, fieldValue);
            }
        }
        return fieldValuesMap;
    }

    /***
     * This method compare actualMap and expectedMap and return true if they are similar
     * 1. verify the number of keys are same
     * 2. verify all keys are same as in expected Map
     * 3. verify all keys have same value as in expected Map
     * @param expectedMap
     * @param actualMap
     * @return
     */

    public boolean compareMap(HashMap<String, String> expectedMap, HashMap<String, String> actualMap) {
        boolean result = true;
        if (expectedMap.size() != actualMap.size()) {
            result = false;
        }

        for (String key : expectedMap.keySet()) {
            String expectedValue = expectedMap.get(key);
            if (actualMap.containsKey(key)) {
                String actualValue = actualMap.get(key);
                if (expectedValue.equalsIgnoreCase(actualValue)) {
                    System.out.println("Key Matched : ExpectedKey [" + key + "] ExpectedValue [" + expectedValue + "] ActualValue [" + actualValue + "]");
                } else {
                    System.out.println("Key Not Matched : ExpectedKey [" + key + "] ExpectedValue [" + expectedValue + "] ActualValue [" + actualValue + "]");
                    result = false;
                }
                actualMap.remove(key);
            } else {
                System.out.println("Key Not Found in Actual Map : ExpectedKey [" + key + "] ExpectedValue [" + expectedValue + "]");
                result = false;
            }
        }

        for (String key : actualMap.keySet()) {
            String actualValue = actualMap.get(key);
            System.out.println("Key Not Found in Expected Map : ActualKey [" + key + "] ActualValue [" + actualValue + "]");
            result = false;
        }
        return result;
    }

    /***
     * This is a generic method to create required Json String which is required for API to change the settings in device level
     * @param baseApi
     * @param fieldNameList
     * @param fieldValueList
     * @return
     */
    public String getApiDataJsonObject(String baseApi, String fieldNameList, ArrayList<Object> fieldValueList) {
        ObjectMapper mappper = new ObjectMapper();
        JsonNode rootNode = mappper.createObjectNode();
        JsonNode childNodeOne = mappper.createObjectNode();

        String[] baseApiArray = baseApi.split("\\/");
        String[] fieldNameListArray = fieldNameList.split("\\/");

        if (fieldNameListArray.length != fieldValueList.size()) {
            System.out.println("ERROR : Device field Name list and value list does not match");
            return null;
        }

        for (int iCount = 0; iCount < fieldNameListArray.length; iCount++) {
            String className = fieldValueList.get(iCount).getClass().toString();
            if (className.contains("String")) {
                ((ObjectNode) childNodeOne).put(fieldNameListArray[iCount], fieldValueList.get(iCount).toString());
            } else if (className.contains("Integer")) {
                ((ObjectNode) childNodeOne).put(fieldNameListArray[iCount], Integer.valueOf(fieldValueList.get(iCount).toString()));
            } else if (className.contains("Boolean")) {
                ((ObjectNode) childNodeOne).put(fieldNameListArray[iCount], Boolean.valueOf(fieldValueList.get(iCount).toString()));
            } else if (className.contains("JSONArray")) {
                ((ObjectNode) childNodeOne).putPOJO(fieldNameListArray[iCount], new JSONArray(fieldValueList.get(iCount).toString()));
            }
        }

        JsonNode tempRootNode = null;
        for (int iCount = 0; iCount < baseApiArray.length; iCount++) {
            tempRootNode = mappper.createObjectNode();
            ((ObjectNode) tempRootNode).set(baseApiArray[iCount], childNodeOne);
            childNodeOne = tempRootNode;
        }
        ((ObjectNode) rootNode).set(baseApiArray[0], childNodeOne);
        String jsonString = null;
        try {
            jsonString = mappper.writeWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException je) {
            jsonString = rootNode.toString();
        }
        return jsonString;
    }

    /***
     * This helper method check if UXValdation is provided false or not.
     * @param hierarchy
     * @return
     */
    public boolean isUxValidationAllowed(String hierarchy) {
        String isUxValidationAllowed = jsonFileReader.getElementData(hierarchy, "UXValidation");
        if (isUxValidationAllowed.equalsIgnoreCase("false")) {
            return false;
        }
        return true;
    }

    /***
     * This method takes string array as input arguments and returns string arraylist
     * @param accordionArray
     * @return
     */
    public ArrayList<String> convertToArrayList(String[] accordionArray) {
        ArrayList<String> accordionList = new ArrayList<>();
        for (String accordion : accordionArray) {
            accordionList.add(accordion);
        }
        return accordionList;
    }

    /***
     * This helper method takes integer array as input (having size is two, where first element is hour and second element is minute value) and interval time to set schedule time. It computes schedule time and returns as integer array
     * @param hour
     * @param minute
     * @return
     */
    public int [] computeHourAndMinute(int schduledAfterMinute, int[] setHourAndMinute){
        int [] hourMinute = new int[2];
        if (setHourAndMinute.length == 2){
            if ((setHourAndMinute[1]+schduledAfterMinute) >= 60){
                setHourAndMinute[1] = (setHourAndMinute[1]+schduledAfterMinute) - 60;
                //set hour value
                setHourAndMinute[0] = setHourAndMinute[0] + 1;
            }else{
                //set minute value
                setHourAndMinute[1] = setHourAndMinute[1] + schduledAfterMinute;
            }
            hourMinute[0] = setHourAndMinute[0];
            hourMinute[1] = setHourAndMinute[1];
        }
        return hourMinute;
    }

    public boolean changeSlider(String sliderLocator, String sliderPercentLocator,String changeValue){
        boolean result = false;
        try{
            double deviceMultiplier = 3.0;
            int currentSliderValue = Integer.parseInt(getSliderValue(sliderPercentLocator));
            int expectedSliderValue = Integer.valueOf(changeValue);
            int counter = 0;
            int pixelToDrag = (int) ((expectedSliderValue - currentSliderValue) * deviceMultiplier);
            while (currentSliderValue != expectedSliderValue) {
                pixelToDrag = currentSliderValue>expectedSliderValue?-3:1;

                //Drag
                if (!dragSlider(sliderLocator, pixelToDrag)){
                    break;
                }

                currentSliderValue = Integer.valueOf(getFieldValue(sliderPercentLocator));

                if (counter>10){
                    System.out.println("10 iteration tried, but did not drag slider to the desired value. Expected : ["+expectedSliderValue+"] Actual : ["+currentSliderValue+"]");
                    break;
                }
                counter++;
            }

            if (currentSliderValue == expectedSliderValue){
                System.out.println("Slider Value changed successfully. Expected : ["+expectedSliderValue+"] Actual : ["+currentSliderValue+"]");
                result = true;
            }else{
                System.out.println("Slider Value is not changed  value : ["+expectedSliderValue+"] Current Value is : ["+currentSliderValue+"]");
            }
        }catch (Exception e){
            System.out.println("Excpetion occured is changing the slider value - "+e);
            result = false;
        }
        return result;
    }

    public String getSliderValue(String sliderPercentLocator){
        String sliderValue = "";
        try{
            sliderValue = seleniumActions.find(sliderPercentLocator,WaitFor.EXPLICIT_WAIT).getText();
            if (sliderValue!=null){
                sliderValue = sliderValue.split("\\s+")[0];
                sliderValue=sliderValue.replaceAll("%","");
                return sliderValue;
            }else{
                System.out.println("Not able to fetch slider value as object does not exist for locator ["+sliderPercentLocator+"]");
                return "Not able to fetch slider value as object does not exist for locator ["+sliderPercentLocator+"]");
            }
        }catch (Exception e){
            System.out.println(e);
            return "Exception occured in fetching the slider value";
        }
    }

    public boolean dragSlider(SelenideElement se, int pixelToDrag){
        Actions builder = new Actions(seleniumActions.driver);
        se.click();
        builder.moveToElement(se).click().dragAndDropBy(se, pixelToDrag, 0).build().perform();
        return true;
    }

    public int getRowCountOfTable(List<SelenideElement> se){
        int rowCount = 0;
        try{
            rowCount = se.size();
            if (!isTableRecordFound()){
                rowCount = 0;
            }
        }catch (Exception e){
            System.out.println("Failed to get Row Count from table ");
        }
        return rowCount;
    }


    public boolean waitForInvisibilityOfElement(String locator, int... waitTime){
        SelenideElement se = null;
        boolean status = false;
        int maxWaitTime = 0;
        if (waitTime.length > 0){
            maxWaitTime = waitTime[0];
        }else{
            maxWaitTime = WaitFor.HIGH_IMPLICIT_TIME_SEC;
        }
        try{
            se = seleniumActions.find(locator, maxWaitTime);
        }catch (Exception e) {
            System.out.println("Exception occured in waitForInvisibilityOfElement - " + e);
        }

        int count =1;
        if (se!=null){
            try {
                while (se.isDisplayed()) {
                   if (!se.isDisplayed()){
                       status = true;
                       break;
                   }

                   if (count == maxWaitTime){
                       System.out.println("Page Loading message is shown even after wait of "+maxWaitTime+" seconds");
                       break;
                   }
                }
            }catch (Exception e){
                System.out.println("Exception occured in waitForInvisibilityOfElement - " + e);
            }
        }
        return status;
    }

    public ArrayList<String> getTabNames(){
        String locatorType = jsonFileReader.getElementData("Application.CommonElements.Tabs","locatorType");
        String locatorValue = jsonFileReader.getElementData("Application.CommonElements.Tabs","locatorValue");

        List<SelenideElement> se = seleniumActions.finds(locatorType+"="+locatorValue, 10);
        ArrayList<String > tabNames = new ArrayList<>();
        int iCount = 0;
        for (SelenideElement s : se){
            tabNames.add(s.getText());
        }
        return tabNames;

    }


}
