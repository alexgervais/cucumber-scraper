Feature: Web scraper

  Scenario: Read the 2014 album collection from bunalti.com
    Given a scraper is available
    When I scrap the first 50 pages of the 2014 albums of bunalti.com
    Then a file named "output_2014.txt" is created
    Then the "output_2014.txt" file should have 187 rows

  Scenario: Read the 2013 album collection from bunalti.com
    Given a scraper is available
    Given we are the 15/01/2014
    When I scrap all 2013 albums of bunalti.com, back to 25/09/2013
    Then a file named "output_2013-20140115.txt" is created