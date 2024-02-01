package main.java.com.core.helper;

import com.codeborne.selenide.SelenideElement;
import com.testkit.actions.web.SeleniumActions;
import com.testkit.utils.reader.JsonFileReader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Helper {
    public Logger logger = Logger.getLogger(Helper.class.getName());
    public JsonFileReader jsonFileReader = null;
    public SeleniumActions seleniumActions;


    public Helper(String environment) {

    }

    /***
     * This method create copy using deep copy mechanism of original hashmap
     * @param originalMap
     * @return
     */
    public Map<String, String> copyHashMap(Map<String, String> originalMap) {
        Map<String, String> copy = new HashMap<>();
        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getFileList(String folderPath) {
        ArrayList<String> listOfFiles = new ArrayList<>();
        try {
            File folderToScan = new File(folderPath);
            File[] filesList = folderToScan.listFiles();
            for (int iCount = 0; iCount < filesList.length; iCount++) {
                if (filesList[iCount].isFile()) {
                    listOfFiles.add(filesList[iCount].getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfFiles;
    }

    public void deleteFile(String fileNameStartsWith, String fileExension, String directoryName) {
        ArrayList<String> fileList = getFileList(directoryName);
        for (int iCount = 0; iCount < fileList.size(); iCount++) {
            if (fileList.get(iCount).startsWith(fileNameStartsWith) && fileList.get(iCount).endsWith(fileExension)) {
                String filePath = directoryName + "\\" + fileList.get(iCount);
                File downloadFileName = new File(filePath);
                if (downloadFileName.exists()) {
                    downloadFileName.delete();
                    System.out.println("fileName : [" + filePath + "] deleted successfully");
                } else {
                    System.out.println("Delete fileName : [" + filePath + "] failed");
                }
            }
        }
    }

    public boolean closePopUp(String popUpName, String action, String btnLocator) {
        boolean result = false;
        String uiAction;

        try {
            switch (action) {
                case "Yes":
                case "yes":
                case "YES":
                    uiAction = "Yes";
                    break;
                case "no":
                case "No":
                case "NO":
                    uiAction = "No";
                    break;
                case "ok":
                case "Ok":
                case "OK":
                    uiAction = "Ok";
                    break;
                case "Apply":
                case "APPLY":
                    uiAction = "Apply";
                    break;
                case "Add":
                    uiAction = "Add";
                    break;
                default:
                    System.out.println("Invalid actions : [" + action + "] passed in function");
            }
            SelenideElement selenideElement = seleniumActions.find(btnLocator, 10);
            for (int iCount = 0; iCount < 3; iCount++) {
                selenideElement.click();
//                String popUpTile = getPopUpTitle();
//                if (!popUpTile.equalsIgnoreCase(popUpName)){
//                    result = true;
//                    break;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    public boolean compareList(ArrayList<String > actual, ArrayList<String> expected, String inMessage, String elementType){
//        boolean result = false;
//        boolean flag = true;
//        int actualLength = actual.size();
//        int expectedLength = expected.size();
//        int length = 0;
//        int iCounter = 0;
//        if (actualLength != expectedLength){
//            System.out.println("No of "+elementType+" not matched, Expected : ["+expectedLength +"] , Actual : ["+actualLength+"] "+inMessage);
//            if (actualLength <expectedLength){
//                length = actualLength;
//            }else{
//                length = expectedLength;
//            }
//            flag =false;
//        }else if (actualLength>0){
//            length = actualLength;
//            System.out.println("No of "+elementType+" not matched, Expected : ["+expectedLength +"] , Actual : ["+actualLength+"] "+inMessage);
//
//        }
//        for (int iCount = 0; iCount < length; iCount++) {
//            if (elementType.contains())
//        }
//
//    }

    /***
     * This method will return the list of Sections displayed on UI.
     * @param accordionName
     * @return
     */
    public ArrayList<String> getUiSectionsUnderAccordion(String accordionName) {
        String locatorType = jsonFileReader.getElementData("Application.CommonElements.Sections", "locatorType");
        String locatorValue = jsonFileReader.getElementData("Application.CommonElements.Sections", "locatorValue");
        locatorValue = locatorValue.replace("ACCORDION_NAME", accordionName);
        String element = locatorType + "=" + locatorValue;
        List<SelenideElement> selenideElementList = seleniumActions.finds(element, 10);
        ArrayList<String> out = new ArrayList<>();
        int iCounter = 0;
        for (SelenideElement se : selenideElementList) {
            out.add(se.getText());
            iCounter++;
        }
        return out;
    }

    /***
     * This method will return the list of Sections displayed on UI.
     * @param accordionName
     * @return
     */
    public ArrayList<String> getSectionsNameList(String accordionName) {
        String locatorType = jsonFileReader.getElementData("Application.CommonElements.Sections", "locatorType");
        String locatorValue = jsonFileReader.getElementData("Application.CommonElements.Sections", "locatorValue");
        locatorValue = locatorValue.replace("ACCORDION_NAME", accordionName);
        String element = locatorType + "=" + locatorValue;
        List<SelenideElement> selenideElementList = seleniumActions.finds(element, 10);
        ArrayList<String> sectionNameList = new ArrayList<>();
        if (!selenideElementList.isEmpty()) {
            for (SelenideElement se : selenideElementList) {
                String sectionName = se.getText().trim();
                String hierarchy = "Application.Settings." + accordionName.replaceAll("\\s", "") + "." + sectionName.replaceAll("\\s", "");
                if (!isUxVerificationAllowed(hierarchy)) {
                    System.out.println("UX Verification is set as No for section [" + sectionName + "] in Accordion [" + accordionName + "]");

                } else {
                    if (!sectionName.equalsIgnoreCase("")) {
                        sectionNameList.add(sectionName);
                    }
                }
            }
        }
        return sectionNameList;
    }

    /***
     * This function will return the list of Sub-Sections displayed on UI under Section of Accordion
     * @param accordionName
     * @param sectionName
     * @return
     */

    public ArrayList<String> getSubSectionNameList(String accordionName, String sectionName) {
        String locatorType = jsonFileReader.getElementData("Application.CommonElements.Sections", "locatorType");
        String locatorValue = jsonFileReader.getElementData("Application.CommonElements.Sections", "locatorValue");
        locatorValue = locatorValue.replace("SECTION_NAME", sectionName).replace("ACCORDION_NAME", accordionName);
        String element = locatorType + "=" + locatorValue;
        List<SelenideElement> selenideElementList = seleniumActions.finds(element, 10);
        ArrayList<String> subSectionNameList = new ArrayList<>();
        if (!subSectionNameList.isEmpty()) {
            for (SelenideElement se : selenideElementList) {
                StringBuilder subSectionName = new StringBuilder(se.getText().trim());

                if (!StringUtils.isEmpty(subSectionName) || subSectionName.toString().replaceAll("\\s", "").equalsIgnoreCase("")) {
                    continue;
                }
                String hierarchy = "Application.Settings." + accordionName.replaceAll("\\s", "") + "." + sectionName.replaceAll("\\s", "") + "." + subSectionName.toString().replaceAll("\\s", "");
                if (!isUxVerificationAllowed(hierarchy)) {
                    System.out.println("UX Verification is set as No for sub section [" + subSectionName + "] under section [" + sectionName + "]");
                } else {
                    subSectionNameList.add(subSectionName.toString());
                }
            }
        }
        return subSectionNameList;
    }


    public Map<String, Integer> incrementSettingTypeValue(Map<String, Integer> map, String key) {
        Integer count = map.get(key);
        if (count == null) {
            map.put(key, 1);

        } else {
            map.put(key, count + 1);
        }
        return map;
    }

    public StringBuilder getValueFromArray(String[] arrayName, int counter, String previousValue) {
        StringBuilder retValue = new StringBuilder("");
        int arrayLength = arrayName.length;
        if (arrayLength >= 2 && counter == 1) {
            if (previousValue.equalsIgnoreCase(arrayName[0])) {
                retValue = new StringBuilder(arrayName[1]);
            } else if (previousValue.equalsIgnoreCase(arrayName[1])) {
                retValue = new StringBuilder(arrayName[0]);
            } else {
                retValue = new StringBuilder(arrayName[0]);
            }
        } else if (arrayLength > 2 && counter != 1) {
            retValue = new StringBuilder(arrayName[counter]);
        } else if (arrayLength == 2 && counter != 1) {
            if (previousValue.equalsIgnoreCase(arrayName[0])) {
                retValue = new StringBuilder(arrayName[1]);
            } else {
                retValue = new StringBuilder(arrayName[0]);
            }
        }
        if (retValue.toString().isEmpty()) {
            System.out.println("Returning Empty Value from list :" + Arrays.toString(arrayName));

        }
        return retValue;
    }

    /***
     * This method compares two arrayList of String with its size and contents.
     * @param actual
     * @param expected
     * @param inMessage
     * @param elementType
     * @return
     */
    public boolean compareList(ArrayList<String> actual, ArrayList<String> expected, String inMessage, String elementType) {
        boolean flag = true;
        int length = 0;
        int loop = 0;
        return false;
    }

    public String convertDateToStringFormat(String dateStrValue, String fromFormat, String toFormat) {
        String retVal = null;
        SimpleDateFormat fromUser = new SimpleDateFormat(fromFormat);
        SimpleDateFormat myFormat = new SimpleDateFormat(toFormat);
        try {
            retVal = myFormat.format((fromUser.parse(dateStrValue)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /***
     * This method is use dto getTimeStamp from current time
     * @return
     */

    public String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat(("yyyy_MM_dd_HH_mm_ss_a"));
        Date date = new Date();
        String timeStamp = dateFormat.format(date).toString();
        return timeStamp;
    }

//    public boolean dragAndDropAction(SelenideElement sourceElement, SelenideElement targetElement){
//        boolean result = false;
//        try{
//            if (!sourceElement.isDisplayed() && !targetElement.isDisplayed()){
//                result = false;
//            }
//
//            Actions builder = new Actions(seleniumActions.driver);
//            Action dragAndDrop = builder.clickAndHold(sourceElement).moveByOffset(-1,-1).moveToElement(targetElement).release(targetElement).build();
//            dragAndDrop.perform();
//            result = true;
//        }catch (Exception e){
//            System.out.println(e);
//        }
//        return result;
//    }
//
//    public void dragAndDropUsingJavaScript(SelenideElement sourceElement, SelenideElement targetElement) throws InterruptedException {
//        String startDrag = getWebCssSelector(sourceElement);
//        String endDrag = getWebCssSelector(targetElement);
//        String fileContent = "";
//        ((JavascriptExecutor) seleniumActions.driver).executeScript("eval(arguments[0]);", fileContent );
//        boolean dragMockExists = (boolean) ((JavascriptExecutor) seleniumActions.driver).executeScript("return !!window.dragMock");
//        if (dragMockExists == false){
//            throw new InterruptedException("Unable to add the drag mock to the driver");
//        }
//        ((JavascriptExecutor) seleniumActions.driver).executeScript("var startEle= document.querySelector('"+startDrag+"');var endEle= document.querySelector('"+endDrag+"');var wait = 150;");
//    }

    public String getRandomElementFromList(List<String> lst) {
        String randomElement = null;
        Random rand = new Random();
        List<String> tempLst = lst.stream().filter(value -> value != null && value.length() > 0).collect(Collectors.toList());
        tempLst = tempLst.stream().distinct().collect(Collectors.toList());
        if (!tempLst.isEmpty()) {
            randomElement = tempLst.get(rand.nextInt(tempLst.size()));
        } else {
            System.out.println("This is no element to get random value");
        }
        return randomElement;
    }

    public String generateRandomString(int randomStringLength) {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();
        while (randomStringLength-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            randomString.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return randomString.toString();
    }

    public int generateRandomNumber(int startsNum, int endNum) {
        return (int) (Math.random() * (endNum - startsNum + 1) + startsNum);
    }

    /***
     * This method checks if expected list elements exists in actual lsit
     * @param actual
     * @param expected
     * @param inMessage
     * @param elementType
     * @return
     */
    public boolean containList(ArrayList<String> actual, ArrayList<String> expected, String inMessage, String elementType) {
        boolean flag = true;
        for (int iCount = 0; iCount < actual.size(); iCount++) {
            if (actual.contains(expected.get(iCount))) {
                System.out.println(elementType + "MATCHED - Expected : [" + expected.get(iCount) + "] exists in Actual " + inMessage);
            } else {
                System.out.println(elementType + "NOT MATCHED - Expected : [" + expected.get(iCount) + "] does not exists in Actual " + inMessage);
                flag = false;
                break;
            }
        }
        return flag;
    }


    /***
     * This method check if file exists & returns file size in MB
     * @param filePath
     * @return
     */
    public double getFileSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return 0;
        }
        return (double) file.length() / (1024 * 1024);
    }

    public static <K, V> K getKey(Map<K, V> map, String value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            return entry.getKey();
        }
        return null;
    }

    public HashMap<String, String> generateMapForKeyValues(HashMap<String, String> map) {
        HashMap<String, String> keyMap = new HashMap<>();
        if (map.get("placeHolderKeyList") != null && map.get("placeHolderValueList") != null) {
            List<String> keys = Arrays.asList(map.get("placeHolderKeyList").split(","));
            List<String> values = Arrays.asList(map.get("placeHolderValueList").split(","));

            if (keys.size() != values.size()) {
                throw new RuntimeException("Number of keys and values are not matching");
            }

            for (int iCount = 0; iCount < keys.size(); iCount++) {
                keyMap.put(keys.get(iCount), values.get(iCount));
            }
        }
        return keyMap;
    }


    /***
     * This method replaces key with value in given string value
     * @param strValue
     * @param map
     * @return
     */
    public String replaceKeyWithValueInString(String strValue, HashMap<String, String> map) {
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                strValue = strValue.replace(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return strValue;
    }

    public void writeDataIntoTextFile(String data, String fileName) {
        BufferedWriter writer = null;
        try {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            if (!StringUtils.isEmpty(data)) {
                writer = new BufferedWriter(new FileWriter(file, false));
                writer.write(data);
                writer.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String convertDateTimeToRequiredTimeZone(String originalDateTime, String originalDateTimeFormat, String targetTimeZoneCode, String targetDateTimeFormat) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(originalDateTimeFormat);
        Date fromDate = (Date) formatter.parse(originalDateTime);
        TimeZone toTimeZone = TimeZone.getTimeZone(targetTimeZoneCode);
        formatter.setTimeZone(toTimeZone);
        String convertedTime = formatter.format(fromDate);
        return convertedTime;
    }
}
