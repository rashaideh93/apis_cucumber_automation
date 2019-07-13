$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/test/resources/cucumber-features/JobsSearch.feature");
formatter.feature({
  "name": "Verify Job Search API",
  "description": "",
  "keyword": "Feature"
});
formatter.scenarioOutline({
  "name": "Verify Searching For Positions With Description, Location and Full Time.",
  "description": "",
  "keyword": "Scenario Outline"
});
formatter.step({
  "name": "From git hub job search API call with \u003cDescription\u003e as description, location \u003cLocation\u003e and \u003cFull_Time\u003e as full time.",
  "keyword": "When "
});
formatter.step({
  "name": "From API response , validate all search keywords \u003cLocation\u003e is displayed.",
  "keyword": "Then "
});
formatter.examples({
  "name": "",
  "description": "",
  "keyword": "Examples",
  "rows": [
    {
      "cells": [
        "Description",
        "Location",
        "Full_Time"
      ]
    },
    {
      "cells": [
        "java",
        "New York",
        "true"
      ]
    },
    {
      "cells": [
        "java",
        "New York",
        "false"
      ]
    },
    {
      "cells": [
        "java",
        "Universal City",
        "true"
      ]
    },
    {
      "cells": [
        "java",
        "Universal City",
        "false"
      ]
    },
    {
      "cells": [
        "java",
        "Chicago",
        "true"
      ]
    },
    {
      "cells": [
        "java",
        "Chicago",
        "false"
      ]
    },
    {
      "cells": [
        "java",
        "Remote",
        "true"
      ]
    },
    {
      "cells": [
        "java",
        "Remote",
        "false"
      ]
    }
  ]
});
formatter.scenario({
  "name": "Verify Searching For Positions With Description, Location and Full Time.",
  "description": "",
  "keyword": "Scenario Outline"
});
formatter.before({
  "status": "passed"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "From git hub job search API call with java as description, location Universal City and false as full time.",
  "keyword": "When "
});
formatter.match({
  "location": "JobsSearch.getResultsWithDescriptionAndFullTime(String,String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "From API response , validate all search keywords Universal City is displayed.",
  "keyword": "Then "
});
formatter.match({
  "location": "JobsSearch.verify(Striformatter.result({
  "status": "passed"
});
formatter.step({
  "name": "From API response , validate all search keywords Salesforce Engineer is displayed.",
  "keyword": "Then "
});
formatter.match({
  "location": "JobsSearch.verify(String)"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});"status": "failed"
});
formatter.after({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});