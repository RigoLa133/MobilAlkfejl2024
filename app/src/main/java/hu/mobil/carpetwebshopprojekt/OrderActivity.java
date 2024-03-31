package hu.mobil.carpetwebshopprojekt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.dao.UserDao;
import hu.mobil.carpetwebshopprojekt.models.CarpetInCart;
import hu.mobil.carpetwebshopprojekt.models.Receipt;
import hu.mobil.carpetwebshopprojekt.models.User;
import hu.mobil.carpetwebshopprojekt.utils.ShoppingCart;

public class OrderActivity extends AppCompatActivity {
    private EditText vnevET;
    private EditText knevET;
    private EditText postalCodeET;
    private EditText cityET;
    private EditText addressET;
    private CheckBox checkBox;

    private Receipt receipt;

    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        vnevET = findViewById(R.id.orderVnevET);
        knevET = findViewById(R.id.orderKnevET);
        postalCodeET = findViewById(R.id.orderPostalCodeET);
        cityET = findViewById(R.id.orderCityET);
        addressET = findViewById(R.id.orderAddressET);
        checkBox = findViewById(R.id.notificationCheckbox);

        getUserData(MainActivity.getUser().getEmail());
        createNotificationChannel();
    }

    public void cancel(View view) {
        finish();
    }

    public void finalizeOrder(View view) {
        String vnev = vnevET.getText().toString();
        String knev = knevET.getText().toString();
        String postalCode = postalCodeET.getText().toString();
        String city = cityET.getText().toString();
        String address = addressET.getText().toString();


        if (vnev.trim().isEmpty() || knev.trim().isEmpty() || postalCode.trim().isEmpty()
            || city.trim().isEmpty() || address.trim().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Minden adatot kötelező megadni!");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                builder.show();
                return;
        }

        ArrayList<String> carpetNames = new ArrayList<>();
        ArrayList<Integer> carpetAmounts = new ArrayList<>();
        ArrayList<Integer> carpetPrices = new ArrayList<>();
        int receiptTotal = ShoppingCart.getTotalPrice();

        for (CarpetInCart carpet : ShoppingCart.getCart()) {
            carpetNames.add(carpet.getName());
            carpetAmounts.add(carpet.getAmount());
            carpetPrices.add(carpet.getTotalPrice());
        }
        receipt = new Receipt(MainActivity.getUser().getEmail(),
                                        carpetNames, carpetPrices, carpetAmounts, receiptTotal,
                                        vnev, knev, postalCode, city, address);

        if (checkBox.isChecked()) {
            ShoppingCart.emptyCart();
            askForPermission();
        } else {
            insertReceipt(receipt);
            ShoppingCart.emptyCart();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void insertReceipt(Receipt receipt) {
        CollectionReference receiptsRef = FirebaseFirestore.getInstance().collection("Receipts");
        receiptsRef.add(receipt);
    }

    private void getUserData(String email) {
        UserDao.getUser(email, user -> {
            if (user != null) {
                setUserData(user);
            }
        });
    }

    private void setUserData(User user) {

        vnevET.setText(user.getVezeteknev());
        knevET.setText(user.getKeresztnev());
        postalCodeET.setText(user.getPostalCode());
        cityET.setText(user.getCity());
        addressET.setText(user.getAddress());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Mr. Carpet Notification Channel";
            String description = "Receipt Done Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            notificationChannel = new NotificationChannel("Mr. Carpet", name, importance);
            notificationChannel.setDescription(description);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification() {
        String text = "Elkészült a számlája!";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Mr. Carpet")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Mr. Carpet")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(0, builder.build());
    }

    private void orderDelayed(Receipt receipt) {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                runOnUiThread(() ->{
                    sendNotification();
                    insertReceipt(receipt);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        info();
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
            }
        }
        orderDelayed(receipt);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    orderDelayed(receipt);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void info() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Amint elkészült a számla értesítjük önt!");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        builder.show();
    }

}