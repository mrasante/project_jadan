package support.esri.com.fuse.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppRateLimit {

    @SerializedName("rate_limit_context")
    @Expose
    private RateLimitContext rateLimitContext;
    @SerializedName("resources")
    @Expose
    private Resources resources;

    public RateLimitContext getRateLimitContext() {
        return rateLimitContext;
    }

    public void setRateLimitContext(RateLimitContext rateLimitContext) {
        this.rateLimitContext = rateLimitContext;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

}
