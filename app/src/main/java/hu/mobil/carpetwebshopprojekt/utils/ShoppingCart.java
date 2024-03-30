package hu.mobil.carpetwebshopprojekt.utils;

import android.util.Log;

import java.util.ArrayList;

import hu.mobil.carpetwebshopprojekt.models.Carpet;
import hu.mobil.carpetwebshopprojekt.models.CarpetInCart;

public class ShoppingCart {
    private static ArrayList<CarpetInCart> contents = new ArrayList<>();

    public static void addToCart(Carpet carpet) {
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getName().equals(carpet.getName())) {
                contents.get(i).addOne();
                return;
            }
        }
        contents.add(new CarpetInCart(carpet.getName(), carpet.priceToInt(), carpet.getImageResource()));
    }

    public static int getAmount() {
        int amount = 0;
        for (CarpetInCart carpet : contents) {
            amount += carpet.getAmount();
        }
        return amount;
    }
}
