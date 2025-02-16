package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;

public class InstituteEmailIdTest extends BaseTest {

	@Test
	public void verifyLoginWithValidInstituteEmail() {

		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectInstituteEmailOption();
		loginPage.EmailField("vanisri9244@gmail.com");
		loginPage.clickSendLoginLink();
		Assert.assertEquals(loginPage.getErrorMessageForUnrigisteredEmail(), 
				"Your domain is not registered with the SPP program. Please contact spp@zrpl.co.in for more information.");

	}

	@Test
	public void verifyLoginWithInvalidEmail() {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.selectInstituteEmailOption();
		loginPage.EmailField("testemail.com");
		loginPage.clickSendLoginLink();
		Assert.assertEquals(loginPage.getErrorMessage(),
				"Please enter a valid email address (Ex: johndoe@domain.com).");
	}

}