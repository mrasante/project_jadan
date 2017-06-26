package support.esri.com.fuse;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esri.arcgisruntime.mapping.BookmarkList;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

/**
 * Created by kwas7493 on 6/20/2017.
 */

public class BookmarkRecyclerAdapter extends RecyclerView.Adapter<BookmarkRecyclerAdapter.BookmarkViewHolder> {
    private FragmentManager fragmentManager;
    private View inflatedView;
    private BookmarkList classBookmarkList;


//
//    public BookmarkRecyclerAdapter(){
//
//    }


    public BookmarkRecyclerAdapter(FragmentManager fragmentManager, BookmarkList bookmarkList) {
        this.fragmentManager = fragmentManager;
        this.classBookmarkList = bookmarkList;
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_inflator, null);
        BookmarkViewHolder viewHolder = new BookmarkViewHolder(inflatedView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, final int position) {
        Activity activityCompat = (Activity)inflatedView.getContext();
        final MapView mapView = (MapView)activityCompat.findViewById(R.id.fuse_map_view);
        holder.textView.setText(classBookmarkList.get(position).getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setViewpointGeometryAsync(classBookmarkList.get(position).getViewpoint().getTargetGeometry());
                List<Fragment> fragments = fragmentManager.getFragments();
               for(Fragment fragment : fragments){
                   fragmentManager.beginTransaction().hide(fragment).commit();
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return classBookmarkList.size();
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public BookmarkViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.bookmark_name);
        }
    }
}
