package support.esri.com.fuse;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
}
