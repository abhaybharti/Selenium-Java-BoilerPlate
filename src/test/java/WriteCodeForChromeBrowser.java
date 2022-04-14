import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WriteCodeForChromeBrowser {
 WebDriver driver;
  @BeforeClass
  public void setUp(){
    WebDriverManager.chromedriver().setup();
  }

  @Test
  public void yourTestMethod(){
    driver = new ChromeDriver();
    driver.get("https://yahoo.com");
  }

  @AfterClass
  public void tearDown(){
    if (driver!=null){
      driver.quit();
    }
  }

}
