package self.scraper;

import org.joda.time.LocalDate;

public class Album {
    
    private String title;
    private String link;
    private LocalDate publishedDate;

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getLink() {

        return link;
    }

    public void setLink(String link) {

        this.link = link;
    }

    public LocalDate getPublishedDate() {

        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {

        this.publishedDate = publishedDate;
    }
}
