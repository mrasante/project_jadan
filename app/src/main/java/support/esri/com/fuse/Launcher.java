package support.esri.com.fuse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Launcher extends AppCompatActivity {

    private long DELAY_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        final AppPreferences appPreferences = new AppPreferences(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("Authenticated", getIntent().getStringExtra("Credential_Log_In"));
                SharedPreferences.Editor editor = AppPreferences.getSharedPreferencesEditor();
                if (getIntent().getStringExtra("whoSentYou").equalsIgnoreCase("Twitter")) {
                    String imageURL = getIntent().getStringExtra("twitterProfileByte");
                    String twitterUsername = getIntent().getStringExtra("twitterUsername");
                    editor.putString("whoSentYou", "Twitter");
                    editor.putString("twitterProfileUrl", imageURL);
                    editor.putString("twitterUsername", twitterUsername);
                    editor.commit();
                } else if (getIntent().getStringExtra("whoSentYou").equalsIgnoreCase("Facebook")) {
                    String facebookImageUrl = getIntent().getStringExtra("FacebookUserProfileURL");
                    String facebookUserName = getIntent().getStringExtra("FacebookUserFullName");
                    editor.putString("whoSentYou", "Facebook");
                    editor.putString("twitterProfileUrl", facebookImageUrl);
                    editor.putString("twitterUsername", facebookUserName);
                    editor.commit();
                } else if (getIntent().getStringExtra("whoSentYou").equalsIgnoreCase("arcgis.com")) {
                    editor.putString("whoSentYou", "arcgis.com");
                    editor.commit();
                }
                startActivity(intent);
            }
        }, DELAY_TIME);
    }


}
