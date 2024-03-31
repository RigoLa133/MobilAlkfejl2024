package hu.mobil.carpetwebshopprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import hu.mobil.carpetwebshopprojekt.utils.ShoppingCart;
import hu.mobil.carpetwebshopprojekt.utils.ShoppingCartViewAdapter;

public class ShoppingCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShoppingCartViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerView = findViewById(R.id.shoppingCartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingCartViewAdapter(this, ShoppingCart.getCart());
        recyclerView.setAdapter(adapter);

        checkEmptiness();
    }

    public void goBack(View view) {
        finish();
    }

    public void placeOrder(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    public void checkEmptiness() {
        if (ShoppingCart.getAmount() == 0) {
            findViewById(R.id.cartEmptyText).setVisibility(View.VISIBLE);
        }
    }
}