package hu.mobil.carpetwebshopprojekt.models;

public class User {
    private String email;
    private String keresztnev;
    private String vezeteknev;
    private String postalCode;
    private String city;
    private String address;

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public User(String email, String keresztnev, String vezeteknev, String postalCode, String city, String address) {
        this.email = email;
        this.keresztnev = keresztnev;
        this.vezeteknev = vezeteknev;
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getKeresztnev() {
        return keresztnev;
    }

    public String getVezeteknev() {
        return vezeteknev;
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
