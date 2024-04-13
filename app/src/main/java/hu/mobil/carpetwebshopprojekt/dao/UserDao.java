package hu.mobil.carpetwebshopprojekt.dao;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import hu.mobil.carpetwebshopprojekt.callback.FindUserCallback;
import hu.mobil.carpetwebshopprojekt.callback.UpdateUserCallback;
import hu.mobil.carpetwebshopprojekt.callback.UserCallback;
import hu.mobil.carpetwebshopprojekt.models.User;

public class UserDao {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addUser(User user) {
        db.collection("Users").add(user);
    }

    public static void getUser(String email, UserCallback callback) {
        db.collection("Users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                User user = doc.toObject(User.class);
                if (user.getEmail().equals(email)) {
                    callback.onCallBack(user);
                }
            }
        }).addOnFailureListener(e -> {
        });
    }

    public static void updateUser(String email, String vnev, String knev, String postalCode, String city, String address, UpdateUserCallback callback) {
        findCurrentUser(email, userRef -> {
            Map<String, Object> newData = new HashMap<>();
            newData.put("vezeteknev", vnev);
            newData.put("keresztnev", knev);
            newData.put("postalCode", postalCode);
            newData.put("city", city);
            newData.put("address", address);
            userRef.update(newData).addOnSuccessListener(succ -> {
                callback.onUpdateSuccess();
            });
        });
    }

    private static void findCurrentUser(String email, FindUserCallback callback) {
        Log.d("Nice", email);
        db.collection("Users").whereEqualTo("email", email).get().addOnSuccessListener(qds -> {
            if (!qds.isEmpty()) {
                Log.d("Nice", "close");
                DocumentSnapshot snapshot = qds.getDocuments().get(0);
                callback.onUserFound(snapshot.getReference());
            }
        });
    }


}
