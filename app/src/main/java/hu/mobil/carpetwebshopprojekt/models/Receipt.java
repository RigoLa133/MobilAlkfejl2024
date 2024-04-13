package hu.mobil.carpetwebshopprojekt.models;

import java.util.ArrayList;
import java.util.Random;

public class Receipt {
    private int id;
    private String userEmail;
    ArrayList<String> carpetNames;
    ArrayList<Integer> carpetAmounts;
    ArrayList<Integer> carpetPrices;
    int totalPrice;

    String name;
    String postalCode;
    String city;
    String address;

    public Receipt() {}

    public Receipt(String userEmail, ArrayList<String> carpetNames, ArrayList<Integer> carpetAmounts, ArrayList<Integer> carpetPrices, int totalPrice, String vnev, String knev, String postalCode, String city, String address) {
        this.id = new Random().nextInt(999999999 - 10000000 +1) - 10000000;
        this.userEmail = userEmail;
        this.carpetNames = carpetNames;
        this.carpetAmounts = carpetAmounts;
        this.carpetPrices = carpetPrices;
        this.totalPrice = totalPrice;

        this.name = vnev.concat(" ").concat(knev);
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
    }

    public int getId() {return id;}

    public String getUserEmail() {
        return userEmail;
    }

    public ArrayList<String> getCarpetNames() {
        return carpetNames;
    }

    public ArrayList<Integer> getCarpetPrices() {
        return carpetPrices;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public ArrayList<Integer> getCarpetAmounts() {
        return carpetAmounts;
    }

    public String getName() {
        return name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }
}
