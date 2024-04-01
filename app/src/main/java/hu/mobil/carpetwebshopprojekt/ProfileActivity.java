package hu.mobil.carpetwebshopprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import hu.mobil.carpetwebshopprojekt.dao.UserDao;
import hu.mobil.carpetwebshopprojekt.models.User;
import hu.mobil.carpetwebshopprojekt.utils.ShoppingCart;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameTV;
    private TextView emailTV;
    private TextView postalCodeTV;
    private TextView cityTV;
    private TextView addressTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (MainActivity.getUser() == null) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData(MainActivity.getUser().getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        ShoppingCart.emptyCart();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void displayInfo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A személyes adatok kitöltésével meggyorsíthatja a rendelés leadását!");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
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
        } else {
            nameTV.setText("");
        }
        postalCodeTV.setText(user.getPostalCode());
        cityTV.setText(user.getCity());
        addressTV.setText(user.getAddress());

    }

}