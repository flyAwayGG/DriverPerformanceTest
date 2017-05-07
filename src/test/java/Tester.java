import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Nikita Fomichev fomichev.n@raidix.com
 *         Date: 06-May-17
 */
public class Tester {

    private Map<String, Map<String, Long>> resultsTable = new HashMap<>();
    private Map<String,Long> benchResult;
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
        benchResult = new HashMap<>();

        WebDriver driver = calc(driverProvider::getDriver,"Driver initialization");

        calc(() -> driver.get("https://google.com/"),"get(String)");
//        calc(()->driver.findElement(By.cssSelector("")));


        //driver.getClient("file:///home/nikita/idea_projects/playground/index.html");
        driver.get("file:///C:/Users/Nikita/playground/index.html");
        // Test 1
        String title = driver.getTitle();
        driver.findElement(By.id("answer1")).sendKeys(title);

        // Test 2
        driver.findElement(By.id("name")).sendKeys("Kilgore Trout");

        //Test 3
        driver.findElement(By.cssSelector("#occupation > [value=scifiauthor]")).click();

        // Test 4
        int blueBoxes = driver.findElements(By.cssSelector(".bluebox")).size();
        driver.findElement(By.id("answer4")).sendKeys(Integer.toString(blueBoxes));
        System.out.println(blueBoxes);

        // Test 5
        driver.findElement(By.linkText("click me")).click();

        // Test 6
        String redboxCssSelector = driver.findElement(By.id("redbox")).getAttribute("class");
        driver.findElement(By.id("answer6")).sendKeys(redboxCssSelector);

        // Test 7
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("ran_this_js_function()");

        // Test 8
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object returnValue = js.executeScript("return got_return_from_js_function()");
        driver.findElement(By.id("answer8")).sendKeys(String.valueOf(returnValue));

        //Test 9
        driver.findElement(By.cssSelector("[value=wrotebook]")).click();

        // Test 10
        String redboxText = driver.findElement(By.id("redbox")).getText();
        driver.findElement(By.id("answer10")).sendKeys(redboxText);

        // Test 11
        int greenBoxY = driver.findElement(By.id("greenbox")).getLocation().getY();
        int orangeBoxY = driver.findElement(By.id("orangebox")).getLocation().getY();
        System.out.println(greenBoxY + ", " + orangeBoxY);
        String elementOnTop = orangeBoxY > greenBoxY ? "green" : "orange";
        driver.findElement(By.id("answer11")).sendKeys(elementOnTop);

        // Test 12
        Dimension dimension = new Dimension(850, 650);
        driver.manage().window().setSize(dimension);

        // Test 13
        Boolean elemIsHereDisplayed = driver.findElements(By.id("ishere")).size() > 0;
//        Boolean elemIsHereDisplayed = (Boolean)(new WebDriverWait(driver, 10))
//               .until(ExpectedConditions
//                       .visibilityOfElementLocated(By.id("ishere")));
        String isDisplayed = elemIsHereDisplayed ? "yes" : "no";
        driver.findElement(By.id("answer13")).sendKeys(isDisplayed);

        // Test 14
        Boolean elemPurpleboxDisplayed = driver.findElement(By.id("purplebox")).isDisplayed();
        String purpleboxIsDisplayed = elemPurpleboxDisplayed ? "yes" : "no";
        driver.findElement(By.id("answer14")).sendKeys(purpleboxIsDisplayed);

        // Test 15
        driver.findElement(By.linkText("click then wait")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
         wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.linkText("click after wait")));
        driver.findElement(By.linkText("click after wait")).click();

        // Test 16
        driver.switchTo().alert().accept();

        // Test 17
        driver.findElement(By.id("submitbutton")).click();

        // Check Result
        driver.findElement(By.id("checkresults")).click();



        driver.quit();
        resultsTable.put(driverName,benchResult);
    }

    @AfterClass
    public void afterClass(){
        calcTable();
    }

    private void calcTable() {

    }


    private <T> T calc(Supplier<T> toCalc, String description) {
        long startTime = System.currentTimeMillis();
        T t = toCalc.get();
        long endTime = System.currentTimeMillis()-startTime;
        benchResult.put(description,endTime);
        return t;
    }

    private <T> void calc(Runnable toCalc, String description) {
        long startTime = System.currentTimeMillis();
        toCalc.run();
        long endTime = System.currentTimeMillis()-startTime;
        benchResult.put(description,endTime);
    }

}
