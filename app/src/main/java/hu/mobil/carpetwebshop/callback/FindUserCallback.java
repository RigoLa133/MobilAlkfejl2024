package hu.mobil.carpetwebshop.callback;

import com.google.firebase.firestore.DocumentReference;

public interface FindUserCallback {
    void onUserFound(DocumentReference userRef);
}
