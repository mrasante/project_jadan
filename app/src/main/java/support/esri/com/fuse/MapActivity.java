package support.esri.com.fuse;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.BookmarkList;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.popup.Popup;
import com.esri.arcgisruntime.mapping.popup.PopupDefinition;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.NavigationChangedEvent;
import com.esri.arcgisruntime.mapping.view.NavigationChangedListener;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.facebook.login.LoginManager;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
//import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
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
import com.twitter.sdk.android.Twitter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;
import support.esri.com.fuse.models.AppRateLimit;
import support.esri.com.fuse.models.ClosestWoeId;
import support.esri.com.fuse.models.FastAdapterItemImpl;
import support.esri.com.fuse.models.RecentSearchSuggestionProvider;
import support.esri.com.fuse.models.Trend;
import support.esri.com.fuse.models.TwitterTrends;

import static support.esri.com.fuse.LogInActivity.fusePortal;
import static support.esri.com.fuse.LogInActivity.sessionKeeper;
import static support.esri.com.fuse.LogInActivity.tracker;
import static support.esri.com.fuse.LogInActivity.twitterSession;

public class MapActivity extends AppCompatActivity implements BasemapFragment.OnFragmentInteractionListener,
        BookmarkFragment.OnFragmentInteractionListener {

    private static final String SYMBOL_URL = "http://static.arcgis.com/images/Symbols/Basic/RedStickpin.png";
    private static Toolbar toolbar;
    private ArcGISMap fuseMap;
    private MapView fuseMapView;
    private LocationDisplay locationDisplay;
    private Drawer drawer;
    private Envelope envelope;
    private String woeid;
    private Bundle globalInstanceState;
    private double lat;
    private double longi;
    private TwitterTrends twitterTrends;
    private List<Trend> trendList;
    private FuseBottomSheetDialog fuseBottomSheetDialog;
    private FloatingActionButton fabIcon;
    private NavigationChangedListener navigationChangedListener;
    private Viewpoint viewPoint;
    private boolean trending;
    private SeekBar seekBar;
    private Point point;
    private Graphic graphic;
    private FloatingActionButton basemapFab;
    private PopupDefinition popupDefinition;
    private static AppCompatActivity currentActivity = null;
    private GraphicsOverlay graphicsOverlay;
    private MyTwitterAPIClientExtender myTwitterApiClient;
    private FloatingActionButton bookmarkFab;
    private FloatingActionButton bookmarkFab_plus;
    private FloatingActionButton bookmarkFab_remove;
    private Intent startedIntent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        currentActivity = this;
        startedIntent = getIntent();
        setSupportActionBar(toolbar);
        setTitle("Social GIS");
        //handleSearchIntent(getIntent());
        //create and add the map with basemap
        fuseMapView = (MapView) findViewById(R.id.fuse_map_view);
        fuseMap = new ArcGISMap(Basemap.createStreetsNightVector());
        requestGPSLocation();
        fuseMapView.setMap(fuseMap);
        bookmarkFab_plus = (FloatingActionButton) findViewById(R.id.bookmark_fab_plus);
        bookmarkFab_remove = (FloatingActionButton) findViewById(R.id.bookmark_fab_remove);
        bookmarkFab_plus.setVisibility(View.INVISIBLE);
        bookmarkFab_remove.setVisibility(View.INVISIBLE);

        basemapFab = (FloatingActionButton) findViewById(R.id.basemap_action_button);
        final BasemapFragment basemapFragment = new BasemapFragment();
        basemapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHandler.toggleBasemapSelector(currentActivity, basemapFragment);
            }
        });


        //welcome the user
        if (fusePortal != null && getIntent().getStringExtra("Authenticated").equalsIgnoreCase("Authenticated")) {
            Snackbar.make(fuseMapView, "Welcome " + fusePortal.getUser().getFullName(), Snackbar.LENGTH_LONG).show();
        }
        globalInstanceState = savedInstanceState;

        //create a nav changed listener and add the logic
        navigationChangedListener =
                new NavigationChangedListener() {
                    @Override
                    public void navigationChanged(final NavigationChangedEvent navigationChangedEvent) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                retrieveTwitterWoeidOnPan(navigationChangedEvent);
                            }
                        }).start();
                    }
                };

        fabIcon = (FloatingActionButton) findViewById(R.id.fab);
        fabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panToCurrentLocation();
            }
        });

        bookmarkFab = (FloatingActionButton) findViewById(R.id.bookmark_fab);
        bookmarkFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookmarkFabVisibility();
            }
        });

        bookmarkFab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialogBuilder.showBookmarkAlert((Activity) fuseMapView.getContext());
            }
        });

        bookmarkFab_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder removeAlert = new AlertDialog.Builder(MapActivity.this);
                removeAlert.setMessage("All bookmarks will be permanently removed. \n Do you want to proceed?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BookmarkList bookmarks = fuseMapView.getMap().getBookmarks();
                                if(bookmarks != null){
                                    bookmarks.clear();
                                    Toast.makeText(v.getContext(), "All bookmarks removed", Toast.LENGTH_LONG).show();
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    List<Fragment> listOfFragments = fragmentManager.getFragments();
                                    for (Fragment fragment : listOfFragments) {
                                        fragmentManager.beginTransaction().hide(fragment).commit();
                                    }
                                }else
                                    return;

                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "User cancelled bookmark clearing.", Toast.LENGTH_LONG).show();
                            }
                        }).show();
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {
//        setIntent(intent);
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions recentSuggestions = new SearchRecentSuggestions(this,
                    RecentSearchSuggestionProvider.AUTHORITY, RecentSearchSuggestionProvider.MODE);
            recentSuggestions.saveRecentQuery(searchQuery, null);



        }
    }

    private void toggleBookmarkFabVisibility() {
        if (bookmarkFab_plus.getVisibility() != View.VISIBLE && bookmarkFab_remove.getVisibility() != View.VISIBLE) {
            bookmarkFab_plus.setVisibility(View.VISIBLE);
            bookmarkFab_remove.setVisibility(View.VISIBLE);

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(bookmarkFab_plus, "translationY", 150f);
            objectAnimator.setDuration(1000);
            objectAnimator.start();

            ObjectAnimator removeBookmarkFabAnimator = ObjectAnimator.ofFloat(bookmarkFab_remove, "translationY", 300f);
            removeBookmarkFabAnimator.setDuration(1000);
            removeBookmarkFabAnimator.start();
        } else {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(bookmarkFab_plus, "translationY", -150f);
            objectAnimator.setDuration(1000);
            objectAnimator.start();

            ObjectAnimator removeBookmarkFabAnimator = ObjectAnimator.ofFloat(bookmarkFab_remove, "translationY", -300f);
            removeBookmarkFabAnimator.setDuration(1000);
            removeBookmarkFabAnimator.start();
            bookmarkFab_plus.setVisibility(View.INVISIBLE);
            bookmarkFab_remove.setVisibility(View.INVISIBLE);
        }

    }

    private double[] getCoordsArray(Envelope extent) {
        return new double[]{extent.getXMin(), extent.getYMin(), extent.getXMax(), extent.getYMax()};
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        try {
            createMaterialDrawer(globalInstanceState, toolbar, getIntent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createMaterialDrawer(Bundle savedInstanceState, Toolbar drawerToolbar, Intent localIntent) throws Exception {
        IProfile profile = null;
        Bitmap userImage = null;
        String userFullName = null;

        //retrieve who sent you extra from intent.
        String whoSentYou = AppPreferences.getSharedPreferences().getString("whoSentYou", "arcgis.com");
        if (whoSentYou.equalsIgnoreCase("arcgis.com")) {
            userImage = new PortalNetworkAsyncTask().execute().get();
            userFullName = fusePortal.getUser().getFullName();
        } else if (whoSentYou.equalsIgnoreCase("Twitter")) {
//            String imageUrl = localIntent.getStringExtra("twitterProfileUrl");
            String imageUrl = AppPreferences.getSharedPreferences().getString("twitterProfileUrl", "TestURL");
//            userFullName = localIntent.getStringExtra("twitterUsername");
            userFullName = AppPreferences.getSharedPreferences().getString("twitterUsername", "TestUsername");
            userImage = new BitmapViaNetworkAsync().execute(new URL(imageUrl)).get();
        } else if (whoSentYou.equalsIgnoreCase("Facebook")) {
            String imageUrl = localIntent.getStringExtra("facebookProfileUrl");
            userFullName = localIntent.getStringExtra("facebookUsername");
            userImage = new BitmapViaNetworkAsync().execute(new URL(imageUrl)).get();
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
//                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withGenerateMiniDrawer(true)
                .withToolbar(drawerToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems( //String.valueOf(new TrendingRetriever().execute().get().intValue())
                        new PrimaryDrawerItem().withName("Sign Out").withIdentifier(1).withTextColor(Color.WHITE).withIcon(GoogleMaterial.Icon.gmd_sign_in),
                        new PrimaryDrawerItem().withName("Toggle Trending").withTextColor(Color.WHITE).withBadgeStyle(
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
                                toggleTrending();
                                break;
                           /* case 3:
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
                        }
                        if (drawerItem.getIdentifier() > 9 && drawerItem.getIdentifier() < 14) {
                            final String basemapName = ((SecondaryDrawerItem) drawerItem).getName().getText().toString();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private boolean toggleTrending() {
        if (trending) {
            fuseMapView.removeNavigationChangedListener(navigationChangedListener);
            trending = false;
            Snackbar.make(getCurrentFocus(), "Trending deactivated", Snackbar.LENGTH_LONG).show();
            return trending;
        } else {
            fuseMapView.addNavigationChangedListener(navigationChangedListener);
            trending = true;
            Snackbar.make(getCurrentFocus(), "Trending activated", Snackbar.LENGTH_LONG).show();
        }
        return trending;
    }

    private Integer getAppRateLimit(AppRateLimit appRateLimit) {
        return appRateLimit.getResources().getSearch().getSearchTweets().getLimit();
    }

    private void obtainTrendingTopics(MyTwitterAPIClientExtender myTwitterApiClient, Long yahooWoeId) {
        try {
            Integer rateLimit = getAppRateLimit(myTwitterApiClient.getCustomTwitterService().getRateLimit().execute().body());
            if (rateLimit < 0) {
                Snackbar.make(fuseMapView, "Twitter rate limit has been reached!!", Snackbar.LENGTH_LONG).show();
                return;
            }
            Response<List<TwitterTrends>> trendResponse = myTwitterApiClient.getCustomTwitterService().show(yahooWoeId).execute();
            if (trendResponse.body() == null) {
                return;
            }
            twitterTrends = trendResponse.body().get(0);
            trendList = twitterTrends.getTrends();
        } catch (IOException ioException) {
            Log.e("IOException ", ioException.getMessage());
        }
        showTrendingUI();
    }


    /**
     * Use this method to show the results of the trending query on the UI
     */

    private void showTrendingUI() {
        if (fuseBottomSheetDialog != null && fuseBottomSheetDialog.isShowing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fuseBottomSheetDialog = new FuseBottomSheetDialog(MapActivity.this);
                RecyclerView recyclerView = (RecyclerView) fuseBottomSheetDialog.inflatedView.findViewById(R.id.fuse_recycler_sheet);
                FastItemAdapter<FastAdapterItemImpl> fastItemAdapter = new FastItemAdapter<>();
                List<FastAdapterItemImpl> fastAdapterItemList = new ArrayList<>();
                for (Trend trend : trendList) {
                    if (fastAdapterItemList.size() <= 4) {
                        FastAdapterItemImpl fastAdapterItem = new FastAdapterItemImpl();
                        fastAdapterItem.name = trend.getName();
                        fastAdapterItem.locations = twitterTrends.getLocations();
                        fastAdapterItemList.add(fastAdapterItem);
                    } else
                        break;
                }
                fastItemAdapter.add(fastAdapterItemList);
                recyclerView.setAdapter(fastItemAdapter);
                fastItemAdapter.withSelectable(true);
                /*fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<FastAdapterItemImpl>() {
                    @Override
                    public boolean onClick(View v, IAdapter<FastAdapterItemImpl> adapter, FastAdapterItemImpl item, int position) {
                        try {
                            AppGeocoder appGeocoder = new AppGeocoder(new UserCredential("mrasante1", "apple@4GONES"));
                            List<Point> pointList = appGeocoder.getGeocodedLocations(appGeocoder.execute(item.locations.get(0).getName()).get());
                            plotPoints(pointList);
                        } catch (InterruptedException | ExecutionException interExec) {
                            Log.e("Geocoder Error: ", "Error: " + interExec.getMessage());
                        }
                        return true;
                    }
                });
*/
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                fuseBottomSheetDialog.show();
            }
        });
    }


    private void plotPoints(final List<Point> pointList) {
        graphicsOverlay = new GraphicsOverlay(GraphicsOverlay.RenderingMode.DYNAMIC);
        //clear mapview of all graphics overlay
        if (fuseMapView.getGraphicsOverlays().size() != 0) {
            fuseMapView.getGraphicsOverlays().clear();
        }

        final PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(SYMBOL_URL);
        pictureMarkerSymbol.setHeight(50);
        pictureMarkerSymbol.setWidth(50);
        pictureMarkerSymbol.loadAsync();
        pictureMarkerSymbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (pictureMarkerSymbol.getLoadStatus() == LoadStatus.LOADED) {
                    for (Point plotPoint : pointList) {
                        point = (Point) GeometryEngine.project(plotPoint, fuseMap.getSpatialReference());
                        graphic = new Graphic(point);
                        graphic.setSymbol(pictureMarkerSymbol);
                        popupDefinition = new PopupDefinition(graphic);
                        graphicsOverlay.getGraphics().add(graphic);
                        graphicsOverlay.setPopupDefinition(popupDefinition);
                    }
                    fuseMapView.getGraphicsOverlays().add(graphicsOverlay);
                    fuseMapView.setViewpointGeometryAsync(graphicsOverlay.getExtent());
                    try {
                        graphicsOverlay.setPopupEnabled(true);
                        IdentifyGraphicsOverlayResult identifieds = fuseMapView.identifyGraphicsOverlayAsync(graphicsOverlay,
                                fuseMapView.locationToScreen(point), 100, true, 5).get();
                        List<Popup> popup = identifieds.getPopups();
                        if (popup.size() == 0)
                            return;
                        GeoElement geoElement = popup.get(0).getGeoElement();
                        Callout callout = fuseMapView.getCallout();
                        callout.setGeoElement(geoElement, (Point) graphic.getGeometry());
                        Callout.ShowOptions showOptions = new Callout.ShowOptions(true, true, true);
                        callout.setShowOptions(showOptions);
                        RelativeLayout callout_layout = (RelativeLayout) LayoutInflater.from(
                                getApplicationContext()).inflate(R.layout.callout_layout, null);
                        callout.setContent(callout_layout);
                        callout.show();
                    } catch (InterruptedException | ExecutionException interExev) {

                    }

                }
            }
        });

    }

    private void signoutAccount() {
        final String whoSendYou = AppPreferences.getSharedPreferences().getString("whoSentYou", "None");
//        Log.e("whoSentYou", AppPreferences.getSharedPreferences().getString("whoSentYou", null));
        switch (whoSendYou) {
            case "arcgis.com":
                new LoggOutAsyncTask().onPreExecute();
                break;
            case "Twitter":
                new LoggOutAsyncTask().onPreExecute();
                break;
            case "Facebook":
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
            panToCurrentLocation();
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


    private void panToCurrentLocation() {
        locationDisplay = fuseMapView.getLocationDisplay();
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
        locationDisplay.startAsync();
    }


    @Override
    public void onRequestPermissionsResult(int value, String[] permArray, int[] grantedPerms) {
        if (grantedPerms[0] == PackageManager.PERMISSION_GRANTED) {
            panToCurrentLocation();
        }

    }

    private String retrieveTwitterWoeidOnPan(NavigationChangedEvent navChangeEvent) {
        try {
            boolean isNavigating = navChangeEvent.isNavigating() ? true : false;
            if (!isNavigating) {
                Point point = new Point(fuseMapView.getVisibleArea().getExtent().getCenter().getX(),
                        fuseMapView.getVisibleArea().getExtent().getCenter().getY(), fuseMap.getSpatialReference());
                Point projectedPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326));
                lat = projectedPoint.getX();
                longi = projectedPoint.getY();
            } else
                return null;
            Double[] coords = {lat, longi};
            myTwitterApiClient = new MyTwitterAPIClientExtender(twitterSession);
            Response<List<ClosestWoeId>> responseRoot = myTwitterApiClient.getCustomTwitterService().getClosestWoeId(coords[0], coords[1]).execute();
            if (responseRoot == null)
                return null;
            Long woeidVal = responseRoot.body().get(0).getWoeid();
            obtainTrendingTopics(myTwitterApiClient, woeidVal);
        } catch (IOException ioe) {
            Log.e(this.getClass().getName() + " Longi ", ioe.getMessage());
        }
        String value = woeid != null ? woeid : null;
        return value;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    private class BitmapViaNetworkAsync extends AsyncTask<URL, Void, Bitmap> {
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
            if (fusePortal != null) {
                logOut();
            }

            if (twitterSession != null) {
                Twitter.logOut();
                logOut();
            }

            if (tracker != null) {
                tracker.stopTracking();
                LoginManager.getInstance().logOut();
                logOut();
            }
            return null;
        }

        private void logOut() {
            twitterSession = null;
            fusePortal = null;
            sessionKeeper.clearSharedPreferences();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.putExtra("loggedOut", "loggedOut");
            startActivity(intent);
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