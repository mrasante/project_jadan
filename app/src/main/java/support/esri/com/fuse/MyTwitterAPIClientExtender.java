package support.esri.com.fuse;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterRateLimit;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import support.esri.com.fuse.models.AppRateLimit;
import support.esri.com.fuse.models.AvailableWoeId;
import support.esri.com.fuse.models.TwitterTrends;

/**
 * Created by kwas7493 on 3/13/2017.
 */

public class MyTwitterAPIClientExtender extends TwitterApiClient {

    public MyTwitterAPIClientExtender(TwitterSession twitterSession) {
        super(twitterSession);
    }

    public CustomTwitterService getCustomTwitterService() {
        return getService(CustomTwitterService.class);
    }


    public interface CustomTwitterService {
        @GET("/1.1/trends/place.json")
        Call<List<TwitterTrends>> show(@Query("id") long id);

        @GET("/1.1/trends/available.json")
        Call<List<AvailableWoeId>> getAvailableWoeid();

        @GET("/1.1/application/rate_limit_status.json")
        Call<AppRateLimit> getRateLimit();
    }

}
