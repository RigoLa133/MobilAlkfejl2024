package hu.mobil.carpetwebshop;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends MainActivity {
    EditText vnevET;
    EditText knevET;
    EditText emailET;
    EditText passwordET;
    EditText passwordConfirmET;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        vnevET = findViewById(R.id.vezetekNevET);
        knevET = findViewById(R.id.keresztNevET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        passwordConfirmET = findViewById(R.id.passwordConfirmET);

        auth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        String vnev = vnevET.getText().toString();
        String knev = knevET.getText().toString();
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
        } else if (!password.equals(passwordConfirm)) {
            passwordConfirmET.setError("A jelszavak nem egyeznek");
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Nice", "User created successfully");
                    RegistrationActivity.super.redirectToProfileScreen();
                } else {
                    Log.d("Not Nice", "User registration failed");
                }
            }
        });

    }

    public void cancel(View view) {
        finish();
    }
}