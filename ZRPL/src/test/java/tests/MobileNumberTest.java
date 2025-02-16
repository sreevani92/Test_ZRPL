package tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mailosaur.MailosaurClient;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;

import base.BaseTest;
import pages.LoginPage;

public class MobileNumberTest extends BaseTest {

	@Test
	public void verifyLoginWithValidMobileNumber() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectMobileNumberOption();
		loginPage.MobileNumberField("7815915699");
		loginPage.clickSendOTPMobile();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 200);");

		// Setup the API client
		MailosaurClient mailosaur = new MailosaurClient("cTnDjJ2wxyxEKZSFwoL53ZLfcsy9GPWQ");

		MessageSearchParams params = new MessageSearchParams();
		params.withServer("qopisodj");

		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.withSentTo("123456789"); // Phone number

		// Introduce a wait to ensure the email arrives
		Message message = null;
		int retryCount = 0;
		while (retryCount < 5) {
			try {
				message = mailosaur.messages().get(params, searchCriteria);
				if (message != null)
					break;
			} catch (Exception e) {
				System.out.println("Waiting for OTP SMS...");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ignored) {
				}
			}
			retryCount++;
		}

		if (message == null) {
			System.out.println("Failed to retrieve OTP SMS.");
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

	@Test
	public void verifyLoginWithValidMobileNumberAndInvalidOTP() {

		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectMobileNumberOption();
		loginPage.MobileNumberField("7815915691");
		loginPage.clickSendOTPMobile();

		// Scroll down
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");

		// Wait for OTP field to be visible and interactable
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement otpField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='otpVerify']")));

		// Enter OTP
		otpField.sendKeys("123456");

		driver.findElement(By.xpath("//*[@id=\"submitOTP\"]")).click();

		Assert.assertEquals(
				driver.findElement(By.xpath("//*[@id=\"mobile_otp_form\"]/section/div/div[3]/div/p[1]")).getText(),
				"Wrong OTP, Please Enter the Correct OTP");
	}

	@Test
	public void verifyLoginWithInvalidMobileNumber() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectMobileNumberOption();
		loginPage.MobileNumberField("78159156611");
		loginPage.clickSendOTPMobile();
		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"mobile_number-error\"]")).getText(),
				"Please enter a valid 10-digit mobile number.");

	}
}