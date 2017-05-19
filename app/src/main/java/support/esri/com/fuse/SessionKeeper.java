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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void setUsername(String username) {
        editor.putString(username, "");
        editor.commit();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }

    public void setPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public boolean clearSharedPreferences() {
        editor.clear();
        return editor.commit();
    }

    public boolean isContainsCredentials(String username, String password) {
        boolean containsFlag = false;
        boolean containsUsername = sharedPreferences.contains(username);
        boolean containsPassword = sharedPreferences.contains(password);
        if (containsUsername && containsPassword) {
            containsFlag = true;
        }
        return containsFlag;
    }
}
