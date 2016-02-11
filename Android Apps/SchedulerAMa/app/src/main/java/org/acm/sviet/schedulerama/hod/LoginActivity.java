package org.acm.sviet.schedulerama.hod;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.acm.sviet.schedulerama.R;


public class LoginActivity extends AppCompatActivity {

    private EditText hodUsernameEditText;
    private EditText hodPasswordEditText;
    private Button hodLoginButton;

    private String TAG="/LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hodUsernameEditText = (EditText) findViewById(R.id.hodUsernameEditText);
        hodPasswordEditText = (EditText) findViewById(R.id.hodPasswordEditText);
        hodLoginButton = (Button) findViewById(R.id.hodLoginButton);
        hodLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                Log.d(TAG, "Username : " + hodUsernameEditText.getText() + " Password :" + hodPasswordEditText.getText());
                if(getResources().getString(R.string.hod_uname).equals(hodUsernameEditText.getText().toString())){
                    if(getResources().getString(R.string.hod_pass).equals(hodPasswordEditText.getText().toString())){
                       msg="Login correct.";
                        startActivity(new Intent(LoginActivity.this, HodHomeActivity.class));
                    } else {
                        msg = "Wrong Password.";
                    }
                } else{msg = "Wrong Username.";}
                Snackbar.make(v,msg,Snackbar.LENGTH_LONG).show();

            }
        });
    }
}
