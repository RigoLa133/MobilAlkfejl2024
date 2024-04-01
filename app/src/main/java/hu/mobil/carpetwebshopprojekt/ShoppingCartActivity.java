package hu.mobil.carpetwebshopprojekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import hu.mobil.carpetwebshopprojekt.utils.ShoppingCart;
import hu.mobil.carpetwebshopprojekt.utils.ShoppingCartViewAdapter;

public class ShoppingCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShoppingCartViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.shoppingCartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingCartViewAdapter(this, ShoppingCart.getCart());
        recyclerView.setAdapter(adapter);

        checkEmptiness();
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

    public void goBack(View view) {
        finish();
    }

    public void placeOrder(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    public void checkEmptiness() {
        if (ShoppingCart.getAmount() == 0) {
            Button button = findViewById(R.id.cancel_order);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
            params.setMargins(0,0,0,0);
            findViewById(R.id.place_order).setVisibility(View.GONE);
            findViewById(R.id.cartEmptyText).setVisibility(View.VISIBLE);
        }
    }
}