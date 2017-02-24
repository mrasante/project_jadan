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
                startActivity(intent);
            }
        }, DELAY_TIME);
    }
}
