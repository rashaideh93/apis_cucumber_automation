package runner;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static apiHelper.StateHelper.clearStoryState;

public class Hooks  {

    //custom Before scenario hook
    @Before
    public void beforeScenario() {
        System.out.println("custom Before scenario hook");
    }

    //custom After scenario hook
    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("custom After scenario hook");
        clearStoryState();

    }

}