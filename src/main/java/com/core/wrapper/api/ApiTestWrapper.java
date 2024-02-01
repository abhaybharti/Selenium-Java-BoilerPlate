package main.java.com.core.wrapper.api;

import com.testkit.helper.api.ApiTestHelper;
import com.testkit.reportmanager.ExtentReportManager;
import com.testkit.utils.reader.JsonFileReader;
import com.testkit.utils.store.Cache;
import com.testkit.wrapper.Wrapper;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class ApiTestWrapper extends Wrapper {
    public Logger logger = Logger.getLogger(ApiTestWrapper.class.getName());
    JsonFileReader jsonFileReader = null;
    HashMap<String, String> keyMap = new HashMap<>();
    HashMap<String, String> apiHeaderInfo = new HashMap<>();
    public ApiTestHelper apiTestHelper;
    StringBuilder dataApiBaseUri = new StringBuilder();

    public ApiTestWrapper(HashMap<String, String> testData, Logger logger, ExtentReportManager reportManager, String jsonFileName) {
        super(testData, logger, reportManager, jsonFileName);
        jsonFileReader = new JsonFileReader(jsonFileName, testData.get("env"));
        apiTestHelper = new ApiTestHelper(jsonFileReader);
    }

    public void hitApiEndPoint() {
        boolean result = true;
        Response response = null;
        String endPointName = Cache.getCacheString("endPointName");
        String elementTypeValue = jsonFileReader.getElementData(endPointName, "elementType");
        String operationTypeValue = jsonFileReader.getElementData(endPointName, "operationType").toUpperCase();
        String apiUriValue = jsonFileReader.getElementData(endPointName, "apiUri");
        String jsonBodyValue = jsonFileReader.getElementData(endPointName, "jsonPayload");
        String apiStatusName = Cache.getCacheString("apiStatus");
        String expectedStatusFromJson = "";
        HashMap<String, String> keyMap = new HashMap<>();

        if (!StringUtils.isEmpty("apiStatusName")) {
            expectedStatusFromJson = jsonFileReader.getElementData(apiStatusName, "expectedStatus");
        }

        if (Cache.getCacheString("jsonKey") != null) {
            jsonBodyValue = jsonFileReader.getElementData(endPointName, Cache.getCacheString("jsonKey"));
        }

        if (StringUtils.isEmpty(expectedStatusFromJson)) {
            expectedStatusFromJson = "200";
        }

        if (Cache.getCacheString("jsonKeyList") != null) {
            String[] jsonKeyList = Cache.getCacheString("jsonKeyList").split(",");
            for (String jsonKey : jsonKeyList) {
                String[] temp = jsonFileReader.getElementData(endPointName, jsonKey).split("\\|");
                if (Cache.getCacheString("placeHolderKeyList") != null) {
                    keyMap.put("placeHolderKeyList", temp[0]);
                    keyMap.put("placeHolderValueList", temp[1]);
                } else {
                    keyMap.put("placeHolderKeyList", keyMap.get("placeHolderKeyList") + "," + temp[0]);
                    keyMap.put("placeHolderValueList", keyMap.get("placeHolderValueList") + "," + temp[1]);
                }
            }
        }

        keyMap = apiTestHelper.generateKeyMapForApi(keyMap);

        if (Cache.getCacheString("payloadKeyList") != null && Cache.getCacheString("payloadValueList") != null) {
            List<String> keyList = Arrays.asList(Cache.getCacheString("payloadKeyList").split("\\|"));
            List<String> valueList = Arrays.asList(Cache.getCacheString("payloadValueList").split("\\|"));
            if (keyList.size() != valueList.size()) {
                Assert.fail("payloadKeyList and payloadValueList size does not match");
            }

            for (int iLoop = 0; iLoop < keyList.size(); iLoop++) {
                jsonBodyValue = jsonBodyValue.replace(keyList.get(iLoop), valueList.get(iLoop));
            }
        }

        //form API
        StringBuilder uri = new StringBuilder(apiTestHelper.replaceKeyWithValueInString(apiUriValue, keyMap));
        Cache.setCacheString(endPointName + "_uri", uri.toString());

        int expectedStatusCode = 200; //default response code

        //Override expectedStatusCode if passed from script
        if (Cache.getCacheInteger("expectedStatusCode") != null) {
            expectedStatusCode = Cache.getCacheInteger("expectedStatusCode");
        }

        if (Cache.getCacheInteger("apiStatus") != null) {
            expectedStatusCode = Integer.parseInt(expectedStatusFromJson);
        }

        dataApiBaseUri = DataApiConstants.API_ENV_MAP.get(testData.get("env").toLowerCase());

        apiHeaderInfo = apiTestHelper.setApiHeaderInfo(keyMap);
        report.log("INFO", "Endpoint : [" + endPointName + "], performing operation :[" + operationTypeValue + "], API URI : [" + dataApiBaseUri + uri.toString() + "]");

        switch (operationTypeValue.toLowerCase()) {
            case "get":
                response = apiTestHelper.invokeGetApi(dataApiBaseUri, uri, apiHeaderInfo, expectedStatusCode);
                break;
            case "post":
                if (jsonBodyValue == null) {
                    report.log("ERROR", "API Json Body is null");
                    Assert.fail("API Json Body is null");
                }
                response = apiTestHelper.invokePostApi(dataApiBaseUri, uri, jsonBodyValue, apiHeaderInfo, expectedStatusCode, jsonBodyValue);
                break;
            case "postcsv":
                if (jsonBodyValue == null) {
                    report.log("ERROR", "CSV File name is not passed");
                    Assert.fail("CSV File name is not passed");
                }

                StringBuilder contentType = new StringBuilder(jsonFileReader.getElementData(endPointName, "contentType"));
                String csvFilePath = (System.getProperty("user.dir")+"\\"+jsonBodyValue).replace("ENV",testData.get("env"));
                apiHeaderInfo.put("Content-Disposition","form-data");
                apiHeaderInfo.put("Content-Type",contentType.toString());
                response = apiTestHelper.invokePostApiWithCSVFile(dataApiBaseUri, uri, apiHeaderInfo, expectedStatusCode, csvFilePath);
                break;
            case "put":
                if (jsonBodyValue==null){
                    report.log("ERROR", "API Json Body is null");
                    Assert.fail("API Json Body is null");
                }

                jsonBodyValue = apiTestHelper.replaceKeyWithValueInString(jsonBodyValue, keyMap);
                response = apiTestHelper.invokePutApi(dataApiBaseUri, uri, apiHeaderInfo, expectedStatusCode,jsonBodyValue);
                break;
            case "delete":
                if (Objects.nonNull(jsonBodyValue)){
                    jsonBodyValue = apiTestHelper.replaceKeyWithValueInString(jsonBodyValue, keyMap);
                }
                response = apiTestHelper.invokeDeleteApi(dataApiBaseUri, uri, apiHeaderInfo, expectedStatusCode,jsonBodyValue);
            case "default":
                Assert.fail("Invalid operationTypeValue : [" + operationTypeValue + "]");
        }
        if (response.getStatusCode()!=expectedStatusCode){
            report.log("FAIL", "Expected Status Code : [" + expectedStatusCode + "] , Actual Status Code : [" + response.getStatusCode() + "]");
            Assert.fail("Expected Status Code : [" + expectedStatusCode + "] , Actual Status Code : [" + response.getStatusCode() + "]");
        }
        Cache.setCacheApiResponse(endPointName + "_response", response);
        Cache.setCacheInteger(endPointName+"_response_status", response.getStatusCode());
    }

    public void verifyApiResponseStatusCode(){
        SoftAssert softAssert = new SoftAssert();
        String apiStatusName = Cache.getCacheString("apiStatus");
        String endPointName = Cache.getCacheString("endPointName");
        int expectedStatusCode = Cache.getCacheInteger("expectedStatusCode");

        Integer actualStatus = Cache.getCacheInteger(endPointName+"_response_status");
        if (expectedStatusCode == actualStatus){
            report.log("PASS", "Verify API Status for URI : ["+Cache.getCacheString(endPointName)+"] , Expected : [" + expectedStatusCode + "] , Actual : [" + actualStatus + "]");
        }else{
            String errorMsg = "Verify API Status for URI : ["+Cache.getCacheString(endPointName)+"] , Expected : [" + expectedStatusCode + "] , Actual : [" + actualStatus + "]";
            report.log("FAIL", errorMsg);
            softAssert.fail(errorMsg);
        }
        softAssert.assertAll();
    }


    public void verifyJsonSchemaOfApi(){
        SoftAssert softAssert = new SoftAssert();
        String endPointName = Cache.getCacheString("endPointName");
        set<ValidationMessage> result = apiTestHelper.validateResponseJsonSchema(endPointName);

        if (result.isEmpty()){
            report.log("PASS", "Verify JSON Schema for endpoint : ["+endPointName+"] , response JSON Schema validated successfully, No Error found");
        }else{
            String errorMsg = "["+endPointName+"] , response JSON Schema validated Failed !!!";
            report.log("FAIL", errorMsg);
            softAssert.fail(errorMsg);
        }
        softAssert.assertAll();
    }

    public void verifyApiStatusAndJsonSchemaDetails(){
        String endPointName = Cache.getCacheString("endPointName");
        if (!endPointName.contains(".")){
            Cache.setCacheString("endPointName", apiTestHelper.getEndPointJsonHierarchy(endPointName));
        }

        hitApiEndPoint();
        verifyApiResponseStatusCode();
        verifyJsonSchemaOfApi();
    }

    public void hitApiWithInvalidDataAndValidate(){
        SoftAssert softAssert = new SoftAssert();
        String endPointName = apiTestHelper.getEndPointJsonHierarchy(Cache.getCacheString("endPointName"));
        String elementTypeValue = jsonFileReader.getElementData(endPointName, "elementType");
        String operationTypeValue = jsonFileReader.getElementData(endPointName, "operationType").toUpperCase();
        String apiUriValue = jsonFileReader.getElementData(endPointName, "apiUri");

        String jsonKey = Cache.getCacheString("jsonKey");

        if (Cache.getCacheString("placeHolderKeyListJson") != null && !Cache.getCacheString("placeHolderValueListJson").isEmpty()) {
            if (Cache.getCacheString("placeHolderKeyList") == null) {
                Cache.setCacheString("placeHolderKeyList", jsonFileReader.getElementData(endPointName, "placeHolderKeyListJson"));
                Cache.setCacheString("placeHolderValueList", jsonFileReader.getElementData(endPointName, "placeHolderValueListJson"));
            }else{
                Cache.setCacheString("placeHolderKeyList", Cache.getCacheString("placeHolderKeyList") + "," + jsonFileReader.getElementData(endPointName, "placeHolderKeyListJson"));
                Cache.setCacheString("placeHolderValueList", Cache.getCacheString("placeHolderValueList") + "," + jsonFileReader.getElementData(endPointName, "placeHolderValueListJson"));
            }
        }

        String fieldValueList = jsonFileReader.getElementData(endPointName, jsonKey);
        String [] fieldDataList = fieldValueList.split(",");
        int index = 1;
        Map<String,String> temp = new HashMap<>();
        temp = apiTestHelper.copyHashMap(keyMap);

        for(String fieldData :fieldDataList ){
         // TBD
        }
    }

    public void verifyValueInApiResponse(){
        SoftAssert softAssert = new SoftAssert();
        Boolean expectedFailure = false,passFlag = false;

        String keyName = Cache.getCacheString("KeyName");

        String expectedValue = "", endpointName = "";

        if (Cache.getCacheString("expectedFailure")!=null && Cache.getCacheString("expectedFailure").equalsIgnoreCase("true")){
            expectedFailure = true;
        }
        if (Cache.getCacheString("expectedValue")!=null ){
            expectedValue = Cache.getCacheString("expectedValue");
        }

        endpointName = Cache.getCacheString("endPointName")!=null? Cache.getCacheString("endPointName"): throwErrorWhenKeyNoExistInMap("Cache.getCacheString(\"endPointName\") is not passed from script") ;

        String expectedErrorMsgFromJson = "";
        if (Cache.getCacheString("expectedValue")==null){
            expectedErrorMsgFromJson = jsonFileReader.getElementData(endpointName, "invalidPageSizeError");
            expectedValue = expectedErrorMsgFromJson;
        }

        boolean isValueExactMatch = true, isKeyHasValue = false;

        if (Cache.getCacheString("isValueExactMatch")!=null && Cache.getCacheString("isValueExactMatch").equalsIgnoreCase("false")){
            isValueExactMatch = false;
        }

        if (Cache.getCacheString("isKeyHasValue")!=null && Cache.getCacheString("isKeyHasValue").equalsIgnoreCase("true")){
            isKeyHasValue = true;
        }

        String actualValue = null;

        try{

            actualValue = apiTestHelper.getKeyValueFromResponse(endpointName, keyName);
        }catch (IOException e){
            logger.info(e.getMessage());
        }

        if (actualValue== null || actualValue.contains("[null]")){
            actualValue = "NULL";
        }else{
            actualValue = actualValue.replace("\"","").replace("[","").replace("]","");
        }

        if (isKeyHasValue){
            if (StringUtils.isEmpty(actualValue)){
                report.log("FAIL",keyName + " value is blanked");
                softAssert.fail(keyName + " value is blanked");
            }else{
                report.log("PASS",keyName + " value is not blank, value is [" + actualValue + "]");
            }
            Cache.setCacheString(keyName+"_value",actualValue);
            softAssert.assertAll();
            return;
        }

        Cache.setCacheString(keyName+"_value",actualValue);
        String [] expectedOutputValue = expectedValue.split("\\|");

        for (int iLoop = 0; iLoop <expectedOutputValue.length ; iLoop++) {

            if (isValueExactMatch){
                if (actualValue.equalsIgnoreCase(expectedOutputValue[iLoop])){
                    passFlag = true;
                    break;
                }
            }else{
                if (actualValue.contains(expectedOutputValue[iLoop])){
                    passFlag = true;
                    break;
                }
            }
        }

        if (expectedFailure && !passFlag){
            passFlag = true;
        }

        if (passFlag){
            report.log("PASS", keyName + " value matched in JSON Response, Actual : [" + actualValue + "] value, Expected : [" + expectedValue + "]");
        }else{
            report.log("FAIL", keyName + " value is not matched in JSON Response, Actual : [" + actualValue + "] value, Expected : [" + expectedValue + "]");
            softAssert.fail(keyName + " value is not matched in JSON Response, Actual : [" + actualValue + "] value, Expected : [" + expectedValue + "]");
        }

        softAssert.assertAll();
    }

    /***
     * This wrapper gets list of key and values from response where key is provider by user and verifies the expected key with expected value by user from test script
     */
    public void verifyValueInApiResponseForGivenKeyValue(){
        SoftAssert softAssert = new SoftAssert();
        Boolean passFlag = false;
        String keyName = Cache.getCacheString("KeyName");
        String keyValue = Cache.getCacheString("KeyValue");
        String expectedKey = "", expectedValue = "", endpointName = "";

        if (Cache.getCacheString("expectedKey")!=null){
            expectedKey = Cache.getCacheString("expectedKey");
        }

        if (Cache.getCacheString("expectedValue")!=null){
            expectedValue = Cache.getCacheString("expectedValue");
        }


        endpointName = Cache.getCacheString("endPointName") != null ? Cache.getCacheString("endPointName") : throwErrorWhenKeyNoExistInMap("Cache.getCacheString(\"endPointName\") is not passed from script");

        String expectedErrorMsgFromJson = "";

        if (Cache.getCacheString("expectedValue")==null) {
            expectedErrorMsgFromJson = jsonFileReader.getElementData(endpointName, "invalidPageSizeError");
            expectedValue = expectedErrorMsgFromJson;
        }

        boolean isValueExactmatch = true;
        boolean isKeyHasValue = false;

        if (Cache.getCacheString("isValueExactMatch")!=null && Cache.getCacheString("isValueExactMatch").equalsIgnoreCase("false")){
            isValueExactmatch = false;
        }

        if (Cache.getCacheString("isKeyHasValue")!=null && Cache.getCacheString("isKeyHasValue").equalsIgnoreCase("true")){
            isKeyHasValue = true;
        }

        String responseJsonString = Cache.getCacheString("endPointName")+"_response";

        List <Map<String,Object>> resultFromResponse = apiTestHelper.getAllKeyValueResponseFromJsonArrayForMatchedKeyAndValue(responseJsonString,keyName,keyValue);
        String actualVal="";
        for (int iCount = 0; iCount <resultFromResponse.size() ; iCount++) {
            Map<String,Object> map = resultFromResponse.get(iCount);
            for (Map.Entry<String, Object> entry : ((HashMap<String,Object>)map).entrySet()) {
                String k = entry.getKey();
                if (k.equalsIgnoreCase(expectedKey)){
                    actualVal = entry.getValue().toString();
                    System.out.println("Key : " + k + " Value : " + actualVal);
                    if (actualVal.equalsIgnoreCase(expectedValue)){
                        passFlag = true;
                        Cache.setCacheString(expectedKey+"_value",expectedValue);
                        break;
                    }
                }else {
                    continue;
                }
            }

        }
        if (passFlag){
            rm.log("PASS", expectedKey + " & value matched in JSON Response, Actual : [" + actualVal + "] value, Expected : [" + expectedValue + "]");
        }else{
            rm.log("FAIL",expectedKey+" & value not matached in JSON response, Actual : ["+ actualVal + "] value, Expected : [" + expectedValue + "]");
            softAssert.fail(expectedKey+" & value not matached in JSON response, Actual : ["+ actualVal + "] value, Expected : [" + expectedValue + "]");
        }
        softAssert.assertAll();
    }

}
