package support.esri.com.fuse;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by kwas7493 on 5/17/2017.
 */

public class SessionKeeper {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionKeeper(Context context) {
        sharedPreferences = new AppPreferences(context).getSharedPreferences(); //AppPreferences.getSharedPreferences();PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences != null){
            editor = sharedPreferences.edit();
        }else
            editor = null;
    }

    public void setUsername(String username) {
        if(editor != null){
            editor.putString("username", username);
            editor.commit();
        }else
            return;
    }

    public String getUsername() {
        return sharedPreferences.getString("username", "None");
    }

    public void setPassword(String password) {
        if(editor != null){
            editor.putString("password", password);
            editor.commit();
        }else
            return;
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "None");
    }

    public boolean clearSharedPreferences() {
        editor.clear();
        return editor.commit();
    }

    public boolean isContainsCredentials(String username, String password) {
        boolean containsFlag = false;
        boolean containsUsername = AppPreferences.getSharedPreferences().contains(username);
        boolean containsPassword = AppPreferences.getSharedPreferences().contains(password);
        if (containsUsername && containsPassword) {
            containsFlag = true;
        }
        return containsFlag;
    }
}
