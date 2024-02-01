package test.java.sampletest;


import com.testkit.basetest.WebTestSetup;
import com.testkit.wrapper.web.WebTestWrapper;
import org.testng.annotations.Test;

public class DemoTests extends WebTestSetup {

  @Test
  public void firstTest() {
    // TestWrapper testWrapper = new TestWrapper.TestWrapperBuilderClass(testConfigData).build();
    WebTestWrapper testWrapper = new WebTestWrapper(testConfigData);
    testWrapper.scriptTestData.put("key", "value");

    testWrapper.scriptTestData.put("browserName", "firefox");
    testWrapper.login();
  }
}
