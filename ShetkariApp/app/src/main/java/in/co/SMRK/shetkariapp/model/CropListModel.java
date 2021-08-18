package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/16/2017.
 */

public class CropListModel {

    private int cropId;
    private String cropName;

    public String getThumnails() {
        return thumnails;
    }

    public void setThumnails(String thumnails) {
        this.thumnails = thumnails;
    }

    private String thumnails;


    public CropListModel() {
    }

    public CropListModel(int cropId, String cropName,String thumnails) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.thumnails = thumnails;
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

    @Override
    public String toString() {
        return getCropName();
    }
}
