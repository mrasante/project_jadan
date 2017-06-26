package support.esri.com.fuse;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.view.MapView;

import org.w3c.dom.Text;

/**
 * Created by kwas7493 on 3/13/2017.
 */

public class AppDialogBuilder {

    public AppDialogBuilder() {

    }

    public static AlertDialog.Builder showAccountOption(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setAdapter(new ArrayAdapter<String>(context, R.layout.adapter_res_layout_file, R.id.adapter_text, new String[]{"Facebook", "Twitter", "ArcGIS Online"}),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                   /* showMessage(context, String.valueOf(which));*/


                            }
                        });

        alertDialogBuilder.setTitle("Select Account Type");
        alertDialogBuilder.create();
        alertDialogBuilder.show();
        return alertDialogBuilder;
    }

    private static void showMessage(Context context, String testing) {
        Toast.makeText(context, testing, Toast.LENGTH_SHORT).show();
    }

    public static void showBookmarkAlert(final Activity activity) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        final View view = LayoutInflater.from(activity).inflate(R.layout.bookmark_alert_dialog, null);
        Button bookmark_button = (Button) view.findViewById(R.id.bookmark_ok_button);
        alertBuilder.setView(view);
        final AlertDialog alertDialog = alertBuilder.show();
        bookmark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) view.findViewById(R.id.bookmark_id);
                String bookmarkName = text.getText().toString();
                alertDialog.dismiss();
                CustomViewHandler.createBookmark(BookmarkFragment.newInstance(bookmarkName, "BookmarkFrag"), (MapView) activity.findViewById(R.id.fuse_map_view));
            }
        });


    }
}
