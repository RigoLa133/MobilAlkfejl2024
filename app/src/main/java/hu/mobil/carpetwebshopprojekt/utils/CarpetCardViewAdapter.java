package hu.mobil.carpetwebshopprojekt.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.MainActivity;
import hu.mobil.carpetwebshopprojekt.R;
import hu.mobil.carpetwebshopprojekt.models.Carpet;

public class CarpetCardViewAdapter extends RecyclerView.Adapter<CarpetCardViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<Carpet> carpetsData;
    private ArrayList<Carpet> carpetsDataAll;
    private Context context;
    private int lastPosition = -1;

    public CarpetCardViewAdapter(Context context, ArrayList<Carpet> carpetsData) {
        this.context = context;
        this.carpetsData = carpetsData;
        this.carpetsDataAll = carpetsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.carpet_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Carpet currentCarpet = carpetsData.get(position);
        holder.bindTo(currentCarpet);
        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return carpetsData.size();
    }

    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Carpet> filteredCarpets = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.count = carpetsDataAll.size();
                results.values = carpetsDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Carpet item : carpetsDataAll){
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredCarpets.add(item);
                    }
                }
                results.count = filteredCarpets.size();
                results.values = filteredCarpets;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            carpetsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView titleText;
        private TextView priceText;
        private Button button;
        Animation anim;


        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            titleText = itemView.findViewById(R.id.itemTitle);
            priceText = itemView.findViewById(R.id.price);
            button = itemView.findViewById(R.id.addToCart);
            anim = AnimationUtils.loadAnimation(context, R.anim.add_to_cart_button_animation);

        }

        public void bindTo(Carpet currentCarpet) {
            titleText.setText(currentCarpet.getName());
            priceText.setText(currentCarpet.getPrice());
            button.setOnClickListener(v -> {
                    ((MainActivity)context).addToCart(currentCarpet);
                    button.startAnimation(anim);
            });
            Glide.with(context).load(currentCarpet.getImageResource()).into(itemImage);
        }
    }
}
