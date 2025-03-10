package src.java.parser;

// This is such a stupid class
// WHY DID THEY HAVE TO MAKE THE FILES THIS WAY???

// cool, hsv in files is not the real hsv, so conversion to rgb sucks

import src.java.structure.CountryColor;

public class ParserColor implements ParadoxParserPart {
    private final String type;
    private final CountryColor countryColor;



    public ParserColor(String type, String s, String c1, String c2, String c3){
        this.type = type;

        if (s.equalsIgnoreCase("rgb")) {
            countryColor = new CountryColor(Integer.parseInt(c1), Integer.parseInt(c2), Integer.parseInt(c3));
            return;
        }
        if (s.equalsIgnoreCase("hsv")) {
            countryColor = new CountryColor(Float.parseFloat(c1), Float.parseFloat(c2), Float.parseFloat(c3));
            return;
        }
        throw new RuntimeException("Color type not supported"); // TODO better exception
    }
    public ParserColor(String type, String c1, String c2, String c3){
        this(type, "rgb", c1, c2, c3);
    }
    public ParserColor(String type, String s, ParserList pl){
        this(type, s, pl.getList().get(0).toString(), pl.getList().get(1).toString(), pl.getList().get(2).toString());
    }
    public ParserColor(String type, ParserList pl){
        this(type, "rgb", pl);
    }

    public int getRGB() {
        return countryColor.getRGB();
    }
    public CountryColor getCountryColor() {
        return countryColor;
    }
    public boolean isRGB() {
        return countryColor.isRGB();
    }
    public boolean isHSV() {
        return countryColor.isHSV();
    }
    public String getType() {
        return type;
    }

    public String toString() {
        return String.format("%s = %s", type, countryColor.toString());
    }

    @Override
    public int size() {
        return 0;
    }
}
