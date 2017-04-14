package support.esri.com.fuse.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableWoeId {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parentid")
    @Expose
    private Integer parentid;
    @SerializedName("placeType")
    @Expose
    private PlaceType placeType;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("woeid")
    @Expose
    private Integer woeid;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWoeid() {
        return woeid;
    }

    public void setWoeid(Integer woeid) {
        this.woeid = woeid;
    }

}