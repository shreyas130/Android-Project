package in.co.SMRK.shetkariapp.model;


public class CropModel  {

    private int farmerId;
    private int cropId;
    private String cropName;
    private String thumbnails;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag;


    public CropModel() {
    }

    public CropModel(int farmerId, int cropId, String cropName, String flag) {
        this.farmerId = farmerId;
        this.cropId = cropId;
        this.cropName = cropName;
        this.flag = flag;
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

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }
}
