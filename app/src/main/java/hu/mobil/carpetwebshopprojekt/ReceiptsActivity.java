package hu.mobil.carpetwebshopprojekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.models.Receipt;
import hu.mobil.carpetwebshopprojekt.utils.ReceiptCardViewAdapter;

public class ReceiptsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReceiptCardViewAdapter adapter;
    private ArrayList<Receipt> receipts;
    private CollectionReference receiptsRef = FirebaseFirestore.getInstance().collection("Receipts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        receipts = new ArrayList<>();
        recyclerView = findViewById(R.id.receiptsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReceiptCardViewAdapter(this, receipts);
        recyclerView.setAdapter(adapter);
        readReceipts();
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

    private void readReceipts() {
        receiptsRef.whereEqualTo("userEmail", MainActivity.getUser().getEmail()).get().addOnSuccessListener(qds -> {
            for (QueryDocumentSnapshot snapshot : qds) {
                Receipt receipt = snapshot.toObject(Receipt.class);
                receipts.add(receipt);
            }
            adapter.notifyDataSetChanged();
            checkEmptiness();
        });
    }

    private void checkEmptiness() {
        if (receipts.size() == 0) {
            findViewById(R.id.noReceipts).setVisibility(View.VISIBLE);
        }
    }

    public void deleteReceiptFromDb(Receipt receipt) {
        receiptsRef.whereEqualTo("id", receipt.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
           for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
               DocumentReference docRef = snapshot.getReference();
               docRef.delete();
           }
        });
    }

    public void deleteReceiptConfirm(Receipt receipt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Biztosan törölni szeretné a kiválasztott számlát?");
        builder.setPositiveButton("Igen", (dialog, which) -> {
            deleteReceiptFromDb(receipt);
            deleteReceiptLocally(receipt);
            dialog.dismiss();
        });
        builder.setNegativeButton("Mégsem", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    public void deleteReceiptLocally(Receipt receipt) {
        receipts.remove(receipt);
        adapter.notifyDataSetChanged();
        checkEmptiness();
    }

    public void checkReceipt(Receipt receipt) {


        View receiptDetails = getLayoutInflater().inflate(R.layout.receipt_details, null);
        setDetails(receiptDetails, receipt);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(receiptDetails);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void setDetails(View view, Receipt receipt) {
        LinearLayout totalLayout = view.findViewById(R.id.receiptDetailsTotal);
        LinearLayout namesLayout = view.findViewById(R.id.carpetNames);
        LinearLayout amountsLayout = view.findViewById(R.id.carpetAmounts);
        LinearLayout pricesLayout = view.findViewById(R.id.carpetPrices);

        TextView id = view.findViewById(R.id.receiptDetailsId);
        id.setText(String.valueOf(receipt.getId()));
        TextView name = view.findViewById(R.id.receiptDetailsName);
        name.setText(receipt.getName());
        TextView postalCode = view.findViewById(R.id.receiptDetailsPostalCode);
        postalCode.setText(receipt.getPostalCode());
        TextView city = view.findViewById(R.id.receiptDetailsCity);
        city.setText(receipt.getCity());
        TextView address = view.findViewById(R.id.receiptDetailsAddress);
        address.setText(receipt.getAddress());


        ArrayList<String> carpetNames = receipt.getCarpetNames();
        ArrayList<Integer> carpetAmounts = receipt.getCarpetAmounts();
        ArrayList<Integer> carpetPrices = receipt.getCarpetPrices();
        int total = receipt.getTotalPrice();

        for (int i = 0; i < carpetAmounts.size(); i++) {
            TextView carpetName = new TextView(this);
            TextView carpetAmount = new TextView(this);
            TextView carpetTotal = new TextView(this);

            String[] split = carpetNames.get(i).split("\n");

            carpetName.setText(split[0]);
            carpetAmount.setText(String.valueOf(carpetPrices.get(i)));
            carpetTotal.setText(String.valueOf(carpetAmounts.get(i)));
            carpetTotal.setGravity(Gravity.END);

            namesLayout.addView(carpetName);
            amountsLayout.addView(carpetAmount);
            pricesLayout.addView(carpetTotal);
        }

        TextView totalTv = new TextView(this);
        totalTv.setText(String.valueOf(total));
        totalTv.setGravity(Gravity.END);
        totalLayout.addView(totalTv);

    }
}