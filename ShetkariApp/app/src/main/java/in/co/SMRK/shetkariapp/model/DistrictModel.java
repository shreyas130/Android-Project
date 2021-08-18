package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/28/2017.
 */

public class DistrictModel {

    private int DistrictId;
    private String DistrictName;

    public DistrictModel(int districtId, String districtName) {
        DistrictId = districtId;
        DistrictName = districtName;
    }

    public DistrictModel() {
    }

    public int getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(int districtId) {
        DistrictId = districtId;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    @Override
    public String toString() {
        return getDistrictName();
    }
}
