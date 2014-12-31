package self.scraper;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scraper {

    private LocalDate currentDate;
    private static final Map<Integer, Integer> yearToCategoryMap = new HashMap<Integer, Integer>();

    static {
        yearToCategoryMap.put(2012, 2189);
        yearToCategoryMap.put(2013, 2605);
        yearToCategoryMap.put(2014, 2851);
        yearToCategoryMap.put(2015, 3102);
    }


    public void scrape(int pageCount, int year) {

        try {
            scrape(1, pageCount, yearToCategoryMap.get(year), getOutputFileName(year));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scrape(LocalDate backTo, int year) {

        try {
            scrape(backTo, yearToCategoryMap.get(year), getOutputFileName(year));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentDate(final LocalDate currentDate) {

        this.currentDate = currentDate;
    }

    private void scrape(int from, int to, int category, final String outputFileName) throws IOException, InterruptedException {

        FileOutputStream fos = new FileOutputStream(outputFileName, true);

        for (int i = from; i <= to; i++) {
            System.out.println(String.format("Processing page %s", i));
            Document document = getPage(category, i);
            List<Album> albums = getAlbums(document);

            for (Album album : albums) {
                fos.write(String.format("%s - %s [%s]\n", album.getPublishedDate(), album.getTitle(), album.getLink()).getBytes());
            }

            fos.write("+\n".getBytes());
            Thread.sleep(500);
        }
    }

    private void scrape(final LocalDate backTo, int category, final String outputFileName) throws IOException, InterruptedException {

        FileOutputStream fos = new FileOutputStream(outputFileName, true);

        boolean backInTimeLimitReached = false;
        int pageIndex = 1;
        while (!backInTimeLimitReached) {
            System.out.println(String.format("Processing page %s", pageIndex));
            Document document = getPage(category, pageIndex++);
            List<Album> albums = getAlbums(document);

            for (Album album : albums) {
                fos.write(String.format("%s - %s [%s]\n", album.getPublishedDate(), album.getTitle(), album.getLink()).getBytes());

                if (album.getPublishedDate().isBefore(backTo)) {
                    backInTimeLimitReached = true;
                    break;
                }
            }

            fos.write("+\n".getBytes());
            Thread.sleep(500);
        }
    }

    private List<Album> getAlbums(Document document) {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd yyyy");

        List<Album> albums = new ArrayList<>();
        Elements entries = document.getElementsByClass("entry");
        for (Element element : entries) {
            Element titleAndLinkElements = element.getElementsByClass("entrytitle").get(0);
            Element dateElement = element.getElementsByClass("entrymeta").get(0);

            final Album album = new Album();
            album.setTitle(titleAndLinkElements.text());
            album.setLink(titleAndLinkElements.getElementsByTag("a").get(0).attr("href"));
            album.setPublishedDate(formatter.parseLocalDate(dateElement.text().replaceAll("(?<= \\d{0,10})(?:st|nd|rd|th)(?= \\d+$)", "")));

            albums.add(album);
        }
        return albums;
    }

    private Document getPage(int category, int pageIndex) throws IOException {

        Document document = null;
        while (document == null) {
            try {
                document = Jsoup.connect(String.format("http://www.bunalti.com/?cat=%s&paged=%s", category, pageIndex)).get();
            } catch (SocketTimeoutException ignored) {

            }
        }
        return document;
    }

    private String getOutputFileName(int year) {

        String outputFileName = String.format("output_%s.txt", year);
        if (currentDate != null) {
            outputFileName = String.format("output_%s-%s%02d%02d.txt", year, currentDate.getYear(), currentDate.getMonthOfYear(), currentDate.getDayOfMonth());
        }
        return outputFileName;
    }
}
