package support.esri.com.fuse;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kwas7493 on 6/17/2017.
 */

class BasemapChangerAdapter extends RecyclerView.Adapter<BasemapChangerAdapter.ViewHolder> {

    private View inflatedView;
    private Map<String, Bitmap> adapterContent;
    private ArrayList<String> contentKeys;
    private FragmentManager fragmentManager;

    public BasemapChangerAdapter(Map<String, Bitmap> constContent, ArrayList<String> constContentKey, FragmentManager fragmentManager) {
        this.adapterContent = constContent;
        this.contentKeys = constContentKey;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basemap_recycler_content, null);
        ViewHolder viewHolder = new ViewHolder(inflatedView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(contentKeys.get(position));
        holder.cardView.setImageBitmap(adapterContent.get(contentKeys.get(position)));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performBasemapSelection(position);
            }
        });
    }

    private void performBasemapSelection(int position) {
        Activity activity = (Activity) inflatedView.getContext();
        MapView mapView = (MapView) activity.findViewById(R.id.fuse_map_view);
        mapView.getMap().setBasemap(BasemapChanger.changeBasemapTo(contentKeys.get(position)));
        Snackbar.make(mapView, "Basemap changed to " + contentKeys.get(position), Snackbar.LENGTH_LONG).show();
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }


    @Override
    public int getItemCount() {

        return contentKeys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardView = null;
        TextView textView = null;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (ImageView) itemView.findViewById(R.id.basemap_image);
            textView = (TextView) itemView.findViewById(R.id.basemap_name);
        }
    }
}
