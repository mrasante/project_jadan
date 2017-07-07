package support.esri.com.fuse;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kwas7493 on 2/27/2017.
 */

public class AppPreferences extends AppCompatActivity {

    private static final String PREFERENCES = "REMEMBER ME";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor sharedEditor;

    public AppPreferences(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

   public static SharedPreferences.Editor getSharedPreferencesEditor() {
       sharedEditor = sharedPreferences.edit();
        return sharedEditor;
    }

    public static SharedPreferences getSharedPreferences(){
        return  sharedPreferences;
    }


}
