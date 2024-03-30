package hu.mobil.carpetwebshopprojekt.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.R;
import hu.mobil.carpetwebshopprojekt.models.Carpet;

public class CarpetDbManager extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference carpets;

    public CarpetDbManager(FirebaseFirestore db) {
        this.db = db;
        carpets = db.collection("Carpets");
    }

    public void queryData(ArrayList<Carpet> carpetList, CarpetCardViewAdapter adapter, Context context) {
        carpetList.clear();

        carpets.orderBy("category", Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Nice", "Almost");
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Carpet carpet = doc.toObject(Carpet.class);
                carpetList.add(carpet);
            }

            if (carpetList.size() == 0) {
                initializeData(context);
                queryData(carpetList, adapter, context);
            }
            adapter.notifyDataSetChanged();
        });
    }

    public void initializeData(Context context) {
        String[] itemNames = context.getResources().getStringArray(R.array.carpet_names);
        String[] itemPrice = context.getResources().getStringArray(R.array.carpet_prices);
        String[] itemCategory = context.getResources().getStringArray(R.array.carpet_categories);
        TypedArray itemImageResource = context.getResources().obtainTypedArray(R.array.carpet_images);

        for (int i = 0; i < itemNames.length; i++) {

            carpets.add(new Carpet(itemNames[i], itemPrice[i], itemCategory[i], itemImageResource.getResourceId(i,0)));
        }

        itemImageResource.recycle();
    }
}
