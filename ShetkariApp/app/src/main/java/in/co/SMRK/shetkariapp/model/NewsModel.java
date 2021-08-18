package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/17/2017.
 */

public class NewsModel  {

    private int newsId;
    private String newsShortTitle;
    private String newsDate;
    private String thumbnails;


    public NewsModel(int newsId, String newsShortTitle, String newsDate,String thumbnails) {
        this.newsId = newsId;
        this.newsShortTitle = newsShortTitle;
        this.newsDate = newsDate;
        this.thumbnails = thumbnails;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }



    public NewsModel() {
    }


    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getNewsShortTitle() {
        return newsShortTitle;
    }

    public void setNewsShortTitle(String newsShortTitle) {
        this.newsShortTitle = newsShortTitle;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    @Override
    public String toString() {
        return getNewsShortTitle();
    }
}
