package hu.mobil.carpetwebshop;

public class ShoppingItem {
    private String name;
    //private String description;
    private String price;
    private final int imageResource;

    public ShoppingItem(String name, String price, int imageResource) {
        this.name = name;
        //this.description = description;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getName() {return name;}
    //public String getDescription() {return description;}
    public String getPrice() {return price;}
    public int getImageResource() {return imageResource;}
}
