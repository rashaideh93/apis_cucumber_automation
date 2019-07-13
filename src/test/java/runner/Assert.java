package runner;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.testng.asserts.SoftAssert;

public class Assert {

	public static SoftAssert softAssert = new SoftAssert();

	@Before
	 public void beforeScenario() {
		softAssert = new SoftAssert();
	 }
	 @After
	 public void afterScenario() {
		 softAssert.assertAll();

	 }
}
