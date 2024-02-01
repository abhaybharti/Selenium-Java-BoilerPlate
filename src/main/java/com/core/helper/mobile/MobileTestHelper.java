package main.java.com.core.helper.mobile;

import com.testkit.helper.Helper;
import com.testkit.utils.store.Cache;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MobileTestHelper extends Helper {
    AppiumDriver driver;
    public MobileTestHelper(String environment) {
        super(environment);
    }

    public boolean setUp(){
        boolean result = false;
        try{
            String deviceIp = Cache.getCacheString("deviceIp");
            setAdbConnection(deviceIp);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", Cache.getCacheString("deviceIp")+":5555");
            capabilities.setCapability("automationName", "uiautomater2");
            capabilities.setCapability("newCommandTiemout","600000");
            capabilities.setCapability("noReset",true);
            driver = new AppiumDriver("http://127.0.0.1:4723/wd/hub", capabilities);
            if (driver != null){
                rm.log("FAIL", "Fail to create android driver");
            }else{
                result = true;
                rm.log("PASS", "Pass to create android driver");
            }
            result = true;
        }catch (Exception e){
            rm.log("FAIL", "Driver creation fail with message "+e);
            Assert.fail("Driver creation fail with message "+e);
        }catch (Error e){
            rm.log("FAIL", "Driver creation fail with message "+e);
            Assert.fail("Driver creation fail with message "+e);
        }
        return result;
    }
    public boolean setAdbConnection(String deviceIp){
        String command = "adb connect "+deviceIp;
        System.out.println("Executing command : ["+command+"]");
        boolean result = false;
        Process process = null;
        try{
            if (verifyAdbConnectionExist(deviceIp)){
                process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null){
                    System.out.println(line);
                    if (line.contains("Connected to "+deviceIp)){
                        result = true;
                        System.out.println("Device ["+deviceIp+"] connection to adb is successful");
                    }
                }
                reader.close();
            }else{
                result = true;
                System.out.println("Device ["+deviceIp+"] is already ADB connected");
            }
        }catch (Exception e){
            System.out.println("setAdbConnection() failed with exception ["+e+"]");
        }
        return result;
    }

    public boolean verifyAdbConnectionExist(String deviceIp){
        String command = "adb devices";
        System.out.println("Executing command : ["+command+"]");
        boolean result = false;
        Process process = null;
        try{
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
                if (Pattern.matches(deviceIp+":(\\d+)(\\s+)(device)",line)){
                    result = true;
                    System.out.println("Device ["+deviceIp+"] connected to adb");
                }
            }
            reader.close();
        }catch (Exception e){
            System.out.println("verifyAdbConnectionExist() failed with exception ["+e+"]");
        }
        return result;
    }

    /***
     * This method creates connection with device & poll for device reboot
     * 1. Check device is connected (calls verifyAdbConnectionExist())
     * 2. If device is connected, creates connection with device
     * 3. Calls for pollForReboot() to know device reboot status
     * @param deviceIp
     * @return
     */
    public boolean verifyDeviceReboot(String deviceIp){
        boolean rebootStatus = false;
        int iCounter = 0;
        while(iCounter <10){
            if (!setAdbConnection(deviceIp)){
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }else{
                break;
            }
            iCounter++;
        }
        boolean connectionStatus = verifyAdbConnectionExist(deviceIp);
//        if (connectionStatus){
//            rebootStatus = pollForReboot(10,deviceIp); //wait for maximum 5 minute
//        }else{
//            return connectionStatus;
//        }
//
//        if (rebootStatus){
//            rm.log("INFO","Device is successfully rebooted");
//        }else{
//            rm.log("FAIL","Device is successfully rebooted");
//        }
        return rebootStatus;
    }

//    private boolean pollForReboot(int timeOut, String deviceIp){
//        boolean returnVal = false;
//        int lastTotalTime =0, currentUpTime = 0;
//    }

    public ArrayList<String> executeSystemCommand(String command){
        ArrayList<String> output = new ArrayList<>();
        try{
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder line = null;
            while ((line = new StringBuilder(reader.readLine())) != null){
                output.add(line.toString());
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
