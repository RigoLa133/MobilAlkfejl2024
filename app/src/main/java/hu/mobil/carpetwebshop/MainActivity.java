package hu.mobil.carpetwebshop;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ShoppingItem> itemList;
    private ShoppingItemAdapter adapter;
    private FrameLayout redCircle;
    private TextView contentTextView;
    private Menu menu;

    private int gridNumber = 1;
    private boolean viewRow = true;
    private int cartItems = 0;

    protected FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemList = new ArrayList<>();
        adapter = new ShoppingItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        initializeData();

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void initializeData() {
        String[] itemNames = getResources().getStringArray(R.array.shopping_item_names);
        String[] itemPrice = getResources().getStringArray(R.array.shopping_item_prices);
        TypedArray itemImageResource = getResources().obtainTypedArray(R.array.shopping_item_images);

        itemList.clear();
        for (int i = 0; i < itemNames.length; i++) {
            itemList.add(new ShoppingItem(itemNames[i], itemPrice[i], itemImageResource.getResourceId(i,0)));
        }

        itemImageResource.recycle();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == android.R.id.home && this.getClass() != MainActivity.class) {
            returnToHomePage();
        }
        if (itemId == R.id.shopping_cart) {
            Log.d("Nice", "Shopping cart clicked");
        } else if (itemId == R.id.profile && this.getClass() != LoginActivity.class) {
            Log.d("Nice", "View selector clicked");
            redirectToLoginScreen();
        } else if (itemId == R.id.view_selector) {
            Log.d("Nice", "Profile clicked");
            if (!viewRow) {
                changeSpanCount(menuItem, R.drawable.view_selector_grid, 1);
            } else {
                changeSpanCount(menuItem, R.drawable.view_selector_row, 2);
            }
        }
        super.onOptionsItemSelected(menuItem);
        return true;
    }

    protected void redirectToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected void redirectToProfileScreen() {
        //Intent intent = new Intent(this, ProfileActivity.class);
        //startActivity(intent);
    }

    private void changeSpanCount(MenuItem menuItem, int icon, int spanCount) {
        viewRow = !viewRow;
        menuItem.setIcon(icon);
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.shopping_cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public void updateAlertIcon() {
        cartItems += 1;
        if (0 < cartItems) {
            contentTextView.setText(String.valueOf(cartItems));
        } else {
            contentTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);
    }

    public void returnToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}