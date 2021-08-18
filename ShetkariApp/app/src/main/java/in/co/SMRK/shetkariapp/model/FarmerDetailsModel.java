package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/29/2017.
 */

public class FarmerDetailsModel  {

    private int farmerId;
    private String mobileNo;
    private String stateName;
    private String districtName;
    private String talukaName;
    private String villageName;
    private String firstName;
    private String lastName;

    public FarmerDetailsModel(int farmerId, String mobileNo, String stateName, String districtName, String talukaName, String villageName) {
        this.farmerId = farmerId;
        this.mobileNo = mobileNo;
        this.stateName = stateName;
        this.districtName = districtName;
        this.talukaName = talukaName;
        this.villageName = villageName;
    }

    public FarmerDetailsModel() {
    }

    public FarmerDetailsModel(String firstName,String lastName,String mobileNo, String villageName) {
        this.mobileNo = mobileNo;
        this.villageName = villageName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
