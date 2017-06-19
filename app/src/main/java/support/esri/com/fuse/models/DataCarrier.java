package support.esri.com.fuse.models;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by kwas7493 on 5/19/2017.
 */

public class DataCarrier {
    GraphicsOverlay graphicsOverlay;
    Point point;
    MapView mapView;

    public DataCarrier(GraphicsOverlay graphicsOver, Point point, MapView mPView) {
        this.graphicsOverlay = graphicsOver;
        this.point = point;
        this.mapView =  mPView;
    }

    public GraphicsOverlay getGraphicsOverlay() {
        return graphicsOverlay;
    }

    public void setGraphicsOverlay(GraphicsOverlay graphicsOverlay) {
        this.graphicsOverlay = graphicsOverlay;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point pt) {
        this.point = pt;
    }

    public void setMapView(MapView mapV){
        this.mapView = mapV;
    }

    public MapView getMapView(){
        return mapView;
    }
}
