package support.esri.com.fuse;

import com.esri.arcgisruntime.mapping.Basemap;

/**
 * Created by kwas7493 on 3/14/2017.
 */

public class BasemapChanger {
    /*String nameOfBasemap;

    public BasemapChanger(String basemapName){
        nameOfBasemap = basemapName;
    }*/

    public static Basemap changeBasemapTo(String nameOfBasemap) {
        switch (nameOfBasemap) {
            case "Satellite":
                return Basemap.createImageryWithLabelsVector();

            case "Navigation":
                return Basemap.createNavigationVector();

            case "Dark Gray":
                return Basemap.createDarkGrayCanvasVector();

            case "Streets":
                return Basemap.createStreetsNightVector();

            default:
                return Basemap.createDarkGrayCanvasVector();
        }

    }

}
