package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/28/2017.
 */

public class StateModel {

    private int StateId;
    private String stateName;


    public StateModel() {
    }

    public StateModel(int stateId, String stateName) {
        StateId = stateId;
        this.stateName = stateName;
    }

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return getStateName();
    }
}
