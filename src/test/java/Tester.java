import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Nikita Fomichev fomichev.n@raidix.com
 *         Date: 06-May-17
 */
public class Tester {

    private List<List<String>> resultTable = new ArrayList<>();
    private Map<String, Map<String, Long>> resultsTable = new HashMap<>();

    @DataProvider(name = "Drivers")
    public static Object[][] drivers() {
        return new Object[][]{
                {DriverProvider.FIREFOX, "FirefoxDriver"},
                {DriverProvider.CHROME, "ChromeDriver"},
                {DriverProvider.CHROME_HEADLESS, "ChromeDriverHeadless"},
                {DriverProvider.PHATNOMJS, "PhantomJSDriver"},
                {DriverProvider.HTMLUNIT, "HtmlUnitDriver"}
        };
    }

    @Test(dataProvider = "Drivers")
    public void test(DriverProvider driverProvider, String driverName) {
        WebDriver driver = timeCounterRet(driverProvider::getDriver);

//        timeCounter(()->driver.get("https://google.com/"));
//        timeCounter(()->driver.findElement(By.cssSelector("")));

        driver.quit();
    }

    @AfterClass
    public void afterClass(){

    }


    private <T> T timeCounterRet(Supplier<T> toCalc) {
        long startTime = System.currentTimeMillis();
        T t = toCalc.get();
        long endTime = System.currentTimeMillis()-startTime;
        System.out.println(endTime/1000.0);
        return t;
    }

    private <T> void timeCounter(Supplier<T> toCalc) {
        long startTime = System.currentTimeMillis();
        toCalc.get();
        long endTime = System.currentTimeMillis()-startTime;
        System.out.println(endTime/1000.0);
    }


}
