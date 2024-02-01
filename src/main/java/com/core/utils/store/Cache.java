package main.java.com.core.utils.store;

import io.restassured.response.Response;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.HashMap;

public class Cache {
    private static HashMap<String,String> cacheString = new HashMap<>();
    private static HashMap<String,Boolean> cacheBoolean = new HashMap<>();
    private static HashMap<String,Integer> cacheInteger = new HashMap<>();
    private static HashMap<String, HashMap<String, ArrayList<String>>> cacheArrayList = new HashMap<String, HashMap<String, ArrayList<String>>>();

    private static HashMap<String, ITestResult> cacheITestResult = new HashMap<>();

    private static HashMap<String, Response> cacheApiResponse = new HashMap<>();

    public static void setCacheArrayList(String name,HashMap<String,ArrayList<String>> arrayListValue){
        cacheArrayList.put(name,arrayListValue);
    }

    public static HashMap<String, ArrayList<String>> getCacheArrayList(String name){
     return cacheArrayList.get(name);
    }

    public static void setCacheString(String name, String value){
        cacheString.put(name,value);
    }

    public static String getCacheString(String name){
        return cacheString.get(name);
    }

    public static void setCacheBoolean(String name, Boolean value){
        cacheBoolean.put(name,value);
    }

    public static Boolean getCacheBoolean(String name){
        return cacheBoolean.get(name);
    }

    public static void setCacheInteger(String name, Integer value){
        cacheInteger.put(name,value);
    }

    public static Integer getCacheInteger(String name){
        return cacheInteger.get(name);
    }

    public static void setCacheITestResult( ITestResult value){
        cacheITestResult.put("iTestResult",value);
    }

    public static ITestResult getCacheITestResult(){
        return cacheITestResult.get("iTestResult");
    }

    public static void setCacheApiResponse(String name,Response response){
        cacheApiResponse.put(name,response);
    }

    public static Response getCacheApiResponse(String name){
        return cacheApiResponse.get(name);
    }
}
