
package support.esri.com.fuse.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ClosestWoeId {

    @SerializedName("country")
    private String mCountry;
    @SerializedName("countryCode")
    private String mCountryCode;
    @SerializedName("name")
    private String mName;
    @SerializedName("parentid")
    private Long mParentid;
    @SerializedName("placeType")
    private PlaceType mPlaceType;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("woeid")
    private Long mWoeid;

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getParentid() {
        return mParentid;
    }

    public void setParentid(Long parentid) {
        mParentid = parentid;
    }

    public PlaceType getPlaceType() {
        return mPlaceType;
    }

    public void setPlaceType(PlaceType placeType) {
        mPlaceType = placeType;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Long getWoeid() {
        return mWoeid;
    }

    public void setWoeid(Long woeid) {
        mWoeid = woeid;
    }

}
