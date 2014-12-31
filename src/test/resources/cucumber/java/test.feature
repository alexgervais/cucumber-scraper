Feature: Web scraper

  Scenario: Read the 2015 album collection from bunalti.com
    Given a scraper is available
    When I scrap the first 50 pages of the 2015 albums of bunalti.com
    Then a file named "output_2015.txt" is created
    Then the "output_2015.txt" file should have 29 rows

  Scenario: Read the 2014 album collection from bunalti.com
    Given a scraper is available
    Given we are the 28/12/2014
    When I scrap all 2014 albums of bunalti.com, back to 13/11/2014
    Then a file named "output_2014-20141228.txt" is created