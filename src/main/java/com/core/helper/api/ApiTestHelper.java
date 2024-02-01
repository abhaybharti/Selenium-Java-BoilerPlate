package main.java.com.core.helper.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.testkit.actions.api.ApiActions;
import com.testkit.helper.Helper;
import com.testkit.utils.store.Cache;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.String.valueOf;


public class ApiTestHelper extends Helper {
    public ApiTestHelper(String environment) {
        super(environment);
    }
    ApiActions apiActions =null;

//    public ApiTestHelper(JsonFileReader jsonFileReader) {
//
//    }

    /***
     * This helper method generates header info which will be used in hitApiEndPoint method.
     * @param keyMap
     * @return
     */
    public HashMap<String,String> setHeaderInfo(HashMap<String,String> keyMap){
        HashMap<String,String > temp = new HashMap<>();
        temp.put("Content-Type","application/json");
        return temp;
    }

    public HashMap<String,String> generateMapForApi(HashMap<String,String> compare_data){
        HashMap<String,String> keyMap = new HashMap<>();
        String accountId = "";
        keyMap = generateMapForKeyValues(compare_data);
        keyMap.put("accountId","value");
        return keyMap;

    }

    /**
     * This helper method reads JSON Schema which is already saved, read API response from Cache and perform JSON Schema validation. If Validation fails, returns set having list of error messages.
     * @param endPointName
     * @return
     */
    public Set<ValidationMessage> validateResponseJsonSchema(String endPointName){
        Boolean resultStatus = false;
        JSONPObject jsonpObject = null, jsonSchema = null;


        //check if jsonSchemaFileName key & value exists in JSON file
        String jsonSchemaFileName = jsonFileReader.getElementData(endPointName, "jsonSchemaFileName");
        if (StringUtils.isEmpty(jsonSchemaFileName)){
            Assert.fail(jsonSchemaFileName +" key/value is not stored in JSON file");
        }

        String env = testData.get("env").toUpperCase();

        //get json schema file name
        File jsonSchemaFile = new File(System.getProperty("uer.dir")+"\\src\\TestData\\ApiConfigDetails\\ApiJsonSchemaFile\\"+env+"\\"+jsonSchemaFileName);

        //check if file exists
        if (!jsonSchemaFile.exists()){
            Assert.fail(jsonSchemaFile +" file is not found");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

        StringBuilder expectedSchemaStreamStr = new StringBuilder();

        try{
            expectedSchemaStreamStr = new StringBuilder(FileUtils.readFileToString(new File(valueOf(jsonSchemaFileName), valueOf(StandardCharsets.UTF_8))));
        }catch (IOException e){
            e.printStackTrace();
        }

        InputStream expectedSchemaStream = new ByteArrayInputStream(expectedSchemaStreamStr.toString().getBytes());

        StringBuilder actualResponse = new StringBuilder(String.valueOf(Cache.getCacheApiResponse(endPointName+"_response")));

        InputStream actualResponseStream = new ByteArrayInputStream(actualResponse.toString().getBytes());

        JsonNode json = null;
        JsonSchema schema = null;

        try{
            json = objectMapper.readTree(actualResponseStream);
            schema = schemaFactory.getSchema(expectedSchemaStream);
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        Set<ValidationMessage> validationResult = schema.validate(json);
        return validationResult;
    }

    /***
     * This helper method intercepts error message thrown by json-schema-validator library and sets user defined error messages.
     * @param errorMsg
     * @return
     */
    public String customizeErrorMessageForJsonSchemaValidation(ValidationMessage errorMsg){

        String returnMsg = "";

        //Error message thrown by library
        String KEY_IS_MISSING_IN_RESPONSE_REQUIRED_IN_SCHEMA = "is missing but it is required";
        String KEY_DATA_TYPE_IS_DIFFERENT_IN_RESPONSE_AND_SCHEMA = "integer found, string expected";
        String KEY_IS_NOT_DEFINED_IN_SCHEMA_BUT_EXISTS_IN_RESPONSE = "is not defined in schema";

        //Custom error message
        String KEY_IS_MISSING_IN_RESPONSE_REQUIRED_IN_SCHEMA_MSG = " is defined in Json Schema, but missing in Json response";
        String KEY_DATA_TYPE_IS_DIFFERENT_IN_RESPONSE_AND_SCHEMA_MSG = " Data Type -'String' expected in Schema but 'Integer' data type found in Json response";
        String KEY_IS_NOT_DEFINED_IN_SCHEMA_BUT_EXISTS_IN_RESPONSE_MSG = " not defined in Json Schema, but found in Json response";

        String keyName = errorMsg.toString().substring(0,errorMsg.toString().indexOf(":"));

        if (StringUtils.contains(String.valueOf(errorMsg),KEY_IS_MISSING_IN_RESPONSE_REQUIRED_IN_SCHEMA)){
            returnMsg = keyName+" - "+KEY_IS_MISSING_IN_RESPONSE_REQUIRED_IN_SCHEMA_MSG;
        }else if (StringUtils.contains(String.valueOf(errorMsg),KEY_DATA_TYPE_IS_DIFFERENT_IN_RESPONSE_AND_SCHEMA)){
            returnMsg= keyName+" - "+KEY_DATA_TYPE_IS_DIFFERENT_IN_RESPONSE_AND_SCHEMA_MSG;
        }else if (StringUtils.contains(String.valueOf(errorMsg),KEY_IS_NOT_DEFINED_IN_SCHEMA_BUT_EXISTS_IN_RESPONSE)){
            returnMsg= keyName+" - "+KEY_IS_NOT_DEFINED_IN_SCHEMA_BUT_EXISTS_IN_RESPONSE_MSG;
        }else{
            returnMsg= errorMsg.getMessage();
        }
        return returnMsg;
    }

    /***
     * This helper method returns JSON hierarchy of EndPoint passed from testng.xml file, checks in & returns endpoint JSON Hiearchy. In Default implementation, script will not have any EndPoint name (is taken from testng.xml file), user needs to pass endpoint only when API endpoint is different from testng.xml
     * @param endPointName
     * @return
     */
    public String getEndPointJsonHierarchy(String endPointName){
        String elementType = "EndPoint";
        String endPointHierarchy = "";
        ArrayList<String> endPointList = new ArrayList<>();
        for (String endPoint : endPointList){
            if (!endPointName.contains(endPoint)){
                continue;
            }
            endPointHierarchy = "Application.+EndPoint."+endPoint.replaceAll("\\s","");
        }
        return endPointHierarchy;
    }

    public Response invokePostApi(String dataApiBaseUri, String uri, HashMap<String,String> apiHeaderInfo, int expectedStatusCode, String jsonBodyValue) throws IOException {
        Response response = null;

        for (int iCount = 0; iCount < 3; iCount++) {
            response = apiActions.postApiWithHeaderAndBody(dataApiBaseUri, uri,jsonBodyValue, apiHeaderInfo);
            if (response!=null){
                if (response.statusCode()==expectedStatusCode){
                    report.log("INFO", "Response Expected Status Code : [" + expectedStatusCode + "] , Actual Status Code : ["+response.getStatusCode()+"]");
                    break;
                }
                if (iCount==2){
                    report.log("FAIL", "Getting response Code  other than : [" + expectedStatusCode + "] , even after [" + iCount + "] attempts");
                    break;
                }
                report.log("INFO", "Waiting for 2 minutes before making next API request");
                waitForSeconds(120);
            }else{
                report.log("INFO","API response is NULL");
                break;
            }
        }
        return response;
    }

    public Response invokePutApi(String dataApiBaseUri, String uri, HashMap<String,String> apiHeaderInfo, int expectedStatusCode, String jsonBodyValue) throws IOException {
        Response response = null;
        for (int iCount=0;iCount<3;iCount++){
            response = apiActions.putApiWithHeaderAndBody(dataApiBaseUri, uri, jsonBodyValue, apiHeaderInfo);
            if (response!=null){
                if (response.statusCode()==expectedStatusCode){
                    report.log("INFO", "Response Expected Status Code : [" + expectedStatusCode + "] , Actual Status Code : ["+response.getStatusCode()+"]");
                    break;
                }
                if (iCount==2){
                    report.log("FAIL", "Getting response Code  other than : [" + expectedStatusCode + "] , even after [" + iCount + "] attempts");
                    break;
                }
                report.log("INFO", "Waiting for 2 minutes before making next API request");
                waitForSeconds(120);
            }else{
                report.log("INFO","API response is NULL");
                break;
            }
        }
        return response;
    }

    /***
     * This helper method return value from response for given json key
     * @param endPointName
     * @param fieldHierarchy
     * @return
     */
    public String getKeyValueFromResponse(String endPointName,String fieldHierarchy){
        String retVal = null;
        Response actualResponse = Cache.getCacheApiResponse(endPointName+"_response");
        JsonPath jsonPath = actualResponse.jsonPath();
        try{
            retVal = jsonPath.getString(fieldHierarchy);
        }catch (Exception e){
            System.out.println(e);
        }
        return retVal;
    }

    /***
     * Thsi method reads Rest API response from Cache in json string format[Json Array format],input key name and its value and returns the map which matches the inputkeys valus
     * @param jsonData
     * @param inputKeyName
     * @param inputKeyvalue
     * @return
     */
    public List<Map<String,Object>> getAllKeyValueResponseFromJsonArrayForMatchedKeyAndValue(String jsonData, String inputKeyName,String inputKeyvalue){
        JSONArray convertedJsonArrayFromJsonData = new JSONArray(jsonData);
        int sizeOfJsonArray = convertedJsonArrayFromJsonData.length();

        List<Map<String,Object>> list = new ArrayList<>();
        for (int counter = 0; counter < sizeOfJsonArray; counter++) {
            JSONObject jsonObjectFromJsonArrayElement = convertedJsonArrayFromJsonData.getJSONObject(counter);
            Map<String,Object> m = convertJSONObjectToMap(jsonObjectFromJsonArrayElement);

            for(Map.Entry<String,Object> entry:m.entrySet()){
                String k = entry.getKey();
                String v = null;
                if (!k.equals(inputKeyName)){
                    continue;
                }else if (!entry.getValue().equals(null) && k.equals(inputKeyName)){    //&& entry.getValue().toString().equals(inputKeyvalue)){
                 v = entry.getValue().toString();
                 if (k.equals(inputKeyName) && v.equals(inputKeyvalue)){
                     list.add(m);
                 }
                 continue;
                }

            }
        }
        System.out.println("returns lsit of object where keyName :[ "+inputKeyName+"] Match with value :["+inputKeyvalue+"]");
        System.out.println("Result is : "+list);
        return list;
    }

    /***
     * This helper method converts JSON Object to Map
     * @param jsonObject
     * @return
     */
    public Map<String,Object> convertJSONObjectToMap(JSONObject jsonObject){
        Map<String,Object> map = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){
            String key = keys.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONArray){
                value = converJSONArrayToList((JSONArray)value);
            }else if (value instanceof JSONObject){
                value = convertJSONObjectToMap((JSONObject)value);
            }
            map.put(key,value);
        }
        return map;
    }

    /**
     * This helper method converts JSON Array to List
     * @param jsonArray
     * @return
     */
    public List<Object> converJSONArrayToList(JSONArray jsonArray){
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONArray){
                value = converJSONArrayToList((JSONArray)value);
            }else if (value instanceof JSONObject){
                value = convertJSONObjectToMap((JSONObject)value);
            }
            list.add(value);
        }
        return list;
    }

    public Integer getNumberOfElementFromJsonResponse(String jsonData){
        //TO DO
        return null;
    }

    public Response invokeDeleteApi(String dataApiBaseUri, String uri, HashMap<String,String> apiHeaderInfo, int expectedStatusCode,String jsonBodyValue) throws IOException {
        Response response = null;
        for (int iCount = 0; iCount < 3; iCount++) {
            response = apiActions.deleteApiWithHeaderAndBody(dataApiBaseUri, uri, apiHeaderInfo, jsonBodyValue);
            if (response!=null){
                if (response.statusCode()==expectedStatusCode){
                    report.log("INFO", "Response Expected Status Code : [" + expectedStatusCode + "] , Actual Status Code : ["+response.getStatusCode()+"]");
                    break;
                }
                if (iCount==2){
                    report.log("FAIL", "Getting response Code  other than : [" + expectedStatusCode + "] , even after [" + iCount + "] attempts");
                    break;
                }
                report.log("INFO", "Waiting for 2 minutes before making next API request");
                waitForSeconds(120);
            }else{
                report.log("INFO","API respones is null");
            }

        }
        return response;
    }
}
