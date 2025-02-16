package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.LoginPage;

public class LoginWithGmail extends BaseTest {

	@Test
	public void verifyGoogleLoginButton() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.clickGoogleLogin();
		
		String actualTitle = driver.getTitle();
		Assert.assertEquals("Sign in - Google Accounts", actualTitle);
		
	}
}
