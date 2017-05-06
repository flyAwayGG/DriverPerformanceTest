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
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/usr/bin/google-chrome");
            options.addArguments("--window-size=1200x600");
            System.setProperty("webdriver.chrome.driver", DRIVERS_PATH + "chromedriver");
            return new ChromeDriver(options);
        }
    },
    CHROME_HEADLESS {
        public WebDriver getDriver() {
            System.setProperty("webdriver.chrome.driver", DRIVERS_PATH + "chromedriver");

            ChromeOptions options = new ChromeOptions();
            options.setBinary("/usr/bin/google-chrome");
            options.addArguments("--headless", "--window-size=1200x600");
            return new ChromeDriver(options);
        }
    },
    PHATNOMJS {
        public WebDriver getDriver() {
            DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
            capabilities.setCapability("phantomjs.binary.path", "/home/nikita/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
            return new PhantomJSDriver(capabilities);
        }
    },
    HTMLUNIT {
        public WebDriver getDriver() {
            return new HtmlUnitDriver(true);
        }
    };

    private final static String DRIVERS_PATH = "src/test/resources/drivers/";

    public abstract WebDriver getDriver();

}
