package in.co.SMRK.shetkariapp.model;

/**
 * Created by KK on 19/08/2017.
 */

public class CropSchdulerModel  {

    private int schedulerId;
    private String schedulerType;

    private String typeName;

    private int typeId;

    private String schedulerTitle;
    private String schedulerInfo;


    public CropSchdulerModel() {
    }

    public CropSchdulerModel(String typeName,int typeId) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public CropSchdulerModel(String schedulerTitle, String schedulerInfo) {
        this.schedulerTitle = schedulerTitle;
        this.schedulerInfo = schedulerInfo;
    }

    public CropSchdulerModel(int schedulerId, String schedulerType) {
        this.schedulerId = schedulerId;
        this.schedulerType = schedulerType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getSchedulerTitle() {
        return schedulerTitle;
    }

    public void setSchedulerTitle(String schedulerTitle) {
        this.schedulerTitle = schedulerTitle;
    }

    public String getSchedulerInfo() {
        return schedulerInfo;
    }

    public void setSchedulerInfo(String schedulerInfo) {
        this.schedulerInfo = schedulerInfo;
    }





    public int getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(int schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(String schedulerType) {
        this.schedulerType = schedulerType;
    }

    @Override
    public String toString() {
        return getSchedulerType();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
