package support.esri.com.fuse.models;

import android.os.AsyncTask;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.concurrent.ExecutionException;

/**
 * Created by kwas7493 on 5/19/2017.
 */

public class PopUpAsync extends AsyncTask<DataCarrier, Void, Void> {

    @Override
    protected Void doInBackground(DataCarrier... dataCarriers) {
        try {
            Point point = dataCarriers[0].getPoint();
            GraphicsOverlay graphicsOverlay = dataCarriers[0].getGraphicsOverlay();
            MapView mapView = dataCarriers[0].getMapView();
      /*  IdentifyGraphicsOverlayResult identifieds = mapView.identifyGraphicsOverlayAsync(graphicsOverlay, mapView.locationToScreen(point),
                100, true, 5).get();*/
          /*  GeoElement geoElement = identifieds.getPopups().get(0).getGeoElement();
            Callout callout = fuseMapView.getCallout();
            callout.setGeoElement(geoElement, (Point) graphic.getGeometry());
            Callout.ShowOptions showOptions = new Callout.ShowOptions(true, true, true);
//            callout.setStyle(Callout.Style.LeaderPosition.LOWER_LEFT_CORNER);
            callout.setShowOptions(showOptions);
            callout.show();*/
            // } catch (InterruptedException | ExecutionException interExev) {

        } finally {

        }
        return null;
    }
}
