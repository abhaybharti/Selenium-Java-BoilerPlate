import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WriteCodeForFirefoxBrowser {
 WebDriver driver;
  @BeforeClass
  public void setUp(){
    WebDriverManager.firefoxdriver().setup();
  }

  @Test
  public void yourTestMethod(){
    driver = new FirefoxDriver();
    driver.get("https://yahoo.com");
  }

  @AfterClass
  public void tearDown(){
    if (driver!=null){
      driver.quit();
    }
  }
}
