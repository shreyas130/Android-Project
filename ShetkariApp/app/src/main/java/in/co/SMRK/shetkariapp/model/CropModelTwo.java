package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/30/2017.
 */

public class CropModelTwo  {

    private int cropId;
    private String cropName;
    private String thumbnails;

    public CropModelTwo(int cropId, String cropName, String thumbnails) {


        this.cropId = cropId;
        this.cropName = cropName;
        this.thumbnails = thumbnails;
    }

    public CropModelTwo() {
    }

    public int getCropId() {
        return cropId;
    }

    public void setCropId(int cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }
}
