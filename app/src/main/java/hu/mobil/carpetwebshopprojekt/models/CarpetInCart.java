package hu.mobil.carpetwebshopprojekt.models;

public class CarpetInCart {
    private String name;
    private int amount, price, imageResource;

    public CarpetInCart(String name, int price, int imageResource) {
        this.name = name;
        this.price = price;
        this.amount = 1;
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void addOne() {
        this.amount++;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getTotalPrice() {
        return amount * price;
    }

}
