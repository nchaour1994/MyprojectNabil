package base;

import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import properties.GetProperties;
import reporting.ExtentManager;
import reporting.ExtentTestManager;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class CommonApi {

    public WebDriver driver;
    public Properties prop = GetProperties.loadProperty();
    public static com.relevantcodes.extentreports.ExtentReports extent;
    public DesiredCapabilities capabilities = new DesiredCapabilities();


    String titleHomePage = prop.getProperty("titleHomePage");

    //-------------------------------------------------------
    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void startExtent(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName().toLowerCase();
        ExtentTestManager.startTest(method.getName());
        ExtentTestManager.getTest().assignCategory(className);
    }

    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));

        for (String group : result.getMethod().getGroups()) {
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        }
        ExtentTestManager.endTest();
        extent.flush();
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getName());
        }
        driver.quit();
    }

    @AfterSuite
    public void generateReport() {
        extent.close();
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }


    //---------------------------------------------

    @BeforeSuite
    @Parameters("useGrid")
    public void startDocker(@Optional("false") boolean useGrid) {
        System.out.println("starting grid---------------------");
        if (useGrid == true) {
            try {
                Runtime.getRuntime().exec("cmd /c start C:\\Users\\nchao\\IdeaProjects\\MyprojectNabil\\start-docker.bat");
                Thread.sleep(15000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @BeforeMethod
    @Parameters({"useDocker", "browser","localBrowser"})
    public void init(@Optional("false") boolean useDocker, @Optional("firefox") String browser,@Optional("firefox") String localBrowser) throws MalformedURLException {


        if (useDocker == false) {
            if(localBrowser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
            }else {
                WebDriverManager.firefoxdriver().setup();
                driver=new FirefoxDriver();
            }
            driver.manage().window().maximize();
            driver.get("https://www.walgreens.com/");
        } else {
            if (browser.equalsIgnoreCase("firefox")) {
                //  capabilities.setBrowserName("chrome");
                capabilities.setBrowserName("firefox");
                capabilities.setVersion("latest");
                URL url = new URL("http://54.89.222.29:4444/wd/hub");
                driver = new RemoteWebDriver(url, capabilities);
            } else if (browser.equalsIgnoreCase("chrome")) {
                capabilities.setBrowserName("chrome");
                capabilities.setVersion("latest");
                URL url = new URL("http://54.89.222.29:4444/wd/hub");
                driver = new RemoteWebDriver(url, capabilities);
            }
        }


        driver.get("https://www.walgreens.com/");
        driver.manage().timeouts().implicitlyWait(Duration.of(10, ChronoUnit.SECONDS));
        Assert.assertEquals(titleHomePage, driver.getTitle());
    }

    public void click(WebElement element) {
        element.click();
    }

    public void waitFor(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(9));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void type(WebElement element, String value) {
        element.sendKeys(value);

    }

    public void takeScreenshot(String screenshotName) {
        DateFormat df = new SimpleDateFormat("(MM_dd_yyyy-HH-MM-SS)");
        Date date = new Date();
        df.format(date);

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(System.getProperty("user.dir") + "/screenshots/" + screenshotName + " " + df.format(date) + ".png"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            String path = System.getProperty("user.dir") + "/screenshots/" + screenshotName + " " + df.format(date) + ".png";
            System.out.println(path);
            System.out.println("Exception while taking screenshot " + e.getMessage());
            ;
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @AfterSuite
    @Parameters("useGrid")
    public void stopDocker(@Optional("false") boolean useGrid) {
        if (useGrid == true) {
            try {
                Runtime.getRuntime().exec("cmd /c start C:\\Users\\nchao\\IdeaProjects\\MyprojectNabil\\stop-docker.bat");
                Thread.sleep(1500);
                System.out.println("stopping grid---------------------");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

//            try {
//                Runtime.getRuntime().exec("taskkill /f /im cmd.exe");
//                System.out.println("closing cmd---------------------");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

}
