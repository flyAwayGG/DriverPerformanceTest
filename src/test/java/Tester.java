import org.apache.commons.lang3.RandomStringUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openqa.selenium.*;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Nikita Fomichev fomichev.n@raidix.com
 *         Date: 06-May-17
 */
public class Tester {

    private Map<String, Map<String, Double>> resultsTable = new HashMap<>();
    private Map<String, Double> benchResult;
    private long startTime;
    private String driverName;
//    private Map<String, Point> elementLocationsInDrivers = new HashMap<>();

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

    @BeforeMethod
    public void startTime() {
        benchResult = new HashMap<>();
        startTime = System.currentTimeMillis();
    }

    @Test(dataProvider = "Drivers")
    public void test(DriverProvider driverProvider, String driverName) {
        this.driverName = driverName;

        WebDriver driver = driverProvider.getDriver(); //calc(driverProvider::getDriver, "Initialization");

        /*
         * Test 1
         * Measure time to load html page
         */
        driver.get("file://"+System.getProperty("user.dir")+"/src/test/resources/playground/index.html");
//        calc(() -> driver.get("file://"+System.getProperty("user.dir")+"/src/test/resources/playground/index.html"), "Test1.load");

        /*
         * Test 2
         * Find 1, 100, 1000 elements with method driver.findElement(By.cssSelector(String))
         */
        calc(() -> driver.findElement(By.cssSelector(".div1")), "Test2.1");
        calc(() -> driver.findElement(By.cssSelector(".div100")), "Test2.100");
        calc(() -> driver.findElement(By.cssSelector(".div1000")), "Test2.1000");

        /*
         * Test 3
         * Find 1, 100, 1000 elements with method driver.findElements(By.cssSelector(String))
         */
        calc(() -> driver.findElements(By.cssSelector(".div1")), "Test3.1");
        calc(() -> driver.findElements(By.cssSelector(".div100")), "Test3.100");
        calc(() -> driver.findElements(By.cssSelector(".div1000")), "Test3.1000");

        /*
         * Test 4
         * Click on 1, 100 and 1000 buttons
         */
        WebElement button = driver.findElement(By.cssSelector(".button"));
        calc(button::click, "Test4");

        /*
         * Test 5
         * Send keys to input
         */
        WebElement input1 = driver.findElement(By.cssSelector(".input1"));
        CharSequence cs1 = RandomStringUtils.randomAlphanumeric(1);
        calc(() -> input1.sendKeys(cs1), "Test5");

        /*
         * Test 6
         * Clear input with various number of chars
         */
        calc(input1::clear, "Test6");

        /*
         * Test 7
         * getLocation
         */
        calc(input1::getLocation, "Test7");

        /*
         * Test 8
         * getRect
         */
        calc(input1::getSize, "Test8");

        /*
         * Test 9
         * isDisplayed
         *
         */
        calc(input1::isDisplayed, "Test9");

        /*
         * Test 10
         * isEnabled
         */
        WebElement checkbox = driver.findElement(By.cssSelector(".checkbox"));
        checkbox.click();
        calc(checkbox::isEnabled, "Test10");

        /*
         * Test 11
         * isSelected
         */
        calc(checkbox::isSelected, "Test11");

        /*
         * Test 12
         * JavascriptExecution
         */
        calc(() -> ((JavascriptExecutor) driver).executeScript("$('input').size()"), "Test12");

        driver.quit();
    }

    @AfterMethod
    public void endTime() {
        long total = System.currentTimeMillis() - startTime;
        //benchResult.put("Total", total/1000.0);
        resultsTable.put(driverName, benchResult);
    }

    @AfterClass
    public void afterClass() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        for(Map.Entry<String,Map<String,Double>> driverResult : resultsTable.entrySet()){
            for(Map.Entry<String,Double> testResult : driverResult.getValue().entrySet()){
                dataset.addValue(testResult.getValue(),driverResult.getKey(),testResult.getKey());
            }
        }

        Chart.build(dataset);
    }


    private <T> T calc(Supplier<T> toCalc, String description) {
        long startTime = System.currentTimeMillis();
        T t = toCalc.get();
        long total = System.currentTimeMillis() - startTime;
        benchResult.put(description, total/1000.0);
        return t;
    }

    private void calc(Runnable toCalc, String description) {
        long startTime = System.currentTimeMillis();
        toCalc.run();
        long total = System.currentTimeMillis() - startTime;
        benchResult.put(description, total/1000.0);
    }

}
