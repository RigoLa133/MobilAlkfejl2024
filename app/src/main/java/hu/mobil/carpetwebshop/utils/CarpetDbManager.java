package hu.mobil.carpetwebshop.utils;

import android.content.res.TypedArray;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hu.mobil.carpetwebshop.R;
import hu.mobil.carpetwebshop.models.Carpet;

public class CarpetDbManager extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference carpets;

    public CarpetDbManager(FirebaseFirestore db) {
        this.db = db;
        carpets = db.collection("Carpets");
    }

    public void queryData(ArrayList<Carpet> carpetList, CarpetCardViewAdapter adapter) {
        carpetList.clear();

        carpets.orderBy("category", Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Carpet carpet = doc.toObject(Carpet.class);
                carpetList.add(carpet);
            }

            if (carpetList.size() == 0) {
                initializeData();
                queryData(carpetList, adapter);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        String[] itemNames = getResources().getStringArray(R.array.carpet_names);
        String[] itemPrice = getResources().getStringArray(R.array.carpet_prices);
        String[] itemCategory = getResources().getStringArray(R.array.carpet_categories);
        TypedArray itemImageResource = getResources().obtainTypedArray(R.array.carpet_images);

        for (int i = 0; i < itemNames.length; i++) {

            carpets.add(new Carpet(itemNames[i], itemPrice[i], itemCategory[i], itemImageResource.getResourceId(i,0)));
        }

        itemImageResource.recycle();
    }
}
