package myapp;

import java.io.Serializable;

public class Kot implements Serializable {
    private String imie;

    public Kot(String imie) {
        this.imie = imie;
    }

    @Override
    public String toString() {
        return "Kot{" + "imie='" + imie + '\'' + '}';
    }
}
