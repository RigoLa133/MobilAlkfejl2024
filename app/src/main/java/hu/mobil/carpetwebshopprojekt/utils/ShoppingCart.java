package hu.mobil.carpetwebshopprojekt.utils;

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

    public static ArrayList<CarpetInCart> getCart() {
        return contents;
    }

    public static void addToCarpet(CarpetInCart carpet) {
        carpet.increment();
    }

    public static void removeCarpet(CarpetInCart carpet) {
        boolean deleted = !carpet.decrement();
        if (deleted) {
            contents.remove(carpet);
        }
    }

    public static int getTotalPrice() {
        int total = 0;
        for (CarpetInCart carpet : contents) {
            total += carpet.getTotalPrice();
        }
        return total;
    }

    public static void emptyCart() {
        contents.clear();
    }
}
