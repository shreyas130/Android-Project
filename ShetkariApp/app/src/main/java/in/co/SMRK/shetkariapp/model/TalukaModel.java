package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/28/2017.
 */

public class TalukaModel  {

    private int TalukaId;
    private String talukaName;

    public TalukaModel() {
    }

    public TalukaModel(int talukaId, String talukaName) {
        TalukaId = talukaId;
        this.talukaName = talukaName;
    }

    public int getTalukaId() {
        return TalukaId;
    }

    public void setTalukaId(int talukaId) {
        TalukaId = talukaId;
    }

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }

    @Override
    public String toString() {
        return getTalukaName();
    }
}
