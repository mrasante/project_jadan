package support.esri.com.fuse.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("quality")
    @Expose
    private Integer quality;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("offsetlat")
    @Expose
    private String offsetlat;
    @SerializedName("offsetlon")
    @Expose
    private String offsetlon;
    @SerializedName("radius")
    @Expose
    private Integer radius;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("line1")
    @Expose
    private String line1;
    @SerializedName("line2")
    @Expose
    private String line2;
    @SerializedName("line3")
    @Expose
    private String line3;
    @SerializedName("line4")
    @Expose
    private String line4;
    @SerializedName("house")
    @Expose
    private String house;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("xstreet")
    @Expose
    private String xstreet;
    @SerializedName("unittype")
    @Expose
    private String unittype;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("postal")
    @Expose
    private String postal;
    @SerializedName("neighborhood")
    @Expose
    private String neighborhood;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("countrycode")
    @Expose
    private String countrycode;
    @SerializedName("statecode")
    @Expose
    private String statecode;
    @SerializedName("countycode")
    @Expose
    private String countycode;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("woeid")
    @Expose
    private Integer woeid;
    @SerializedName("woetype")
    @Expose
    private Integer woetype;
    @SerializedName("uzip")
    @Expose
    private String uzip;

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOffsetlat() {
        return offsetlat;
    }

    public void setOffsetlat(String offsetlat) {
        this.offsetlat = offsetlat;
    }

    public String getOffsetlon() {
        return offsetlon;
    }

    public void setOffsetlon(String offsetlon) {
        this.offsetlon = offsetlon;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getXstreet() {
        return xstreet;
    }

    public void setXstreet(String xstreet) {
        this.xstreet = xstreet;
    }

    public String getUnittype() {
        return unittype;
    }

    public void setUnittype(String unittype) {
        this.unittype = unittype;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getCountycode() {
        return countycode;
    }

    public void setCountycode(String countycode) {
        this.countycode = countycode;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getWoeid() {
        return woeid;
    }

    public void setWoeid(Integer woeid) {
        this.woeid = woeid;
    }

    public Integer getWoetype() {
        return woetype;
    }

    public void setWoetype(Integer woetype) {
        this.woetype = woetype;
    }

    public String getUzip() {
        return uzip;
    }

    public void setUzip(String uzip) {
        this.uzip = uzip;
    }

}
