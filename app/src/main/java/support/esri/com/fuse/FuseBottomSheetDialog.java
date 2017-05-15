package support.esri.com.fuse;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by kwas7493 on 4/28/2017.
 */

public class FuseBottomSheetDialog extends BottomSheetDialog {


    public View inflatedView;
    Context context;

    public FuseBottomSheetDialog(Context context) {
        super(context);
        this.context = context;
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedView = layoutInflater.inflate(R.layout.bottomsheet_recycler, viewGroup, false);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(inflatedView);

    }

    public void onStart() {
        super.onStart();
    }

}
