package tests;

import base.CommonApi;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignInPage;
import properties.GetProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class TestLogin extends CommonApi {


    String titleSignInPage = prop.getProperty("titleSignInPage");
    String email = prop.getProperty("username");
    String password = prop.getProperty("password");
    String invalidPassword = prop.getProperty("invalidPassword");


    @Test
    public void loginWithValidCredentials() {
        HomePage homePage = new HomePage(driver);
        SignInPage signInPage = new SignInPage(driver);


        homePage.clickOnAccountBtn();
        Assert.assertEquals("true", homePage.accountBtn.getAttribute("aria-expanded"));
//        waitFor(homePage.dropDown);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Assert.assertTrue(false);
        homePage.clickOnSignInBtn();
        Assert.assertEquals(titleSignInPage, driver.getTitle());
        signInPage.typeOnEmail();
        Assert.assertEquals(email, signInPage.emailField.getAttribute("value"));
        signInPage.typeOnPassword(password);
        Assert.assertEquals(password, signInPage.passwordField.getAttribute("value"));
        signInPage.ClickOnSignIn();
        Assert.assertTrue(true);
        System.out.println(driver.manage().getCookies());
        System.out.println(capabilities.getBrowserName());


    }

    @Test
    public void loginWithInValidCredentials() throws MalformedURLException {


        HomePage homePage = new HomePage(driver);
        SignInPage signInPage = new SignInPage(driver);


        homePage.clickOnAccountBtn();
        Assert.assertEquals("true", homePage.accountBtn.getAttribute("aria-expanded"));
      //  waitFor(homePage.dropDown);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        homePage.clickOnSignInBtn();
        Assert.assertEquals(titleSignInPage, driver.getTitle());
        signInPage.typeOnEmail();
        Assert.assertEquals(email, signInPage.emailField.getAttribute("value"));
        signInPage.typeOnPassword(invalidPassword);
        Assert.assertEquals(invalidPassword, signInPage.passwordField.getAttribute("value"));
        signInPage.ClickOnSignIn();
        Assert.assertFalse(false);
        System.out.println(driver.manage().getCookies());
        System.out.println(capabilities.getBrowserName());

    }


}
