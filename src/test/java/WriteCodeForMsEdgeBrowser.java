import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WriteCodeForMsEdgeBrowser {
 WebDriver driver;
  @BeforeClass
  public void setUp(){
    WebDriverManager.edgedriver().setup();
  }

  @Test
  public void yourTestMethod(){
    driver = new EdgeDriver();
    driver.get("https://yahoo.com");
  }

  @AfterClass
  public void tearDown(){
    if (driver!=null){
      driver.quit();
    }
  }

}
