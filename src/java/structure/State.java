package src.java.structure;

import java.util.LinkedList;

import static src.java.utils.MathUtils.*;

public class State {
    private int id;
    private String name;
    final private LinkedList<Province> provinces;
    private int colorRGB;
    private Country owner;

    public State(int id, String name, LinkedList<Province> provinces, Country owner) {
        this.id = id;
        this.name = name;
        this.provinces = provinces;
        this.owner = owner;
        randomizeColorFromOwner(30);
    }
    public State(int id, String name, LinkedList<Province> provinces) {
        this.id = id;
        this.name = name;
        this.provinces = provinces;
        colorRGB = getRandomRGB();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LinkedList<Province> getProvinces() {
        return new LinkedList<>(provinces);
    }
    public int getColorRGB() {
        return colorRGB;
    }
    public Country getCountry() {
        return owner;
    }
    public void randomizeColorFromOwner(int randomness) {
        int r = owner.getColorRGB() >> 16;
        int g = (owner.getColorRGB() >> 8) & 0xFF;
        int b = owner.getColorRGB() & 0xFF;

        int rand = getRandomInt(-randomness, randomness);

        r = clampInt(r + rand, 0, 255);
        g = clampInt(g + rand, 0, 255);
        b = clampInt(b + rand, 0, 255);
        colorRGB = (r << 16) | (g << 8) | b;
    }

    public void setOwner(Country owner) {
        this.owner = owner;
    }
    public void addProvince(Province province) {
        provinces.add(province);
    }
    public void removeProvince(Province province) {
        provinces.remove(province);
        if (provinces.isEmpty()) {
            throw new RuntimeException("State is empty!");
        }
    }
    public Boolean containsProvince(Province province) {
        return provinces.contains(province);
    }
}
