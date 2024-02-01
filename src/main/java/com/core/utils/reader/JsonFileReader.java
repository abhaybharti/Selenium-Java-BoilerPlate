package main.java.com.core.utils.reader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.testkit.utils.store.Cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class JsonFileReader {

    //  private static final Logger logger = LogManager.getLogger(JsonFileReader.class);
    JsonObject jsonObject;

    // The `JsonFileReader` constructor is responsible for reading a JSON file and parsing its contents
    // into a `JsonObject`.
    public JsonFileReader(String filename, String env) {
        System.out.println("--- JsonReader->JsonReader() start --- filename : " + filename);
        // this.test_data = test_data;
        try {

            String envFolderName = null;
            switch (env.toUpperCase()) {
                case "QE":
                    envFolderName = "QE";
                    break;
                default:
                    System.out.println("---Incorrect environment name is passed in test data-------");
            }
            String filePath =
                    System.getProperty("user.dir")
                            + "\\src\\test\\resources\\JsonUIMap\\"
                            + envFolderName
                            + "\\"
                            + filename;
            File jsonFilePath = new File(filePath);

            if (jsonFilePath.exists()) {
                FileReader jsonFileReader;
                jsonFileReader = new FileReader(jsonFilePath);
                this.jsonObject = new JsonParser().parse(jsonFileReader).getAsJsonObject();
            } else {
                System.out.println(filePath + "  File not found");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Exception Occured : " + e);
        } catch (JsonSyntaxException e) {
            this.jsonObject = null;
            System.out.println("JSON File Parse Exception: " + e);
        }
        System.out.println("--- JsonReader->JsonReader() end ---");
    }

    // The `getElementData` method is used to retrieve a specific property value from a JSON file.
    public String getElementData(String jsonHierarchy, String propertyName) {
        String[] jsonElementHierarchy = jsonHierarchy.split("\\.");

        try {

            JsonObject jsonElementObject = jsonObject.getAsJsonObject(jsonElementHierarchy[0]);
            for (int i = 1; i < jsonElementHierarchy.length; i++) {
                jsonElementObject = jsonElementObject.getAsJsonObject(jsonElementHierarchy[i]);
            }

            for (String key : jsonElementObject.keySet()) {
                if (key.equalsIgnoreCase(propertyName)) {
                    return jsonElementObject.get(key).getAsString();
                }
            }

        } catch (Exception e) {
            System.out.println("" + e);
        }
        return "propertyName " + propertyName + " not found in hierarchy";
    }

    /***
     * This method returns list of EndPoint names from JSON file
     * @param tabName
     * @return
     */
    public ArrayList<String> getEndPointNames(String tabName) {
        JsonObject elementObj = jsonObject.getAsJsonObject("Application").getAsJsonObject(tabName);
        int iCount = 0;
        ArrayList<String> out = new ArrayList<>();
        for (String key : elementObj.keySet()) {
            if (elementObj.get(key).isJsonObject()) {
                JsonObject subElementObj = elementObj.getAsJsonObject(key);
                if (subElementObj.get("elementType").getAsString().equalsIgnoreCase("EndPoint")) {
                    out.add(subElementObj.get("endPointName").getAsString());
                    iCount++;
                }
            }
        }
        return out;
    }

    /***
     * This method is same as getElementData, writtent o read value from API response
     * @param jsonHierarchy
     * @param propertyName
     * @return
     */
    public StringBuilder getElementDataFromAPI(String jsonHierarchy, String propertyName) {
        StringBuilder elementNotFound = new StringBuilder("elementName [" + propertyName + "] not found in hierarchy :[" + jsonHierarchy + "]");
        StringBuilder hierarchyNotFound = new StringBuilder("hierarchy [" + jsonHierarchy + "] not found");
        String[] jsonHierarchyArray = jsonHierarchy.split("\\.");
        try {
            JsonObject elementObj = jsonObject.getAsJsonObject(jsonHierarchyArray[0]);
            for (int iCount = 1; iCount < jsonHierarchyArray.length; iCount++) {
                elementObj = elementObj.getAsJsonObject(jsonHierarchyArray[iCount]);
            }

            for (String key : elementObj.keySet()) {
                if (key.equalsIgnoreCase(propertyName)) {
                    return new StringBuilder(elementObj.get(key).getAsString());
                }
            }
            return new StringBuilder(elementNotFound + " in JSON file");
        } catch (Exception e) {
            return new StringBuilder(hierarchyNotFound + " in JSON file");
        }
    }

    /***
     * This method returns value from json response using JSON key hierarchy
     * @param jsonObjectApi
     * @param hierarchy
     * @return
     */
    public String getJsonObjectForApi(JsonObject jsonObjectApi, String hierarchy) {
        String keys = "";
        String[] hierarchyArray = hierarchy.split("\\.");
        String paramNameToValueFromJson = hierarchyArray[hierarchyArray.length - 1];
        if (hierarchyArray.length == 1) {
            keys = jsonObjectApi.get(paramNameToValueFromJson).getAsString();
            return keys;
        }

        JsonObject elementObj = jsonObjectApi.getAsJsonObject(hierarchyArray[0].toString());
        for (int iCount = 1; iCount < hierarchyArray.length - 1; iCount++) {
            elementObj = elementObj.getAsJsonObject(hierarchyArray[iCount].toString());
        }

        for (String key : elementObj.keySet()) {
            if (key.equalsIgnoreCase(paramNameToValueFromJson) && elementObj.get(key).isJsonObject()) {
                JsonObject jsObj = elementObj.get(key).getAsJsonObject();
                return key;
            } else {
                for (String paramName : elementObj.keySet()) {
                    if (paramName.equalsIgnoreCase(paramNameToValueFromJson)) {
                        String temp = elementObj.get(paramNameToValueFromJson).getAsString();
                        return temp;
                    }
                }
            }
        }
        return "key does not exists";
    }

    /***
     * This method will return the ParameterValue from JSON Objects.
     * @param jsonObjectList
     * @param paramName
     * @return
     */
    public ArrayList<String> getElementsFromJsonObject(ArrayList<JsonObject> jsonObjectList, String paramName) {
        ArrayList<String> paramList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjectList) {
            for (String key : jsonObject.keySet()) {
                if (key.equalsIgnoreCase(paramName)) {
                    String out = jsonObject.get(key).getAsString();
                    paramList.add(out);
                }
            }
        }
        return paramList;
    }

    /***
     * This method return the ParameterValue from JSON Object.
     * @param jsonObject
     * @param paramName
     * @return
     */
    public String getElementFromJsonObject(JsonObject jsonObject, String paramName) {
        ArrayList<String> paramList = new ArrayList<>();
        for (String key : jsonObject.keySet()) {
            if (key.equalsIgnoreCase(paramName)) {
                return jsonObject.getAsJsonObject(key).getAsString();
            }
        }
        return null;
    }

    public ArrayList<JsonObject> getElementLabels(String hierarchy) {
        String[] hierarchyArray = hierarchy.split("\\.");
        JsonObject elementObj = jsonObject.getAsJsonObject(hierarchyArray[0]);
        for (int iCount = 0; iCount < hierarchyArray.length; iCount++) {
            elementObj = elementObj.getAsJsonObject(hierarchyArray[iCount]);
        }

        ArrayList<JsonObject> out = new ArrayList<>();
        for (String key : elementObj.keySet()) {
            if (elementObj.get(key).isJsonObject()) {
                for (String sectionKey : elementObj.getAsJsonObject(key).keySet()) {
                    if (sectionKey.equalsIgnoreCase("elementType")) {
                        String elementType = elementObj.getAsJsonObject(key).get(sectionKey).getAsString();
                        if (elementType.equalsIgnoreCase("section") || elementType.equalsIgnoreCase("subsection") || elementType.equalsIgnoreCase("Accordion") || elementType.equalsIgnoreCase("table")) {

                        } else {
                            if (elementObj.getAsJsonObject(key).get("isDeviceSetting") != null && elementObj.getAsJsonObject(key).get("isDeviceSetting").getAsString().equalsIgnoreCase("false")) {
                                continue;
                            }
                            if (verifySupportedModelAndUxVerification(elementObj.getAsJsonObject(key))) {
                                out.add(elementObj.getAsJsonObject(key));
                            }
                        }
                    }
                }
            }
        }
        return out;
    }

    /***
     *
     * @param jsonObject
     * @return
     */
    public boolean verifySupportedModelAndUxVerification(JsonObject jsonObject) {
        boolean result = true;
        for (String key : jsonObject.keySet()) {
            if (key.equalsIgnoreCase("NotSupportedModels")) {
                String out = jsonObject.get(key).getAsString();
                result = false;
                break;
            }
        }
        return result;
    }

    /***
     * This function fetches all the tab names from JSON file
     * @return
     */
    public ArrayList<String> getTabNames() {
        JsonObject elementObj = jsonObject.getAsJsonObject("Application");
        int iCount = 0;
        String elementType = "Tab";
        ArrayList<String> out = new ArrayList<>();
        for (String key : elementObj.keySet()) {
            if (elementObj.get(key).isJsonObject()) {
                JsonObject sectionObj = elementObj.getAsJsonObject(key);
                if (!sectionObj.keySet().contains("labelName") || (!elementType.isEmpty() &&
                        !sectionObj.get("elementType").getAsString().equalsIgnoreCase("elementType") ||
                        sectionObj.get("label").getAsString().equalsIgnoreCase("DummyGroupname") ||
                        sectionObj.get("label").getAsString().equalsIgnoreCase("DummyDeviceName"))) {
                    continue;
                }

                if (verifySupportedModelAndUxVerification(sectionObj)) {
                    out.add(sectionObj.get("lableName").getAsString());
                    iCount++;
                }
            }
        }
        return out;
    }

    /***
     * This method returns list of all fields[Name as hierarchy] in the given hierarchy
     * @param hierarchy
     * @return
     */
    public ArrayList<String> getAllElementsHierarchy(String hierarchy) {
        String[] hierarchyArray = hierarchy.split("\\.");
        JsonObject elementObj = jsonObject.getAsJsonObject(hierarchyArray[0]);

        for (int iCount = 0; iCount < hierarchyArray.length; iCount++) {
            elementObj = elementObj.getAsJsonObject(hierarchyArray[iCount]);
        }

        boolean flag = true;

        ArrayList<String> out = new ArrayList<>();
        for (String key : elementObj.keySet()) {
            if (elementObj.get(key).isJsonObject()) {
                flag = false;
                out.addAll(getAllElementsHierarchy((hierarchy + "." + key)));
            }

            if (flag) {
                out.add(hierarchy);
                return out;
            }
        }
        return out;
    }

    public boolean isSupportedModel(String NotSupportedModels) {
        String deviceType = Cache.getCacheString("deviceType");
        for (String notSupportModel : NotSupportedModels.split(",")) {
            if (deviceType.equalsIgnoreCase(notSupportModel)) {
                return false;
            }
        }
        return true;
    }

    /***
     * This method fetches all fields label value based on hierarchy and elementType from JSON file
     * @param hierarchy
     * @param elementType
     * @return
     */
    public ArrayList<String> getElementLabelsList(String hierarchy, String elementType) {
        String[] hierarchyArray = hierarchy.split("\\.");
        JsonObject elementObj = jsonObject.getAsJsonObject(hierarchyArray[0]);
        for (int iCount = 0; iCount < hierarchyArray.length; iCount++) {
            elementObj = elementObj.getAsJsonObject(hierarchyArray[iCount]);
        }
        int iEleLoop = 0;
        ArrayList<String> out = new ArrayList<>();
        for (String key : elementObj.keySet()) {
            if (elementObj.get(key).isJsonObject()){
                JsonObject sectionObj = elementObj.getAsJsonObject(key);
                if (!sectionObj.keySet().contains("labelName") || (!elementType.isEmpty() && !sectionObj.get("elementType").getAsString().equalsIgnoreCase(elementType) )){
                    continue;
                }

                if (verifySupportedModelAndUxVerification((sectionObj))){
                    out.add(sectionObj.get("labelName").getAsString().trim());
                    iEleLoop++;
                }
            }
        }
        return out;
    }

    public ArrayList<String> getElement(String hierarchy){
        String [] hierarchyArray = hierarchy.split("\\.");
        JsonObject elementObj = jsonObject.getAsJsonObject(hierarchyArray[0]);
        for (int iCount = 0; iCount < hierarchyArray.length; iCount++) {
            elementObj = elementObj.getAsJsonObject(hierarchyArray[iCount]);
        }

        int iLoop = 0;
        ArrayList<String> out = new ArrayList<>();
        for (String key : elementObj.keySet()){
            if (elementObj.get(key).isJsonObject()){
                JsonObject sectionObj = elementObj.getAsJsonObject(key);
                if (!sectionObj.keySet().contains("labelName")){
                    continue;
                }

                if (verifySupportedModelAndUxVerification(sectionObj)){
                    out.add(hierarchy+"."+key);
                    iLoop++;
                }
            }
        }
        return out;
    }


}
