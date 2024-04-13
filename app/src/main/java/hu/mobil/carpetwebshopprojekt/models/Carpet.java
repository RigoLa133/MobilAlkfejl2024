package hu.mobil.carpetwebshopprojekt.models;

public class Carpet {
    private String name;
    private String price;
    private String category;
    private int imageResource;

    public Carpet() {
    }

    public Carpet(String name, String price, String category, int imageResource) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageResource = imageResource;
    }

    public String getName() {return name;}
    public String getPrice() {return price;}
    public String getCategory() {return category;}
    public int getImageResource() {return imageResource;}

    public int priceToInt() {
        if (price.charAt(1) == ' ') {
            return Integer.valueOf(price.substring(0,1).concat(price.substring(2,5)));
        } else {
            return Integer.valueOf(price.substring(0,2).concat(price.substring(3,6)));
        }
    }
}
