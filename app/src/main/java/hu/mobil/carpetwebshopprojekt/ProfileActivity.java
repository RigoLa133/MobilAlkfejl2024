package hu.mobil.carpetwebshopprojekt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;

import hu.mobil.carpetwebshopprojekt.dao.UserDao;
import hu.mobil.carpetwebshopprojekt.models.User;

public class ProfileActivity extends MainActivity {
    private TextView nameTV;
    private TextView emailTV;
    private TextView postalCodeTV;
    private TextView cityTV;
    private TextView addressTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (super.getUser() == null) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData(super.getUser().getEmail());
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        super.returnToHomePage();
    }


    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("UserEmail", super.getUser().getEmail());
        startActivity(intent);
    }

    public void displayInfo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A személyes adatok kitöltésével meggyorsíthatja a rendelés leadását!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void getUserData(String email) {
        UserDao.getUser(email, user -> {
            if (user != null) {
                setUserData(user);
            }
        });
    }

    private void setUserData(User user) {
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        postalCodeTV = findViewById(R.id.postalCodeTV);
        cityTV = findViewById(R.id.cityTV);
        addressTV = findViewById(R.id.addressTV);

        emailTV.setText(user.getEmail());
        if (user.getVezeteknev() != null && user.getKeresztnev() != null && !user.getKeresztnev().isEmpty() && !user.getVezeteknev().isEmpty()) {
            nameTV.setText(user.getVezeteknev().concat(" ").concat(user.getKeresztnev()));
        }
        postalCodeTV.setText(user.getPostalCode());
        cityTV.setText(user.getCity());
        addressTV.setText(user.getAddress());

    }

}