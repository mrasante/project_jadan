package support.esri.com.fuse.models;

import android.content.Context;
import android.os.AsyncTask;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.security.UserCredential;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by kwas7493 on 5/12/2017.
 */

public class AppGeocoder extends AsyncTask<String, Void, List<GeocodeResult>> {
    private static final String GEOCODE_URL = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";
    UserCredential classCredential;
    List<GeocodeResult> geocodeResultList;


    public AppGeocoder(UserCredential userCredential) {
        this.classCredential = userCredential;
    }


    @Override
    public List<GeocodeResult> doInBackground(String... params) {
        try {
            LocatorTask locatorTask = new LocatorTask(GEOCODE_URL);
            GeocodeParameters geocodeParameters = new GeocodeParameters();
            geocodeParameters.setMaxResults(1000);
            locatorTask.setCredential(classCredential);
/*
            Map<String, String> searchesMap = new HashMap<>();
            for (int i = 0; i < params.length; i++) {
                searchesMap.put("Name", params[i]);
            }*/
            geocodeResultList = locatorTask.geocodeAsync(params[0], geocodeParameters).get();

        } catch (ExecutionException | InterruptedException exeInt) {

        }
        return geocodeResultList;
    }


    public List<Point> getGeocodedLocations(List<GeocodeResult> geocodeResultList) {
        List<Point> returnPointValues = new ArrayList<>();
        for (GeocodeResult geocodeResult : geocodeResultList) {
            returnPointValues.add(geocodeResult.getDisplayLocation());
        }
        return returnPointValues;
    }
}
