package hu.mobil.carpetwebshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void login(View view) {

    }

    public void loginWithGoogle(View view) {
    }

    public void redirectToRegistrationScreen(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}