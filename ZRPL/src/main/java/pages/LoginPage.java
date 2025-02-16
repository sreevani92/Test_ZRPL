package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	private WebDriver driver;
	private WebDriverWait wait;

	// Locators
	private By instituteEmailRadio = By.xpath("//*[@id=\"maincontent\"]/div/div/section[2]/div/div/div[2]/div/div/div[1]/label/input");
	private By mobileNumberRadio = By.xpath("//*[@id=\"maincontent\"]/div/div/section[2]/div/div/div[2]/div/div/div[2]/label/input");
	private By personalEmailRadio = By.xpath("//*[@id=\"maincontent\"]/div/div/section[2]/div/div/div[2]/div/div/div[3]/label/input");
	private By emailInput = By.xpath("//*[@id=\"home_email\"]");
	private By sendLoginLinkButton = By.xpath("//*[@id=\"send_login_link\"]");
	private By googleLoginButton = By.xpath("//*[@id=\"maincontent\"]/div/div/section[2]/div/div/div[2]/div/div/div[4]/a");
	private By errorMessage = By.xpath("//*[@id='home_email-error']");
	private By errorMessageForUnRegigisteredEmail = By.xpath("//*[@id=\"register-validate-detail\"]/div[3]/div/div[2]/div/div/div");
	private By mobileNumberField = By.xpath("//*[@id='mobile_number']");
	private By sendOtpMobile = By.xpath("//*[@id='send_mobile_login_link']");
	private By sendOtpEmail = By.xpath("//*[@id='send_login_link']");
	private By personalEmailInput = By.xpath("//*[@id=\"home_email\"]");

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Set wait time
	}

	// Utility method for waiting
	private WebElement waitForElementVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void selectInstituteEmailOption() {
		driver.findElement(instituteEmailRadio).click();
	}

	public void selectMobileNumberOption() {
		driver.findElement(mobileNumberRadio).click();
	}

	public void selectPersonalEmailOption() {
		driver.findElement(personalEmailRadio).click();
	}

	public void EmailField(String email) {
		driver.findElement(emailInput).clear();
		driver.findElement(emailInput).sendKeys(email);
	}
	
	public void MobileNumberField(String mobile) {
		driver.findElement(mobileNumberField).clear();
		driver.findElement(mobileNumberField).sendKeys(mobile);
	}
	
	public void PersonalEmailField(String mobile) {
		driver.findElement(personalEmailInput).clear();
		driver.findElement(personalEmailInput).sendKeys(mobile);
	}


	public void clickSendLoginLink() {
		driver.findElement(sendLoginLinkButton).click();
	}

	public void clickSendOTPMobile() {
		driver.findElement(sendOtpMobile).click();
	}
	
	public void clickSendOTPEmail() {
		driver.findElement(sendOtpEmail).click();
	}

	public void clickGoogleLogin() {
		driver.findElement(googleLoginButton).click();
	}

	public String getErrorMessage() {
		return driver.findElement(errorMessage).getText();
	}

	public String getErrorMessageForUnrigisteredEmail() {

		WebElement errormessage = waitForElementVisible(errorMessageForUnRegigisteredEmail);

		return errormessage.getText();

	}
	
	

}
