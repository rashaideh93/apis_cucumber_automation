package runner;


import courgette.api.CourgetteOptions;
import courgette.api.CourgetteRunLevel;
import courgette.api.junit.Courgette;
import cucumber.api.testng.TestNGCucumberRunner;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import org.testng.annotations.Test;

@RunWith(Courgette.class)
@CourgetteOptions(
        threads = 4,
        runLevel = CourgetteRunLevel.SCENARIO,
        rerunFailedScenarios = true,
        showTestOutput = true,
        cucumberOptions =  @CucumberOptions(plugin = {"html:target/cucumber-html-report","junit:target/cucumber-junit.xml",
                "json:target/cucumber.json","pretty:target/cucumber-pretty.txt",
                "usage:target/cucumber-usage.json"},
                glue = {"cucumberSteps", "runner"},
                strict = true,
            monochrome = true,
                features="src/test/resources/cucumber-features/"))
public class RunCukesTest {
        @Test(description = "Example of using TestNGCucumberRunner to invoke Cucumber")
        public void runCukes() {
            new TestNGCucumberRunner(getClass()).runCukes();
        }

}