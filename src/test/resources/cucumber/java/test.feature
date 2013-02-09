Feature: Web scraper
  
  Scenario: Read the 2012 album collection from bunalti.com
    Given a scraper is available
    When I scrap the first 5 pages of the 2012 albums of bunalti.com
    Then a file named "output_2012.txt" is created
    Then the "output_2012.txt" file should have 75 rows
    
  Scenario: Read the 2013 album collection from bunalti.com
    Given a scraper is available
    Given we are the 30/01/2013
    When I scrap all 2013 albums of bunalti.com, back to 28/01/2013
    Then a file named "output_2013-20130208.txt" is created