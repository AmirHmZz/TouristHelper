package com.entezaar.thelperdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.entezaar.thelperdemo.util.PrefManager;

import static com.entezaar.thelperdemo.util.ServerUtil.LoginPageUrl;
import static com.entezaar.thelperdemo.util.ServerUtil.retrieveString;

public class SplashActivity extends AppCompatActivity {

    private final int SplashTime = 3000;
    private PrefManager Pref ;
    private boolean LoggedInBefore ;
    private boolean TimerDone = false ;
    private Boolean LoginResult ;
    private Point point ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        transparentLayout();

        if(!isInternetAvailable())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.splash_check_internet_conectivity))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.splash_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SplashActivity.this.finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            Pref = new PrefManager(this);
            LoggedInBefore = Pref.checkLogin();

            point = new Point();
            getWindowManager().getDefaultDisplay().getRealSize(point);

            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(SplashTime);
                    } catch (Exception ignored) {
                    } finally {
                        TimerDone = true;
                        if (!LoggedInBefore)
                            GoToLoginActivity();
                        if (LoginResult != null)
                            if (LoginResult)
                                GoToMainActivity();
                            else
                                GoToLoginActivity();
                    }
                }
            }.start();

            if (LoggedInBefore) (new LoginTask()).execute(Pref.getPhoneNumber());
        }





    }

    public class LoginTask extends AsyncTask<String , Void , Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String response = retrieveString(LoginPageUrl + "?PhoneNumber=" + strings[0] );
            if (response == null) return false ;
            return response.contains("1");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            LoginResult = result ;
            if ( TimerDone )
                if ( result )
                    GoToMainActivity();
                else
                    GoToLoginActivity();
        }
    }

    private void GoToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this , LoginActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void GoToMainActivity() {
        Intent intent = new Intent(SplashActivity.this , MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void transparentLayout()
    {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private boolean isInternetAvailable () {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;
        } catch (Exception ignored) {
            return true ;
        }

    }

}
