package support.esri.com.fuse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MapActivity extends AppCompatActivity {

    private ArcGISMap arcgisMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create and add the map with basemap
        MapView fuseMapView = (MapView)findViewById(R.id.fuse_map_view);
        arcgisMap = new ArcGISMap(Basemap.Type.STREETS_NIGHT_VECTOR, -82.954, 42.298, 9);
        fuseMapView.setMap(arcgisMap);
    }

}
