package cucumber.scraper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.joda.time.LocalDate;
import self.scraper.Scraper;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class StepDefinitions {

    private Scraper scraper;

    @Then("^a file named \\\"([^\\\"]*)\\\" is created$")
    public void a_file_named_is_created(String fileName) throws Throwable {

        assertTrue(new File(fileName).exists());
    }

    @Given("^a scraper is available$")
    public void a_scraper_is_available() throws Throwable {

        scraper = new Scraper();
    }

    @When("^I scrap the first (\\d+) pages of the (\\d+) albums of bunalti.com$")
    public void I_scrap_the_first_pages_of_bunalti_com(int pageCount, int year) throws Throwable {

        scraper.scrape(pageCount, year);
    }

    @Then("^the \\\"([^\\\"]*)\\\" file should have (\\d+) rows$")
    public void the_file_should_have_rows(String fileName, int rows) throws Throwable {

        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fileName));
        lineNumberReader.skip(Integer.MAX_VALUE);

        assertThat(lineNumberReader.getLineNumber(), is(equalTo(rows)));
    }

    @Given("^we are the (\\d+)/(\\d+)/(\\d+)$")
    public void we_are_the_(int day, int month, int year) throws Throwable {

        scraper.setCurrentDate(new LocalDate(year, month, day));
    }

    @When("^I scrap all (\\d+) albums of bunalti.com, back to (\\d+)/(\\d+)/(\\d+)$")
    public void I_scrap_all_albums_of_bunalti_com_back_to_(int yearToScrape, int day, int month, int year) throws Throwable {
        
        scraper.scrape(new LocalDate(year, month, day), yearToScrape);
    }
}
