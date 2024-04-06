package hu.mobil.carpetwebshopprojekt.callback;

import com.google.firebase.firestore.DocumentReference;

public interface FindUserCallback {
    void onUserFound(DocumentReference userRef);
}
