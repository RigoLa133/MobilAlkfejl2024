package hu.mobil.carpetwebshopprojekt;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.models.Carpet;
import hu.mobil.carpetwebshopprojekt.utils.CarpetCardViewAdapter;
import hu.mobil.carpetwebshopprojekt.utils.CarpetDbManager;
import hu.mobil.carpetwebshopprojekt.utils.ShoppingCart;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Carpet> carpetList;
    private CarpetCardViewAdapter adapter;
    private FrameLayout redCircle;
    private TextView contentTextView;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private CarpetDbManager carpetDbManager;

    private int gridNumber = 1;
    private boolean viewRow = true;

    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        carpetList = new ArrayList<>();
        adapter = new CarpetCardViewAdapter(this, carpetList);
        recyclerView.setAdapter(adapter);
        drawerLayout = findViewById(R.id.mainLayout);
        navigationView = findViewById(R.id.navLayout);
        user = FirebaseAuth.getInstance().getCurrentUser();

        initializeSideMenu();

        carpetDbManager = new CarpetDbManager(FirebaseFirestore.getInstance());
        carpetDbManager.queryData(carpetList, adapter, this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchBar = menu.findItem(R.id.search_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);
        SearchView searchView = (SearchView) searchBar.getActionView();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        //logo clicked
        if (itemId == android.R.id.home && this.getClass() != MainActivity.class) {
            returnToHomePage();
        }
        //shopping cart clicked
        if (itemId == R.id.shopping_cart) {
            redirectToShoppingCart();
        //view selector clicked
        } else if (itemId == R.id.view_selector) {
            if (!viewRow) {
                changeSpanCount(menuItem, R.drawable.view_selector_grid, 1);
            } else {
                changeSpanCount(menuItem, R.drawable.view_selector_row, 2);
            }
        } else if (itemId == R.id.side_menu) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        super.onOptionsItemSelected(menuItem);
        return true;
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

    private void initializeSideMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.allCarpets) {
                carpetDbManager.queryData(carpetList, adapter, this);
            } else if (itemId == R.id.darabCarpets) {
                carpetDbManager.queryDataByCategory(carpetList, adapter,"darab");
            } else if (itemId == R.id.outdoorCarpets) {
                carpetDbManager.queryDataByCategory(carpetList, adapter,"kulteri");
            } else if (itemId == R.id.bathroomCarpets) {
                carpetDbManager.queryDataByCategory(carpetList, adapter,"furdoszoba");
            } else if (itemId == R.id.childrensCarpets) {
                carpetDbManager.queryDataByCategory(carpetList, adapter,"gyerek");
            } else if (itemId == R.id.kitchenCarpets) {
                carpetDbManager.queryDataByCategory(carpetList, adapter, "konyha");
            } else if (itemId == R.id.profile) {
                if (user != null) {
                    redirectToProfileScreen();
                } else {
                    redirectToLoginScreen();
                }
            }
            else if (itemId == R.id.logout) {
                if (user != null) logout(navigationView);
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void changeSpanCount(MenuItem menuItem, int icon, int spanCount) {
        viewRow = !viewRow;
        menuItem.setIcon(icon);
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    public void addToCart(Carpet carpet) {
        ShoppingCart.addToCart(carpet);
        if (0 < ShoppingCart.getAmount()) {
            findViewById(R.id.shopping_cart).startAnimation(AnimationUtils.loadAnimation(this, R.anim.cart_rotation));
            contentTextView.setText(String.valueOf(ShoppingCart.getAmount()));
        } else {
            contentTextView.setText("");
        }

        redCircle.setVisibility((ShoppingCart.getAmount() > 0) ? VISIBLE : GONE);
    }

    public void returnToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void redirectToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void redirectToProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void logout(View view) {
        user = null;
        FirebaseAuth.getInstance().signOut();
    }

    public void redirectToShoppingCart() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }
}
