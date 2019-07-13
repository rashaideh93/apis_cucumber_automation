Feature: Verify Job Search API


 Scenario Outline: Verify Searching For Positions With Description.
      When From git hub job search API call <description> API.
      Then From API response , validate all search keywords <description> is displayed.
   Examples:
   |description|
   |developer|
   |ruby|
   |java|
   |ios|
   |Android|
   |Android developer|
   |Web developer    |
   |swift            |
   |objc             |
   |Remote IOS|
   |Remote Android|
   |DevOps Engineer|
   |Senior PHP Developer|
   |Salesforce Engineer |
   |Remote Web Developer|
   |Remote Web Developer HTML|
   |Remote Web Developer JavaScript|
   |Remote Web Developer JavaScript HTML/CSS|
   |Not Valid|



  Scenario Outline: Verify Searching For Positions With Description and Full Time.
    When From git hub job search API call with <Description> as description and <Full_Time> as full time.
    Then From API response , validate all search keywords <Description> is displayed.
    Examples:
      |Description|Full_Time|
      |java|true|
      |java|false|

  Scenario Outline: Verify Searching For Positions With Description, Location and Full Time.
    When From git hub job search API call with <Description> as description, location <Location> and <Full_Time> as full time.
    Then From API response , validate all search keywords <Location> is displayed.
    Examples:
      |Description|Location|Full_Time|
      |java|New York|true|
      |java|New York|false|
      |java|Universal City|true|
      |java|Universal City|false|
      |java|Chicago|true|
      |java|Chicago|false|
      |java|Remote|true|
      |java|Remote|false|

  Scenario Outline: Verify Searching For Positions With Lat and Long.
    When From git hub job search API call with <Lat> as Lat and <Long> as Long.
    Then From API response , validate all search keywords <Location> is displayed.
    Examples:
      |Lat|Long|Location|
      |111111111|111111111||
      |-111111111|-111111111||
      |00000000|00000000||
      |40.7128|74.0060|New York|
      |41.8781|87.6298|Chicago|

  Scenario Outline: Verify Searching For Positions With ZipCode.
    When From git hub job API call with <ZipCode> API.
    Then From API response , validate all search keywords <Location> is displayed.
    Examples:
      |ZipCode|Location|
      |10010|New York|
      |60007|Chicago|
      |00000000090909090909090909|       |
