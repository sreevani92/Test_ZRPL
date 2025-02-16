package tests;

import java.awt.AWTException;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
import base.BaseTest;
import pages.LoginPage;

public class PersonalEmailIdTest extends BaseTest {

	@Test
	public void verifyLoginWithValidPersonalEmail() throws IOException, MailosaurException, AWTException {

		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectPersonalEmailOption();
		loginPage.EmailField("stomach-burst@qopisodj.mailosaur.net");
		loginPage.clickSendOTPEmail();

		// Setup the API client
		MailosaurClient mailosaur = new MailosaurClient("cTnDjJ2wxyxEKZSFwoL53ZLfcsy9GPWQ");

		MessageSearchParams params = new MessageSearchParams();
		params.withServer("qopisodj");

		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.withSentTo("stomach-burst@qopisodj.mailosaur.net");

		// Introduce a wait to ensure the email arrives
		Message message = null;
		int retryCount = 0;
		while (retryCount < 5) {
			try {
				message = mailosaur.messages().get(params, searchCriteria);
				if (message != null)
					break;
			} catch (Exception e) {
				System.out.println("Waiting for OTP email...");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ignored) {
				}
			}
			retryCount++;
		}

		if (message == null) {
			System.out.println("Failed to retrieve OTP email.");
			return;
		}

		// Extract OTP code
		String otpCode = message.html().codes().isEmpty() ? message.text().codes().get(0).value()
				: message.html().codes().get(0).value();

		if (otpCode == null || otpCode.isEmpty()) {
			System.out.println("OTP Code not found!");
			return;
		}

		// Scroll down
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");

		// Wait for OTP field to be visible and interactable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement otpField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='otpVerify']")));

		// Enter OTP
		otpField.sendKeys(otpCode);

		driver.findElement(By.xpath("//*[@id=\"submitOTP\"]")).click();

		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"emp_inst_details\"]/section/div/h1")).getText(),
				"ZEPP SPP registration form");

	}

	public void verifyLoginWithValidEmailAndInvalidOTP() {

		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectPersonalEmailOption();
		loginPage.EmailField("tomorrow-source@qopisodj.mailosaur.net");
		loginPage.clickSendOTPEmail();

		// Wait for OTP field to be visible and interactable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement otpField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='otpVerify']")));

		// Enter OTP
		otpField.sendKeys("1235456");

		driver.findElement(By.xpath("//*[@id=\"submitOTP\"]")).click();

		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"otp_form\"]/section/div/div[3]/div/p[1]")).getText(),
				"Wrong OTP, Please Enter the Correct OTP");
	}

	@Test
	public void verifyLoginWithInvalidEmail() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectPersonalEmailOption();
		loginPage.EmailField("testemail.com");
		loginPage.clickSendOTPEmail();
		Assert.assertEquals(loginPage.getErrorMessage(),
				"Please enter a valid email address (Ex: johndoe@domain.com).");
	}

}
