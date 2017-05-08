import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Nikita Fomichev fomichev.n@raidix.com
 *         Date: 06-May-17
 */
public enum DriverProvider {

    FIREFOX {
        public WebDriver getDriver() {
            System.setProperty("webdriver.gecko.driver", DRIVERS_PATH + "geckodriver");
            return new FirefoxDriver();
        }
    },
    CHROME {
        public WebDriver getDriver() {
            System.setProperty("webdriver.chrome.driver", DRIVERS_PATH + "chromedriver");
            
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/usr/bin/google-chrome");
            return new ChromeDriver(options);
        }
    },
    CHROME_HEADLESS {
        public WebDriver getDriver() {
            System.setProperty("webdriver.chrome.driver", DRIVERS_PATH + "chromedriver");

            ChromeOptions options = new ChromeOptions();
            options.setBinary("/usr/bin/google-chrome");
            options.addArguments("--headless");
            return new ChromeDriver(options);
        }
    },
    PHATNOMJS {
        public WebDriver getDriver() {
            DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
            capabilities.setCapability("phantomjs.binary.path", PHANTOMJS_BINARY);
            capabilities.setJavascriptEnabled(true);
            return new PhantomJSDriver(capabilities);
        }
    },
    HTMLUNIT {
        public WebDriver getDriver() {
            return new HtmlUnitDriver(true);
        }
    };

    private static final String DRIVERS_PATH = "src/test/resources/drivers/";
    private static final String PHANTOMJS_BINARY = "/home/nikita/phantomjs-2.1.1-linux-x86_64/bin/phantomjs";

    public abstract WebDriver getDriver();

}
