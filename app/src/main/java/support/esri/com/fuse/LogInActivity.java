package support.esri.com.fuse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.security.UserCredential;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.HashSet;
import java.util.Set;

import io.fabric.sdk.android.Fabric;


public class LogInActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mrasante";
    private static final String TWITTER_SECRET = "applegoe";


    private CheckBox rememberMeChkBox;
    private static final String PREFERENCES = "REMEMBER ME";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private SharedPreferences sharedPreferences;
    private EditText usernameEditText;
    private EditText passwordEditText;
    public static Portal fusePortal;
    public static boolean loggedIn;
    private boolean isChecked;
    private Button signInButton;
    private ProgressDialog progressDialog;
    private LoginButton facebookButton;
    private CallbackManager facebookCallbackManager;
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button closerButton = (Button) findViewById(R.id.closer);
        closerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLauncherActivity();
            }
        });
        //for logging in with arcgis
        authenticateWithArcGIS();

        //for logging in with facebook
        authenticateWithFacebook();

        //for logging in with twitter
        authenticateWithTwitter();
    }

    private void authenticateWithTwitter() {
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    /**
     * Method to authenticate user using facebook account
     * Requests permissions to read profile, email and user's friends list
     */

    private void authenticateWithFacebook() {
        facebookButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookButton.setReadPermissions("public_profile", "email", "user_friends");
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new ProfileTracker() {

                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        Intent intent = new Intent(getApplicationContext(), Launcher.class);
                        intent.putExtra("whoSentYou", "Facebook");
                        intent.putExtra("FacebookUserFullName", currentProfile.getName());
                        intent.putExtra("FacebookUserProfileURL", currentProfile.getProfilePictureUri(10, 10));
                        startActivity(intent);
                    }
                };
            }

            @Override
            public void onCancel() {
                Snackbar.make(getCurrentFocus(), "Facebook Login cancelled by user", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Snackbar.make(getCurrentFocus(), error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }


    private void authenticateWithArcGIS() {
        //init the sharedPreferences reference;
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        //get handles on all controls
        rememberMeChkBox = (CheckBox) findViewById(R.id.remember_me_auth);
        usernameEditText = (EditText) findViewById(R.id.auth_username);
        passwordEditText = (EditText) findViewById(R.id.auth_password);
        signInButton = (Button) findViewById(R.id.sign_in);
        //init the state for shared preferences storage
        rememberMeChkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rememberMeChkBox.isChecked()) {
                    isChecked = true;
                } else {
                    isChecked = false;
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginAsyncTask().execute(retrieveUserCredentials());
            }
        });
    }

    private void startLauncherActivity() {
        Intent intent = new Intent(getApplicationContext(), Launcher.class);
        intent.putExtra("whoSentYou", "anonymous");
        intent.putExtra("Anonymous", "ANONYMOUS_USER");
        startActivity(intent);
    }


    private UserCredential retrieveUserCredentials() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (username.length() == 0 && password.length() == 0) {
            return null;
        } else
            return new UserCredential(username, password);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Use this intenal class to log in to the arcgis.com
     */

    class LoginAsyncTask extends AsyncTask<UserCredential, Void, Void> {


        @Override
        public void onPreExecute() {
            progressDialog = ProgressDialog.show(LogInActivity.this, "Signing in", "Authenticating with arcgis.com...", true);
        }

        @Override
        protected Void doInBackground(final UserCredential... credentials) {
            fusePortal = new Portal("https://www.arcgis.com", true);
            if (credentials[0] == null) {
                Snackbar.make(getCurrentFocus(), "Please enter username and password", Snackbar.LENGTH_LONG).show();
                return null;
            }

            fusePortal.setCredential(credentials[0]);
            fusePortal.loadAsync();
            fusePortal.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    if (fusePortal.getLoadStatus() == LoadStatus.LOADED) {
                        loggedIn = true;
                        if (isChecked) {
                            Set<String> credSet = new HashSet<>();
                            credSet.add(credentials[0].getUsername());
                            credSet.add(credentials[0].getPassword());
                            sharedPreferences.edit().putStringSet("credentials", credSet);
                            Intent intent = new Intent(getApplicationContext(), Launcher.class);
                            intent.putExtra("whoSentYou", "arcgis.com");
                            intent.putExtra("Credential_Log_In", "Authenticated");
                            startActivity(intent);
                        }
                    } else if (fusePortal.getLoadStatus() == LoadStatus.FAILED_TO_LOAD) {
                        Snackbar.make(getCurrentFocus(), "Login failed, Please try again.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
            return null;
        }

        @Override
        public void onPostExecute(Void voidParam) {
            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }

}
