
package support.esri.com.fuse.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultSet {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("Error")
    @Expose
    private Integer error;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("Locale")
    @Expose
    private String locale;
    @SerializedName("Quality")
    @Expose
    private Integer quality;
    @SerializedName("Found")
    @Expose
    private Integer found;
    @SerializedName("Results")
    @Expose
    private List<Result> results = null;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
