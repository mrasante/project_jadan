package support.esri.com.fuse;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.NavigationChangedEvent;
import com.esri.arcgisruntime.mapping.view.NavigationChangedListener;
import com.esri.arcgisruntime.security.UserCredential;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;
import support.esri.com.fuse.models.AppRateLimit;
import support.esri.com.fuse.models.AvailableWoeId;
import support.esri.com.fuse.models.TwitterTrends;
import support.esri.com.fuse.models.YahooWOEIDService;

import static support.esri.com.fuse.LogInActivity.fusePortal;
import static support.esri.com.fuse.LogInActivity.twitterSession;

public class MapActivity extends AppCompatActivity {

    private ArcGISMap fuseMap;
    private MapView fuseMapView;
    private LocationDisplay locationDisplay;
    private static Toolbar toolbar;
    private Drawer drawer;
    private Envelope envelope;
    private String woeid;
    private Bundle globalInstanceState;
    private double lat;
    private double longi;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create and add the map with basemap
        fuseMapView = (MapView) findViewById(R.id.fuse_map_view);
        fuseMap = new ArcGISMap(Basemap.createStreetsNightVector());
        requestGPSLocation();
        fuseMapView.setMap(fuseMap);


        //welcome the user
        if (fusePortal != null && getIntent().getStringExtra("Authenticated").equalsIgnoreCase("Authenticated")) {
            Snackbar.make(fuseMapView, "Welcome " + fusePortal.getUser().getFullName(), Snackbar.LENGTH_LONG).show();
        }
        globalInstanceState = savedInstanceState;

        //register panning for yahoo woeid7
        fuseMapView.addNavigationChangedListener(new NavigationChangedListener() {
            @Override
            public void navigationChanged(final NavigationChangedEvent navigationChangedEvent) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        retrieveTwitterWoeidOnPan(navigationChangedEvent);
                    }
                }).start();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart(){
        super.onStart();
        //create the Drawer
        try {
            createMaterialDrawer(globalInstanceState, toolbar, getIntent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Use this this created the navigation drawer. Uses material drawer by Mike Penz
     *
     * @param savedInstanceState
     * @param drawerToolbar
     * @param localIntent
     * @throws Exception
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createMaterialDrawer(Bundle savedInstanceState, Toolbar drawerToolbar, Intent localIntent) throws Exception {
        IProfile profile = null;
        Bitmap userImage = null;
        String userFullName = null;
        //retrieve who sent you extra from intent.
        String whoSentYou = getIntent().getStringExtra("whoSentYou");
        if (whoSentYou.equalsIgnoreCase("arcgis.com")) {
            userImage = new PortalNetworkAsyncTask().execute().get();
            userFullName = fusePortal.getUser().getFullName();
        } else if (whoSentYou.equalsIgnoreCase("Twitter")) {
            String imageUrl = localIntent.getStringExtra("twitterProfileUrl");
            userFullName = localIntent.getStringExtra("twitterUsername");
            userImage = new BitmapViaNetwork().execute(new URL(imageUrl)).get();
        } else if (whoSentYou.equalsIgnoreCase("Facebook")) {
            String imageUrl = localIntent.getStringExtra("facebookProfileUrl");
            userFullName = localIntent.getStringExtra("facebookUsername");
            userImage = new BitmapViaNetwork().execute(new URL(imageUrl)).get();
        }

        //craete and populate the drawer
        if (userFullName != null && userImage != null) {
            profile = new ProfileDrawerItem().withName(userFullName).withIcon(userImage);
        }

        AccountHeader headerResult = new AccountHeaderBuilder().withActivity(this)
                .withHeaderBackground(R.color.cardview_dark_background)
                .withSavedInstance(savedInstanceState)
                .addProfiles(profile,
                        new ProfileSettingDrawerItem().withName("Add Account").withIcon(GoogleMaterial.Icon.gmd_plus)
                                .withIdentifier(1)
                                .withTextColor(Color.WHITE)
                                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        AppDialogBuilder.showAccountOption(MapActivity.this);
                                        return false;
                                    }
                                }))
                .build();


        //create drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withSliderBackgroundColor(getResources().getColor(R.color.material_drawer_dark_background, null))
                .withDrawerLayout(R.layout.drawer_layout)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withGenerateMiniDrawer(true)
                .withToolbar(drawerToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems( //String.valueOf(new TrendingRetriever().execute().get().intValue())
                        new PrimaryDrawerItem().withName("Sign Out").withIdentifier(1).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_sign_in),
                        new PrimaryDrawerItem().withName("Trending").withBadge("50").withTextColor(Color.WHITE).withBadgeStyle(
                                new BadgeStyle(Color.GREEN, Color.blue(20))).withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_trending_up),
                        new PrimaryDrawerItem().withName("Voice").withIdentifier(3).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_mic),
                        new PrimaryDrawerItem().withName("Search").withIdentifier(4).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_search),
                        new PrimaryDrawerItem().withName("Featured Content").withIdentifier(5).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_cloud_download),
                        new SectionDrawerItem().withDivider(true).withName("CUSTOMIZE MAP").withTextColor(Color.GRAY),
                        new ExpandableBadgeDrawerItem().withName("Change Basemap").withTextColor(Color.WHITE).withSelectable(false)
                                .withSubItems(
                                        new SecondaryDrawerItem().withName("Satellite").withIdentifier(10).withLevel(2).withTextColor(Color.WHITE),
                                        new SecondaryDrawerItem().withName("Navigation").withIdentifier(11).withLevel(2).withTextColor(Color.WHITE),
                                        new SecondaryDrawerItem().withName("Dark Gray").withIdentifier(12).withLevel(2).withTextColor(Color.WHITE),
                                        new SecondaryDrawerItem().withName("Streets").withIdentifier(13).withLevel(2).withTextColor(Color.WHITE)
                                ).withIcon(GoogleMaterial.Icon.gmd_map),
                        new SectionDrawerItem().withDivider(true).withName("INFORMATION").withTextColor(Color.GRAY),
                        new SecondaryDrawerItem().withName("Settings").withIdentifier(6).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_settings),
                        new SecondaryDrawerItem().withName("Feedback").withIdentifier(7).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_comment),
                        new SecondaryDrawerItem().withName("About").withIdentifier(8).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_account))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                signoutAccount();
                                break;
                            case 2:

                                break;
                            /*case 3:
                                activateVoiceGuidance();
                            case 4:
                                performSearch("string");
                            case 5:`
                                getArcGISFeaturedContent();
                            case 6:
                                showSettings();
                            case 7:
                                getFeedback();
                            case 8:
                                showAbout();*/
                        }
                        if (drawerItem.getIdentifier() > 9 && drawerItem.getIdentifier() < 14) {
                            final String basemapName = ((SecondaryDrawerItem) drawerItem).getName().getText();
                            fuseMap.setBasemap(BasemapChanger.changeBasemapTo(basemapName));
                            fuseMap.addBasemapChangedListener(new ArcGISMap.BasemapChangedListener() {
                                @Override
                                public void basemapChanged(ArcGISMap.BasemapChangedEvent basemapChangedEvent) {
                                    Snackbar.make(fuseMapView, "Basemap changed to " + fuseMap.getBasemap().getName(), Snackbar.LENGTH_LONG).show();
                                }
                            });

                        }
                        return false;
                    }
                })
                .withShowDrawerOnFirstLaunch(true)
                .build();

        final CrossfadeDrawerLayout crossfadeDrawerLayout = (CrossfadeDrawerLayout) drawer.getDrawerLayout();
        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = drawer.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background,
                com.mikepenz.materialdrawer.R.color.material_drawer_dark_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    drawer.getDrawerLayout().closeDrawer(GravityCompat.START);
                }

            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
    }


    public Integer getKeyFromValue(TreeMap<Integer, Integer> map, Integer val){
        for(Integer value : map.keySet()){
            if(map.get(value) == val){
                return value;
            }
        }
        return Integer.valueOf("1");
    }

    private Integer getAppRateLimit(AppRateLimit appRateLimit){
        return appRateLimit.getResources().getSearch().getSearchTweets().getLimit();
    }



    private void obtainTrendingTopics(TwitterSession twitterSession, Long yahooWoeId) {
        TwitterTrends trend = null;
        try {
            MyTwitterAPIClientExtender myTwitterApiClient = new MyTwitterAPIClientExtender(twitterSession);
            Integer rateLimit = getAppRateLimit(myTwitterApiClient.getCustomTwitterService().getRateLimit().execute().body());

            if(rateLimit < 0){
                Log.e("RateLimit ", "Rate limit has been reached!!");
                return;
            }

            TreeMap<Integer, Integer> woeidMap = getIntegerTreeMap(yahooWoeId, myTwitterApiClient);
            if (woeidMap == null) return;

            Integer matchedKey = getKeyFromValue(woeidMap, Collections.min(woeidMap.values()));
            Log.e("Smallest Value: ", ""+Collections.min(woeidMap.values()));
            Long idToUse = Long.valueOf(matchedKey);
            Log.e("Im using ID: ", ""+idToUse);
            Response<List<TwitterTrends>> trendResponse = myTwitterApiClient.getCustomTwitterService().show(idToUse).execute();
            if(trendResponse.body() == null){
                return;
            }
            trend = trendResponse.body().get(0);

        } catch (IOException ioException) {
            Log.e("IOException ", ioException.getMessage());
        }
        Log.e(this.getClass().getName()+ " Check trend ", "" + trend.getTrends().get(0).getName());
    }



    @Nullable
    private TreeMap<Integer, Integer> getIntegerTreeMap(Long yahooWoeId, MyTwitterAPIClientExtender myTwitterApiClient) throws IOException {
        Response<List<AvailableWoeId>> availableWoeId = myTwitterApiClient.getCustomTwitterService().getAvailableWoeid().execute();
        List<AvailableWoeId> availableWoeIdList = availableWoeId.body();
        if(availableWoeIdList == null){
            return null;
        }


        Integer yahooWoeInteger = Integer.parseInt(String.valueOf(yahooWoeId));
        TreeMap<Integer, Integer> woeidMap = new TreeMap<>();
        for(AvailableWoeId avaWoeid : availableWoeIdList){
            if(avaWoeid.getWoeid() != 1){
                woeidMap.put(avaWoeid.getWoeid(), Math.abs(avaWoeid.getWoeid() - yahooWoeInteger));
            }
        }
        return woeidMap;
    }


    private void signoutAccount() {
        final String whoSendYou = getIntent().getStringExtra("whoSentYou");
        Log.e("whoSentYou", whoSendYou);
        switch (whoSendYou) {

            case "arcgis.com":
                new LoggOutAsyncTask().onPreExecute();
                break;
            default:
                showMessage("Log out attempt");

        }
    }


    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean requestGPSLocation() {
        boolean granted = false;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationDisplay = fuseMapView.getLocationDisplay();
            locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
            locationDisplay.startAsync();
            if (locationDisplay.isStarted() && locationDisplay.getLocation().getPosition() != null) {
                fuseMap.setInitialViewpoint(new Viewpoint(locationDisplay.getLocation().getPosition(), 7));
                granted = true;
            }
        } else {
            String[] arrayPerms = {Manifest.permission.ACCESS_FINE_LOCATION};
            int[] grantedPerms = {PackageManager.PERMISSION_GRANTED};
            requestPermissions(arrayPerms, 0);
            onRequestPermissionsResult(0, arrayPerms, grantedPerms);
        }

        return granted;
    }


    @Override
    public void onRequestPermissionsResult(int value, String[] permArray, int[] grantedPerms) {
        if (grantedPerms[0] == PackageManager.PERMISSION_GRANTED) {
            locationDisplay = fuseMapView.getLocationDisplay();
            locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
            locationDisplay.startAsync();
        }

    }

    private class PortalNetworkAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        public Bitmap doInBackground(Void... portals) {
            Bitmap bitmap = null;
            if (fusePortal.getLoadStatus() == LoadStatus.LOADED) {
                try {
                    byte[] thumbnailByte = fusePortal.getUser().fetchThumbnailAsync().get();
                    bitmap = BitmapFactory.decodeByteArray(thumbnailByte, 0, thumbnailByte.length);
                } catch (ExecutionException | InterruptedException exInt) {

                }
            }
            return bitmap;
        }
    }

    private class BitmapViaNetwork extends AsyncTask<URL, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap bitmap = null;
            HttpURLConnection httpURLConnection = null;
            InputStream is = null;
            try {
                httpURLConnection = (HttpURLConnection) urls[0].openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null && is != null) {
                    httpURLConnection.disconnect();
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }
    }

    private class LoggOutAsyncTask extends AsyncTask<Void, Void, Void> {

        //execute only to log out the account
        @Override
        protected Void doInBackground(Void... voids) {
            fusePortal.setCredential(new UserCredential("x", "x"));
            fusePortal.loadAsync();
            fusePortal.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                    intent.putExtra("loggedOut", "loggedOut");
                    startActivity(intent);
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
            alertDialogBuilder.setMessage("Are you sure you want to sign out of selected account?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doInBackground();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alertDialogBuilder.create();
            alertDialogBuilder.show();

        }


    }


    private String retrieveTwitterWoeidOnPan(NavigationChangedEvent navChangeEvent) {
        Log.e("Threading....", "Entering thread");
        try {
            boolean isNavigating = navChangeEvent.isNavigating() ? true : false;

            if (!isNavigating) {
                Point point = new Point(fuseMapView.getVisibleArea().getExtent().getCenter().getX(),
                        fuseMapView.getVisibleArea().getExtent().getCenter().getY(), fuseMap.getSpatialReference());
               Point projectedPoint = (Point)GeometryEngine.project(point, SpatialReference.create(4326));
                lat = projectedPoint.getX();
                longi = projectedPoint.getY();
                Log.e("Are we Naving: ", isNavigating+" " + longi);
            } else
                return null;
            Double[] coords = {lat, longi};
            String woeidString = new YahooWOEIDService().execute(coords).get();
            Log.e("Using WoeID: ", woeidString);
            if(woeidString.length() == 0){
                return "55988306";
            }
            Long woeidVal = woeidString != null ? Long.parseLong(woeidString) : Long.parseLong("55988306");
            obtainTrendingTopics(twitterSession, woeidVal);
        }catch (ExecutionException | InterruptedException ioe){
        Log.e(this.getClass().getName()+" Longi ", ioe.getMessage());
    }
            String value = woeid != null ? woeid : null;

            return value;

    }

}
