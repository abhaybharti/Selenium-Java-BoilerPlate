package main.java.com.core.basetest;

import com.testkit.actions.web.SeleniumActions;
import org.testng.annotations.AfterMethod;

public class WebTestSetup extends TestSetup {

  @AfterMethod
  public void testCleanup() {
    SeleniumActions.closeBrowser();
  }
}
