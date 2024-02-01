package main.java.com.core.wrapper.mobile;

import com.testkit.helper.mobile.MobileTestHelper;
import com.testkit.wrapper.Wrapper;
import org.testng.asserts.SoftAssert;

public class MobileTestWrapper extends Wrapper {
    MobileTestHelper mobileTestHelper = null;

    public MobileTestWrapper() {

    }

    public void createDeviceConnection() {

        SoftAssert softAssert = new SoftAssert();
        if (!mobileTestHelper.setAdbConnection(testData.get("deviceIp"))) {
            softAssert.fail("Not able to ADB connect device [" + testData.get("deviceIp") + "]");
        } else {
            if (mobileTestHelper.setUp()) {
                System.out.println("Android Driver created successfully");
            } else {
                System.out.println("Fail to create Android Driver");
            }
        }
        softAssert.assertAll();
    }

    /***
     * This method runs in loop until it gets adb disconnection flag from helper method verifyAdbConnectionExists()
     */
    public void verifyDeviceDisconnected(){
        SoftAssert softAssert = new SoftAssert();
        int iCounter = 0;
        try{
            while(mobileTestHelper.verifyAdbConnectionExist(testData.get("deviceIp"))&& iCounter <5){
                mobileTestHelper.waitForSeconds(10);//10 seconds
                iCounter++;
            }
            if (iCounter==5){
                softAssert.fail("Device is still connected to ADB, may be did not go for reboot");
            }
        }catch (Exception e){
            softAssert.fail(String.valueOf(e));
        }
        softAssert.assertAll();
    }

    public void verifyDeviceIsConnected(){
        SoftAssert softAssert = new SoftAssert();
        try{
            if (mobileTestHelper.verifyAdbConnectionExist(testData.get("deviceIp"))){
                System.out.println("Device ["+testData.get("deviceIp")+ "] is ADB connected");
            }else{
                String msg = "Device is not connected to ADB";
            }
        }catch (Exception e){
            softAssert.fail(String.valueOf(e));
        }
        softAssert.assertAll();
    }
}
