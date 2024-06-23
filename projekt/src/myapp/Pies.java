package myapp;

import java.io.Serializable;

public class Pies implements Serializable {
    private String imie;

    public Pies(String imie) {
        this.imie = imie;
    }

    @Override
    public String toString() {
        return "Pies{" + "imie='" + imie + '\'' + '}';
    }
}
