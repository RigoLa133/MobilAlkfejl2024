package hu.mobil.carpetwebshopprojekt.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.R;
import hu.mobil.carpetwebshopprojekt.ReceiptsActivity;
import hu.mobil.carpetwebshopprojekt.models.Receipt;

public class ReceiptCardViewAdapter extends RecyclerView.Adapter<ReceiptCardViewAdapter.ViewHolder> {
    private ArrayList<Receipt> receipts;
    private Context context;

    public ReceiptCardViewAdapter(Context context, ArrayList<Receipt> receipts) {
        this.context = context;
        this.receipts = receipts;
    }

    @NonNull
    @Override
    public ReceiptCardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.receipt_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptCardViewAdapter.ViewHolder holder, int position) {
        Receipt currentReceipt = receipts.get(position);
        holder.bindTo(currentReceipt);
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView receiptName;
        private ImageButton deleteReceipt;
        private ImageButton checkReceipt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            receiptName = itemView.findViewById(R.id.receiptNameTV);
            deleteReceipt = itemView.findViewById(R.id.deleteReceipt);
            checkReceipt = itemView.findViewById(R.id.checkReceipt);
        }

        public void bindTo(Receipt receipt) {
            receiptName.setText(String.valueOf(receipt.getId()));

            deleteReceipt.setOnClickListener(e -> {
                ((ReceiptsActivity)context).deleteReceiptConfirm(receipt);
            });

            checkReceipt.setOnClickListener(e -> {
                ((ReceiptsActivity)context).checkReceipt(receipt);
            });
        }
    }
}
