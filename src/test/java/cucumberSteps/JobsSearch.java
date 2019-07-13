package cucumberSteps;

import Utilities.JobPosition;
import apiHelper.HttpServiceResponse;
import apiHelper.HttpServicesBuilder;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import org.json.JSONArray;
import org.junit.Assert;

public class JobsSearch {
    ThreadLocal<HttpServicesBuilder> httpServicesCommonBuilder = null;
    JobPosition jobPositions[];

    public void beforeStartAPITest(String fileName) {
        apiHelper.StateHelper.clearScenarioState();
        apiHelper.StateHelper.clearStoryState();
        httpServicesCommonBuilder = new ThreadLocal<HttpServicesBuilder>() {
            @Override
            public HttpServicesBuilder initialValue() {
                String pathContent = "apiFiles-Json/" + fileName + ".json";
                try {
                    return new HttpServicesBuilder(pathContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    String  callAPI(String args) {
    beforeStartAPITest("JobsSearchAPI");

    HttpServiceResponse response = (httpServicesCommonBuilder
            .get()
            .build("JOB_DESCRIPTION")
            .resolveServiceRequestUrl("{PLACE_HOLDER}", args)
            .execute());
    return response.getResultAsString();
}

     private void get(String args){
        Gson gson = new Gson();
        jobPositions = gson.fromJson(callAPI(args), JobPosition[].class);
    }
    public void getFullResults(String args){
        int count = 0;
        String response = callAPI(args);
        JSONArray jsonArray = new JSONArray(response);
        while(response.equalsIgnoreCase("[]"))
        if (jsonArray.length()==50){
            response= callAPI(args.replace("1",count+""));
            count++;
        }
        Gson gson = new Gson();
        jobPositions = gson.fromJson(response, JobPosition[].class);
    }
    @When("^From git hub job search API call (.*) API\\.$")
    public void getResultsWithDescription(String description){
        description=description.replace(" ","+");
        get("?description="+description);
    }
    @When("^From git hub job API call with (.*) API\\.$")
    public void getResultsWithDescriptionandPages(String description){
        description=description.replace(" ","+");
        get("?description="+description+"&page=0");
    }
    @When("^From git hub job search API call with (.*) as description and (.*) as full time\\.$")
    public void getResultsWithDescriptionAndFullTime(String description, String fullTime){
        description=description.replace(" ","+");
        get("?description="+description+"&full_time="+fullTime);
    }
    @When("^From git hub job search API call with (.*) as Lat and (.*) as Long\\.$")
    public void getResultsWithLatLong(String lat, String lon){
        get("?lat="+lat+"&long="+lon);
    }
    @When("^From git hub job search API call with (.*) as description, location (.*) and (.*) as full time\\.$")
    public void getResultsWithDescriptionAndFullTime(String description, String location, String fullTime){
        description=description.replace(" ","+");
        location=location.replace(" ","+");
        get("?description="+description+"&location="+location+"&full_time="+fullTime);
    }
    @Then("^From API response , validate all search keywords (.*) is displayed\\.$")
    public void verify(String descriptionsText){
    String descriptions[]  = descriptionsText.split(" ");
    boolean descriptionFound= true;
        for (JobPosition jobPosition : jobPositions) {
            for (String description : descriptions) {
                if (!(jobPosition.getDescription().toLowerCase().contains(description.toLowerCase()))) {
                    System.out.println("Title = "+jobPosition.getTitle()+" Description = "+descriptionsText);
                    System.out.println("Location = "+jobPosition.getLocation().toLowerCase()+" Description = "+descriptionsText);
                    descriptionFound = false;
                    break;
                }
            }
        }
        Assert.assertTrue("Not Found : "+descriptionsText,descriptionFound);
    }

}
