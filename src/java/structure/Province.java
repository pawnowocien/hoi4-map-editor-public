package src.java.structure;

import src.java.parser.ProvinceData;

public class Province implements Comparable<Province> {
    private int id;
    private int colorRGB;
    private provinceType type;
    private Boolean coastal;
    private String terrain;
    private int continent;
    private State state;

    public Province(ProvinceData provinceData) {
        id = provinceData.getId();
        colorRGB = provinceData.getRGB();
        type = provinceData.getType();
        coastal = provinceData.isCoastal();
        terrain = provinceData.getTerrain();
        continent = provinceData.getContinent();
    }

    public int getId() {
        return id;
    }
    public int getColorRGB() {
        return colorRGB;
    }
    public String getType() {
        return type.name();
    }
    public Boolean getCoastal() {
        return coastal;
    }
    public String getTerrain() {
        return terrain;
    }
    public int getContinent() {
        return continent;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public int compareTo(Province o) {
        return (int)Math.signum(id - o.id);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Color: ").append(colorRGB).append("\n");
        sb.append("Type: ").append(type).append("\n");
        sb.append("Coastal: ").append(coastal).append("\n");
        sb.append("Terrain: ").append(terrain).append("\n");
        sb.append("Continent: ").append(continent).append("\n");
        return sb.toString();
    }
}
