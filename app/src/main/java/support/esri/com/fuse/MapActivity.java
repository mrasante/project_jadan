package support.esri.com.fuse;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.security.CredentialChangedEvent;
import com.esri.arcgisruntime.security.CredentialChangedListener;
import com.esri.arcgisruntime.security.UserCredential;
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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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
                .withHeaderBackground(R.color.cardview_dark_background)
                .withSavedInstance(savedInstanceState)
                .addProfiles(profile)
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
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Sign Out").withIdentifier(1).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_sign_in),
                        new PrimaryDrawerItem().withName("Trending").withBadge("22").withTextColor(Color.WHITE).withBadgeStyle(
                                new BadgeStyle(Color.GREEN, Color.blue(20))).withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_trending_up),
                        new PrimaryDrawerItem().withName("Voice").withIdentifier(3).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_mic),
                        new PrimaryDrawerItem().withName("Search").withIdentifier(4).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_search),
                        new PrimaryDrawerItem().withName("Featured Content").withIdentifier(5).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_cloud_download),
                        new SectionDrawerItem().withDivider(true).withName("CUSTOMIZE MAP").withTextColor(Color.GRAY),
                        new ExpandableBadgeDrawerItem().withName("Change Basemap").withTextColor(Color.WHITE).withSelectable(false)
                                .withSubItems(
                                        new SecondaryDrawerItem().withName("Satellite").withLevel(2).withTextColor(Color.WHITE),
                                        new SecondaryDrawerItem().withName("Navigation").withLevel(2).withTextColor(Color.WHITE),
                                        new SecondaryDrawerItem().withName("Dark Gray").withLevel(2).withTextColor(Color.WHITE),
                                        new SecondaryDrawerItem().withName("Streets").withLevel(2).withTextColor(Color.WHITE)
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
                                obtainTrendingTopics();
                                break;
                            /*case 3:
                                activateVoiceGuidance();
                            case 4:
                                performSearch("string");
                            case 5:
                                getArcGISFeaturedContent();
                            case 6:
                                showSettings();
                            case 7:
                                getFeedback();
                            case 8:
                                showAbout();*/
                            default:
                                showMessage("Functionality not yet implemented!");
                        }

                        return false;
                    }
                })
               /* .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Settings").withIdentifier(7).withTextColor(Color.GREEN).withIcon(GoogleMaterial.Icon.gmd_settings),
                        new SecondaryDrawerItem().withName("Feedback").withIdentifier(8).withTextColor(Color.GREEN).withIcon(GoogleMaterial.Icon.gmd_comment),
                        new SecondaryDrawerItem().withName("About").withIdentifier(9).withTextColor(Color.GREEN).withIcon(GoogleMaterial.Icon.gmd_account))*/
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

    private void obtainTrendingTopics() {


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


}
