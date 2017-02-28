package support.esri.com.fuse;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kwas7493 on 2/27/2017.
 */

public class AppPreferences extends AppCompatActivity{

    private static final String PREFERENCES = "REMEMBER ME";


      SharedPreferences getSharedPreferences(){
      SharedPreferences  sharedPreferences =  getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences;
    }


}
