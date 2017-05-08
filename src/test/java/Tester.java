import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.testng.annotations.*;
import org.w3c.dom.css.Rect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Nikita Fomichev fomichev.n@raidix.com
 *         Date: 06-May-17
 */
public class Tester {

    private Map<String, Map<String, Long>> resultsTable = new HashMap<>();
    private Map<String, Long> benchResult;
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

        WebDriver driver = calc(driverProvider::getDriver, "Initialization");

        /*
         * Test 1
         * Measure time to load html page
         */
        calc(() -> driver.get("file:///home/nikita/DriverPerformanceTest/src/test/resources/playground/index.html"), "Test1.load");


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
//        WebElement input2 = driver.findElement(By.cssSelector(".input2"));
//        WebElement input3 = driver.findElement(By.cssSelector(".input3"));
        CharSequence cs1 = RandomStringUtils.randomAlphanumeric(1);
//        CharSequence cs2 = RandomStringUtils.randomAlphanumeric(100);
//        CharSequence cs3 = RandomStringUtils.randomAlphanumeric(1000);
        calc(() -> input1.sendKeys(cs1), "Test5.1");
//        calc(() -> input2.sendKeys(cs2), "Test5.10");
//        calc(() -> input3.sendKeys(cs3), "Test5.100");


        /*
         * Test 6
         * Clear input with various number of chars
         */
        calc(input1::clear, "Test6.1");
//        calc(input2::clear, "Test6.100");
//        calc(input3::clear, "Test6.1000");


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

        /*
        Headless Chrome failing
        org.openqa.selenium.WebDriverException: unknown error: cannot get automation extension
        from unknown error: page could not be found: chrome-extension://aapnijgdinlhnhlmodcfapnahmbfebeb/_generated_background_page.html
        (Session info: headless chrome=59.0.3071.36)
        (Driver info: chromedriver=2.29.461571 (8a88bbe0775e2a23afda0ceaf2ef7ee74e822cc5),platform=Linux 4.8.0-49-generic x86_64) (WARNING: The server did not provide any stacktrace information)

            // Dimension dimension = new Dimension(850, 650);
            // driver.manage().window().setSize(dimension);

        */

        driver.quit();
    }

    @AfterMethod
    public void endTime() {
        long total = System.currentTimeMillis() - startTime;
        benchResult.put("Total", total);
        resultsTable.put(driverName, benchResult);
    }

    @AfterClass
    public void afterClass() {
        calcTable();
    }

    private void calcTable() {

    }

    private <T> T calc(Supplier<T> toCalc, String description) {
        long startTime = System.currentTimeMillis();
        T t = toCalc.get();
        long total = System.currentTimeMillis() - startTime;
        benchResult.put(description, total);
        return t;
    }

    private void calc(Runnable toCalc, String description) {
        long startTime = System.currentTimeMillis();
        toCalc.run();
        long total = System.currentTimeMillis() - startTime;
        benchResult.put(description, total);
    }

}
