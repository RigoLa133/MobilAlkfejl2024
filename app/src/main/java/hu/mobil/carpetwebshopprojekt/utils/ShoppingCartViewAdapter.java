package hu.mobil.carpetwebshopprojekt.utils;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.R;
import hu.mobil.carpetwebshopprojekt.models.CarpetInCart;

public class ShoppingCartViewAdapter extends RecyclerView.Adapter<ShoppingCartViewAdapter.ViewHolder> {
    private ArrayList<CarpetInCart> carpets;
    private Context context;

    public ShoppingCartViewAdapter(Context context, ArrayList<CarpetInCart> carpets) {
        this.context = context;
        this.carpets = carpets;
    }

    @NonNull
    @Override
    public ShoppingCartViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.shopping_cart_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ShoppingCartViewAdapter.ViewHolder holder, int position) {
        CarpetInCart currentCarpet = carpets.get(position);
        holder.bindTo(currentCarpet);
    }

    @Override
    public int getItemCount() {return carpets.size();}

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView carpetName;
        private TextView carpetAmount;
        private TextView carpetTotal;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shoppingCartItemImage);
            carpetName = itemView.findViewById(R.id.shoppingCartItemName);
            carpetAmount = itemView.findViewById(R.id.shoppingCartAmount);
            carpetTotal = itemView.findViewById(R.id.shoppingCartTotal);
        }

        public void bindTo(CarpetInCart carpet) {
            carpetName.setText(carpet.getName());
            carpetAmount.setText(String.valueOf(carpet.getAmount()).concat(" db"));
            carpetTotal.setText(String.valueOf(carpet.getTotalPrice()).concat(" Ft"));
            Glide.with(context).load(carpet.getImageResource()).into(imageView);
        }

    }
}
