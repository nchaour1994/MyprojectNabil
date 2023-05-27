package pages;

import base.CommonApi;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage extends CommonApi {

    public SignInPage(WebDriver driver){
        this.driver=driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(id = "user_name")
    public
    WebElement emailField;
    @FindBy(id = "user_password")
    public
    WebElement passwordField;
    @FindBy(id = "submit_btn")
    WebElement signIn;


    public void typeOnEmail(){
        type(emailField,"john1899@gmail.com");
    }
    public void typeOnPassword(String password){
        type(passwordField,password);
    }

    public void ClickOnSignIn(){
        click(signIn);
    }
}
