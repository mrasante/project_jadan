package support.esri.com.fuse;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.PortalUser;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.concurrent.ExecutionException;

import static support.esri.com.fuse.LogInActivity.fusePortal;

public class MapActivity extends AppCompatActivity {

    private ArcGISMap fuseMap;
    private MapView fuseMapView;
    private LocationDisplay locationDisplay;
    private static Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create and add the map with basemap
        fuseMapView = (MapView)findViewById(R.id.fuse_map_view);
        fuseMap = new ArcGISMap(Basemap.createStreetsNightVector());
        requestGPSLocation();
        fuseMapView.setMap(fuseMap);

        //welcome the user
        if(fusePortal != null && getIntent().getStringExtra("Authenticated").equalsIgnoreCase("Authenticated")){
            Snackbar.make(fuseMapView, "Welcome "+ fusePortal.getUser().getFullName(), Snackbar.LENGTH_LONG).show();
        }

        //create the Drawer
        try {
            createMaterialDrawer(fusePortal.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMaterialDrawer(PortalUser portalUser) throws Exception{
        IProfile profile = null;

        //if the user logged in with facebook or twitter account for the respective profile.


        //use the below for arcgis.com details
        Bitmap userImage = new PortalNetworkAsyncTask().execute().get();
        String userFullName = fusePortal.getUser().getFullName();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean requestGPSLocation(){
        boolean granted = false;
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            locationDisplay = fuseMapView.getLocationDisplay();
            locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
            locationDisplay.startAsync();
            if(locationDisplay.isStarted()){
                fuseMap.setInitialViewpoint(new Viewpoint(locationDisplay.getLocation().getPosition(), 7));
                granted = true;
            }
        }else {
            String [] arrayPerms = {Manifest.permission.ACCESS_FINE_LOCATION};
            int [] grantedPerms = {PackageManager.PERMISSION_GRANTED};
            requestPermissions(arrayPerms, 0);
            onRequestPermissionsResult(0, arrayPerms, grantedPerms);
        }

        return granted;
    }


    @Override
    public void onRequestPermissionsResult(int value, String[] permArray, int[] grantedPerms){
        if(grantedPerms[0] == PackageManager.PERMISSION_GRANTED){
            locationDisplay = fuseMapView.getLocationDisplay();
            locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
            locationDisplay.startAsync();
        }

    }

    private class PortalNetworkAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        public Bitmap doInBackground(Void... portals){
            Bitmap bitmap = null;
            if(fusePortal.getLoadStatus() == LoadStatus.LOADED){
                try{
                    byte[] thumbnailByte = fusePortal.getPortalInfo().fetchPortalThumbnailAsync().get();
                    bitmap = BitmapFactory.decodeByteArray(thumbnailByte, 0, thumbnailByte.length);
                }catch(ExecutionException|InterruptedException exInt){

                }
            }
            return bitmap;
        }

    }

}
