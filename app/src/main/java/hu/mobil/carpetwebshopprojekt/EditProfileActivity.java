package hu.mobil.carpetwebshopprojekt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import hu.mobil.carpetwebshopprojekt.callback.UpdateUserCallback;
import hu.mobil.carpetwebshopprojekt.dao.UserDao;
import hu.mobil.carpetwebshopprojekt.models.User;

public class EditProfileActivity extends AppCompatActivity {
    private String currentUserEmail;
    private EditText vnevET;
    private EditText knevET;
    private EditText postalCodeET;
    private EditText cityET;
    private EditText addressET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        currentUserEmail = getIntent().getStringExtra("UserEmail");
        getUserData(currentUserEmail);
    }

    public void cancel(View view) {
        Log.d("Nice", "Burh");
        finish();
    }

    public void saveChanges(View view) {
        vnevET = findViewById(R.id.vnevET);
        knevET = findViewById(R.id.knevET);
        postalCodeET = findViewById(R.id.postalCodeET);
        cityET = findViewById(R.id.cityET);
        addressET = findViewById(R.id.addressET);
        String vnev = vnevET.getText().toString();
        String knev = knevET.getText().toString();
        String postalCode = postalCodeET.getText().toString();
        String city = cityET.getText().toString();
        String address = addressET.getText().toString();

        UserDao.updateUser(currentUserEmail, vnev, knev, postalCode, city, address, new UpdateUserCallback() {
            @Override
            public void onUpdateSuccess() {
                finish();
            }
        });
    }

    private void getUserData(String email) {
        UserDao.getUser(email, user -> {
            if (user != null) {
                setUserData(user);
            } else {
                Log.d("Not nice", "User not got");
            }
        });
    }

    private void setUserData(User user) {
        vnevET = findViewById(R.id.vnevET);
        knevET = findViewById(R.id.knevET);
        postalCodeET = findViewById(R.id.postalCodeET);
        cityET = findViewById(R.id.cityET);
        addressET = findViewById(R.id.addressET);

        vnevET.setText(user.getVezeteknev());
        knevET.setText(user.getKeresztnev());
        postalCodeET.setText(user.getPostalCode());
        cityET.setText(user.getCity());
        addressET.setText(user.getAddress());
    }
}