package support.esri.com.fuse.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import support.esri.com.fuse.R;

/**
 * Created by kwas7493 on 5/1/2017.
 */

public class FastAdapterItemImpl extends AbstractItem<FastAdapterItemImpl, FastAdapterItemImpl.ViewHolder> {

    public String name;

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fastadapter_layout;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        viewHolder.name.setText(name);
    }

    @Override
    public void unbindView(ViewHolder viewHolder) {
        super.unbindView(viewHolder);
        viewHolder.name.setText(null);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;


        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
