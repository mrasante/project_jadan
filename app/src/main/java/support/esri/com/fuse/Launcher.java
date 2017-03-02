package support.esri.com.fuse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Launcher extends AppCompatActivity {

    private static final long DELAY_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =  new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("Authenticated",getIntent().getStringExtra("Credential_Log_In"));
                    if(getIntent().getStringExtra("whoSentYou").equalsIgnoreCase("Twitter")){
                        String imageURL = getIntent().getStringExtra("twitterProfileByte");
                        String twitterUsername = getIntent().getStringExtra("twitterUsername");
                        intent.putExtra("twitterProfileUrl", imageURL);
                        intent.putExtra("twitterUsername", twitterUsername);
                        intent.putExtra("whoSentYou","Twitter");
                    }else if(getIntent().getStringExtra("whoSentYou").equalsIgnoreCase("Facebook")){
                        String facebookImageUrl = getIntent().getStringExtra("FacebookUserProfileURL");
                        String facebookUserName = getIntent().getStringExtra("FacebookUserFullName");
                        intent.putExtra("facebookUsername",facebookUserName);
                        intent.putExtra("facebookProfileUrl", facebookImageUrl);
                        intent.putExtra("whoSentYou","Facebook");
                    }else if(getIntent().getStringExtra("whoSentYou").equalsIgnoreCase("arcgis.com")){
                        intent.putExtra("whoSentYou","arcgis.com");
                    }
                startActivity(intent);
            }
        }, DELAY_TIME);
    }


}
