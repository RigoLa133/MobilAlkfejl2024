package hu.mobil.carpetwebshop.models;

public class Receipt {
    private String userId;
    String[] carpetNames;
    String[] carpetPrices;
    int totalPrice;

    public Receipt() {}

    public Receipt(String userId, String[] carpetNames, String[] carpetPrices, int totalPrice) {
        this.userId = userId;
        this.carpetNames = carpetNames;
        this.carpetPrices = carpetPrices;
        this.totalPrice = totalPrice;
    }


    public String getUserId() {
        return userId;
    }

    public String[] getCarpetNames() {
        return carpetNames;
    }

    public String[] getCarpetPrices() {
        return carpetPrices;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
