package com.example.pfamonument.Model;

public class Images {

    String title, image, description, teste;
    public double latitude, longitude;

    public Images() { }

    public Images(String title, String image, String description, String teste, double latitude, double longitude) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.teste = teste;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String test) {
        this.teste = test;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
