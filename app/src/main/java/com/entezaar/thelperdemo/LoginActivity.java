package com.entezaar.thelperdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.entezaar.thelperdemo.util.PrefManager;

import static com.entezaar.thelperdemo.util.ServerUtil.LoginPageUrl;
import static com.entezaar.thelperdemo.util.ServerUtil.retrieveString;
import static com.entezaar.thelperdemo.util.StringUtil.checkPhoneNumber;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener , TextWatcher{

    EditText phoneNumberEditText ;
    TextInputLayout phoneNumberTextInputLayout ;
    Button submitButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        transparentLayout();

        phoneNumberEditText = findViewById(R.id.phone_et);
        phoneNumberEditText.addTextChangedListener(this);
        phoneNumberTextInputLayout = findViewById(R.id.phone_field);
        submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(this);

        View join = findViewById(R.id.join_us);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:" + getString(R.string.operator_phone_number)));
                startActivity(call_intent);
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        phoneNumberTextInputLayout.setErrorEnabled(false);
    }
    @Override
    public void afterTextChanged(Editable editable) {}


    public class DoLoginTask extends AsyncTask<String , Void , Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String response = retrieveString(LoginPageUrl + "?PhoneNumber=" + strings[0] );
            if (response == null) return false ;
            if (response.contains("1"))
            {
                PrefManager prefManager = new PrefManager(LoginActivity.this);
                prefManager.submitLogin(strings[0]);
                return true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.login_logging_in), Toast.LENGTH_SHORT).show();
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if( result ) {
                phoneNumberTextInputLayout.setErrorEnabled(false);
                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
            else
            {
                phoneNumberTextInputLayout.setError(getString(R.string.login_error_invalid));
            }


            super.onPostExecute(result);

        }
    }

    @Override
    public void onClick(View view) {

        String phoneNumber = checkPhoneNumber(phoneNumberEditText.getText().toString());

        if (phoneNumber == null)
            phoneNumberTextInputLayout.setError(getString(R.string.login_error_invalid));
        else if (phoneNumber.isEmpty())
            phoneNumberTextInputLayout.setError(getString(R.string.login_error_empty_field));
        else
        {
            DoLoginTask loginTask = new DoLoginTask();
            loginTask.execute(phoneNumber);
        }


    }

    private void transparentLayout()
    {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


}
