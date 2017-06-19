package support.esri.com.fuse;

import android.app.Activity;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by kwas7493 on 6/12/2017.
 */

public class AppUtility extends Activity {
    
    public void clearMap(MapView mapView){
        mapView.getGraphicsOverlays().clear();
    }
}
