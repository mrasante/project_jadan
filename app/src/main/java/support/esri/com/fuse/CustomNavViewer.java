package support.esri.com.fuse;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kwas7493 on 6/12/2017.
 */

public class CustomNavViewer {

    public CustomNavViewer(Context consContext){

    }

    public static void launchBasemapSelector(AppCompatActivity activity){
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        BasemapFragment basemapFragment = BasemapFragment.newInstance("basemapid", "basemapname");
        fragmentTransaction.add(activity.findViewById(R.id.fuse_map_view).getId(), basemapFragment, "BasemapFrag").commit();
    }
}
