package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/18/2017.
 */

public class SelectedNewsModel {

    private int thumbnails;
    private String shortTitle;
    private String longDescription;
    private String newsDate;

    public SelectedNewsModel() {
    }

    public SelectedNewsModel(int thumbnails, String shortTitle, String longDescription, String newsDate) {
        this.thumbnails = thumbnails;
        this.shortTitle = shortTitle;
        this.longDescription = longDescription;
        this.newsDate = newsDate;
    }

    public SelectedNewsModel(String shortTitle,String newsDate, String longDescription) {
        this.shortTitle = shortTitle;
        this.longDescription = longDescription;
        this.newsDate = newsDate;
    }

    public int getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(int thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }
}
