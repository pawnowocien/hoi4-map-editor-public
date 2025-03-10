package src.java.parser;

import src.java.structure.provinceType;

import java.awt.*;

public class ProvinceData {
    private final int id;
    private final int r;
    private final int g;
    private final int b;
    private final provinceType type;
    private final boolean coastal;
    private final String terrain;
    private final int continent;

    public ProvinceData(int id, int r, int g, int b, String type, boolean coastal, String terrain, int continent) {
        this.id = id;
        this.r = r;
        this.g = g;
        this.b = b;
        this.type = provinceType.valueOf(type);
        this.coastal = coastal;
        this.terrain = terrain;
        this.continent = continent;
    }

    public ProvinceData(String line) {
        line = ParadoxParser.stripFromComments(line).trim();
        String[] values = line.split(";");
        if(values.length != 8) {
            throw new RuntimeException("ERROR: ProvinceData: invalid data format");
        }
        id = Integer.parseInt(values[0]);
        r = Integer.parseInt(values[1]);
        g = Integer.parseInt(values[2]);
        b = Integer.parseInt(values[3]);
        type = provinceType.valueOf(values[4].toUpperCase());
        coastal = Boolean.parseBoolean(values[5]);
        terrain = values[6];
        continent = Integer.parseInt(values[7]);
    }

    public int getRGB () {
        return r << 16 | g << 8 | b;
    }
    public Color getColor() {
        return new Color(r, g, b);
    }

    public int getContinent() {
        return continent;
    }
    public String getTerrain() {
        return terrain;
    }
    public boolean isCoastal() {
        return coastal;
    }
    public provinceType getType() {
        return type;
    }
    public int getB() {
        return b;
    }
    public int getG() {
        return g;
    }
    public int getR() {
        return r;
    }
    public int getId() {
        return id;
    }
}
