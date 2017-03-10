package support.esri.com.fuse;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
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
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static support.esri.com.fuse.LogInActivity.fusePortal;

public class MapActivity extends AppCompatActivity {

    private ArcGISMap fuseMap;
    private MapView fuseMapView;
    private LocationDisplay locationDisplay;
    private static Toolbar toolbar;
    private Drawer drawer;

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

        //create the Drawer
        try {
            createMaterialDrawer(savedInstanceState, toolbar, getIntent());
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
            Log.e("URLString ", imageUrl);
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
                .withHeaderBackground(R.drawable.material_drawer_badge)
                .withEmailTypeface(Typeface.SANS_SERIF)
                .withSavedInstance(savedInstanceState)
                .addProfiles(profile)
                .build();

        //create drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withDrawerLayout(R.layout.drawer_layout)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withGenerateMiniDrawer(true)
                .withToolbar(drawerToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Sign Out").withIdentifier(1).withTextColor(Color.BLACK).withIcon(GoogleMaterial.Icon.gmd_sign_in),
                        new PrimaryDrawerItem().withName("Trending").withBadge("22").withTextColor(Color.BLACK).withBadgeStyle(
                                new BadgeStyle(Color.GREEN, Color.blue(20))).withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_trending_up),
                        new PrimaryDrawerItem().withName("Voice").withIdentifier(3).withTextColor(Color.BLACK).withIcon(GoogleMaterial.Icon.gmd_mic),
                        new PrimaryDrawerItem().withName("Search").withIdentifier(4).withTextColor(Color.BLACK).withIcon(GoogleMaterial.Icon.gmd_search),
                        new PrimaryDrawerItem().withName("Featured Content").withIdentifier(5).withTextColor(Color.BLACK).withIcon(GoogleMaterial.Icon.gmd_cloud_download),
                        new SectionDrawerItem().withDivider(true).withName("Customize Map"),
                                new ExpandableBadgeDrawerItem().withName("Change Basemap").withIdentifier(6).withTextColor(Color.BLACK).withSelectable(false)
                                        .withSubItems(
                                                new SecondaryDrawerItem().withName("Satellite").withLevel(2),
                                                new SecondaryDrawerItem().withName("Navigation").withLevel(2),
                                                new SecondaryDrawerItem().withName("Dark Gray").withLevel(2),
                                                new SecondaryDrawerItem().withName("Streets").withLevel(2)
                                                ).withIcon(GoogleMaterial.Icon.gmd_map)
                        )

                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Settings").withIdentifier(7).withTextColor(Color.BLACK).withIcon(GoogleMaterial.Icon.gmd_settings),
                        new SecondaryDrawerItem().withName("Feedback").withIdentifier(8).withTextColor(Color.BLACK).withIcon(GoogleMaterial.Icon.gmd_comment),
                        new SecondaryDrawerItem().withName("About").withIdentifier(9).withTextColor(Color.BLACK).withIcon(GoogleMaterial.Icon.gmd_account))
                .withShowDrawerOnFirstLaunch(false)
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean requestGPSLocation() {
        boolean granted = false;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationDisplay = fuseMapView.getLocationDisplay();
            locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
            locationDisplay.startAsync();
            if (locationDisplay.isStarted()) {
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

}
