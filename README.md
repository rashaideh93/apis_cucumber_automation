# apis_cucumber_automation

Java quickstart project for test automation, covering Github jobs and postions API, acceptance testing. Created with lessons learned from a large number of development projects to provide all commonly required components and concepts. 

**Concepts Included**
* Parallel test runs
* Shared state across cucumber step definitions
* Dependency injection
* Page Object pattern
* Common api interaction methods
* Commonly used test utility classes
* Objects Models

**Tools**
* Maven
* Cucumber-JVM
* JUnit
* Selenium

**Requirements**
In order to utilise this project you need to have the following installed locally:

* Maven 3
* Java 1.8

**Usage**
The project covers API testing for commonly used restfull framworks.
Each of these modules can be utilised independently of the others using maven profiles.

1. To run all API modules tests and cucumber test from the terminal, navigate to the project directory and run:

     `mvn clean install`

2. To run all API modules tests and cucumber test from the IDE, navigate to the project directory and build project and run the desired features. 

**Test details and challenges**

1. Creating test plan to cover all search criterias without using to much automated tests.
2. Cover positive and negative scenarios from API perspective and client perspective (wrong data).
3. Using input data as assertion data to validate general search beheovers .
4. Dealing with dynamic data (Positions will not last for ever).

**Test coverage**
1. Long/Lat, City and ZipCodes testing for jobs in some contries and assert retrived jobs location.
2. Test search terms that might include Job title, company, full time and validate the retrived jobs.
3. Test wrong search terms (invalid city codes, invalid text).


