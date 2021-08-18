package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/23/2017.
 */

public class GalleryModel  {

    private String galleryId;
    private String galleryTitle;



    private String thumnails;


    public GalleryModel(String galleryId, String galleryTitle,String thumnails) {
        this.galleryId = galleryId;
        this.galleryTitle = galleryTitle;
        this.thumnails = thumnails;
    }

    public GalleryModel() {
    }

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getGalleryTitle() {
        return galleryTitle;
    }

    public void setGalleryTitle(String galleryTitle) {
        this.galleryTitle = galleryTitle;
    }
    public String getGalleryImage() {
        return galleryImage;
    }

    public void setGalleryImage(String galleryImage) {
        this.galleryImage = galleryImage;
    }

    private String galleryImage;

    public GalleryModel(String galleryImage) {
        this.galleryImage = galleryImage;
    }

    public String getThumnails() {
        return thumnails;
    }

    public void setThumnails(String thumnails) {
        this.thumnails = thumnails;
    }
}
