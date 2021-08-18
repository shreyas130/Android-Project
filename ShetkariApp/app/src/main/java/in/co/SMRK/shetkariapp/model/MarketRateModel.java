package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/12/2017.
 */

public class MarketRateModel  {
    private int rateId;
    private String marketPlace;
    private String currentDate;
    private String minimumRate;
    private String maximumRate;


    public MarketRateModel(int rateId, String marketPlace, String currentDate, String minimumRate, String maximumRate) {
        this.rateId = rateId;
        this.marketPlace = marketPlace;
        this.currentDate = currentDate;
        this.minimumRate = minimumRate;
        this.maximumRate = maximumRate;
    }

    public MarketRateModel() {
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public String getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(String marketPlace) {
        this.marketPlace = marketPlace;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getMinimumRate() {
        return minimumRate;
    }

    public void setMinimumRate(String minimumRate) {
        this.minimumRate = minimumRate;
    }

    public String getMaximumRate() {
        return maximumRate;
    }

    public void setMaximumRate(String maximumRate) {
        this.maximumRate = maximumRate;
    }
}
