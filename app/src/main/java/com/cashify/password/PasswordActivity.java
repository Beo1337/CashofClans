package com.cashify.password;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.cashify.R;

import java.security.NoSuchAlgorithmException;

public class PasswordActivity extends AppCompatActivity {

    private Switch passwordSwitch;
    private EditText passwordText;
    private Button passwordSave;
    private Button passwordReset;
    private PasswordFiler passwordFiler;
    private boolean active;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        try {
            passwordFiler = new PasswordFiler(getApplicationContext());
        } catch (NoSuchAlgorithmException ex) {
            passwordSave.setEnabled(false);
            passwordText.setEnabled(false);
            passwordReset.setEnabled(false);
            passwordText.setText(getResources().getString(R.string.error_sha256));
        }

        passwordText = (EditText) findViewById(R.id.password_text);
        passwordSave = (Button) findViewById(R.id.password_save);
        passwordReset = (Button) findViewById(R.id.password_reset);

        refreshButtonStates();

        if (active) {
            passwordText.setText("<Passwort>");
        }

        passwordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordFiler.setPassword(passwordText.getText().toString());
                refreshButtonStates();
            }
        });

        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordFiler.clearPassword();
                passwordText.setText("");
                refreshButtonStates();
            }
        });
    }

    private void refreshButtonStates() {
        active = passwordFiler.hasSetPassword();
        passwordSave.setEnabled(!active);
        passwordReset.setEnabled(active);
        passwordText.setEnabled(!active);
    }
}
