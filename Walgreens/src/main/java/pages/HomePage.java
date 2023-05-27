package pages;

import base.CommonApi;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends CommonApi {


    public HomePage(WebDriver driver){
        this.driver=driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//a[@data-element-name='Account']")
    public WebElement accountBtn;

    @FindBy(xpath = "//a[@data-element-name='Account']")
   public WebElement dropDown;

    @FindBy(xpath = "//button[@data-element-name='Sign in']")
    WebElement signInBtn;


    public void clickOnAccountBtn(){
        click(accountBtn);
    }
    public void clickOnSignInBtn(){
        click(signInBtn);
    }
}
