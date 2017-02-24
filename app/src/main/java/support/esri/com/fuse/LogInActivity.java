package support.esri.com.fuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button closerButton = (Button) findViewById(R.id.closer);
        closerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLauncherActivity();
            }
        });
    }

    private void startLauncherActivity() {
        Intent intent = new Intent(getApplicationContext(), Launcher.class);
        intent.putExtra("Annonymous", "ANNONYMOUS_USER");
        startActivity(intent);
    }

}
