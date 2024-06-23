package myapp;

import java.io.Serializable;

public class Ptak implements Serializable {
    private String imie;

    public Ptak(String imie) {
        this.imie = imie;
    }

    @Override
    public String toString() {
        return "Ptak{" + "imie='" + imie + '\'' + '}';
    }
}
