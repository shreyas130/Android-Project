package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/22/2017.
 */

public class WeatherModel {
    private String weatherLoc;
    private String minTemp;
    private String maxTemp;
    private String thmbnails;
    private String desc;
    private String huminity;

    public WeatherModel(String weatherLoc, String minTemp, String maxTemp, String thmbnails, String desc, String huminity) {
        this.weatherLoc = weatherLoc;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.thmbnails = thmbnails;
        this.desc = desc;
        this.huminity = huminity;
    }

    public WeatherModel() {
    }

    public String getWeatherLoc() {
        return weatherLoc;
    }

    public void setWeatherLoc(String weatherLoc) {
        this.weatherLoc = weatherLoc;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getThmbnails() {
        return thmbnails;
    }

    public void setThmbnails(String thmbnails) {
        this.thmbnails = thmbnails;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHuminity() {
        return huminity;
    }

    public void setHuminity(String huminity) {
        this.huminity = huminity;
    }
}
