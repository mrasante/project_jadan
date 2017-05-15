package support.esri.com.fuse.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kwas7493 on 3/14/2017.
 */

public interface CustomYahooClientService {
    @GET("/geocode?location=")
    Call<List<YahooAddress>> searchYahooAddress(@Query("appid=") String appId, @Query("location") Double... coords);
}
