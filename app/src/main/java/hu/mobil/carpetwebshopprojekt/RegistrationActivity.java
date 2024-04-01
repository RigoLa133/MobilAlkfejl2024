package hu.mobil.carpetwebshopprojekt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import hu.mobil.carpetwebshopprojekt.dao.UserDao;
import hu.mobil.carpetwebshopprojekt.models.User;

public class RegistrationActivity extends AppCompatActivity {
    EditText emailET;
    EditText passwordET;
    EditText passwordConfirmET;
    Context context;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        passwordConfirmET = findViewById(R.id.passwordConfirmET);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
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

    public void register(View view) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passwordConfirmET.getText().toString();

        if (email.trim().isEmpty()) {
            emailET.setError("Az email címet kötelező megadni");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Az email cím nem elfogadható");
        }

        if (password.trim().isEmpty()) {
            passwordET.setError("A jelszót kötelező megadni");
        } else if (password.trim().length() < 6) {
            passwordET.setError("A jelszó legalább 6 karakter hosszú kell legyen");
        } else if (!password.equals(passwordConfirm)) {
            passwordConfirmET.setError("A jelszavak nem egyeznek");
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Nice", "User created successfully");
                    UserDao.addUser(new User(email));
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("Not Nice", "User registration failed: " + task.getException().getMessage());
                }
            }
        });

    }

    public void cancel(View view) {
        finish();
    }
}