package support.esri.com.fuse.models;

import android.os.AsyncTask;
import android.util.Log;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;

import support.esri.com.fuse.MapActivity;

/**
 * Created by kwas7493 on 3/14/2017.
 */

public class YahooWOEIDService extends AsyncTask<Double, Void, String>{

    @Override
    protected String doInBackground(Double[] coords) {
        String woeid = null;

        try {
            double  Latitude = coords[0];
            double Longitude = coords[1];
            Log.e("CoordVals: ", Latitude + " "+ Longitude);
            if(Latitude == 0.0 && Longitude == 0.0){
                return "55988306";
            }
//            String YAHOO_API_URL = "https://query.yahooapis.com/v1/public/yql?q=select woeid from geo.places where text=("+Longitude+","+Latitude+")";
            String YAHOO_API_URL = "https://query.yahooapis.com/v1/public/yql?q=select woeid from geo.places where text=\"(" + Longitude +"," +Latitude +")\" limit 1&diagnostics=false";
            Document document = Jsoup.connect(YAHOO_API_URL).timeout(10 * 1000).ignoreHttpErrors(true).ignoreContentType(true).get();
            Log.e("WOEIDExcpetion ", "Connected");
            Document doc = Jsoup.parse(document.html(), "", Parser.xmlParser());
            woeid = doc.select("woeid").html();
            Log.e("TestWOe ", woeid);
        }catch(IOException ioException){
            Log.e("WOEIDExcpetion ", ioException.getMessage());
        }

        if(woeid !=null){
            return woeid;
        }
        return null;
    }
}
