package support.esri.com.fuse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Bookmark;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by kwas7493 on 6/12/2017.
 */

public class CustomViewHandler {

    public CustomViewHandler(Context consContext) {

    }

    public static void createBookmark(BookmarkFragment bookmarkFragment, MapView mapView) {
            AppCompatActivity activity = (AppCompatActivity) mapView.getContext();
            activity.getSupportFragmentManager().beginTransaction().add(activity.findViewById(R.id.fuse_map_view).getId(), bookmarkFragment, "BookmarkFrag").commit();
    }

    public static void toggleBasemapSelector(AppCompatActivity activity, BasemapFragment basemapFragment) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        if (!basemapFragment.isAdded())
            fragmentTransaction.add(activity.findViewById(R.id.fuse_map_view).getId(), basemapFragment, "BasemapFrag").commit();
        else if (!basemapFragment.isHidden())
            fragmentTransaction.hide(basemapFragment).commit();
        else
            fragmentTransaction.show(basemapFragment).commit();

    }
}
