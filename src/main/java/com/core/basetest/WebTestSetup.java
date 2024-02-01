package main.java.com.core.basetest;


import org.testng.annotations.AfterMethod;

public class WebTestSetup extends TestSetup {

  @AfterMethod
  public void testCleanup() {
    SeleniumActions.closeBrowser();
  }
}
