package src.java.structure;

// hsv in files doesn't translate to the real hsv, so conversion to rgb sucks - thanks paradox
// fortunately doesn't change too much here, it'll be ignored
// bruh it's not only hsv - rgb can also be unreliable
// TODO do sth about it
// ^ point stands

import java.awt.*;

public class CountryColor {
    private final boolean isRGB;
    private final int rgb;
    private final float[] hsv;

    public CountryColor(int r, int g, int b){
        rgb = r << 16 | g << 8 | b;
        hsv = new float[3];
        Color.RGBtoHSB(r,g,b,hsv);
        isRGB = true;
    }
    public CountryColor(float h, float s, float v){
        hsv = new float[]{h,s,v};
        rgb = Color.HSBtoRGB(h,s * 0.6f,v * 0.8f) & 0xFFFFFF;
        isRGB = false;
    }

    public int getRGB() {
        return rgb;
    }
    public float[] getHSV() {
        return hsv;
    }
    public boolean isRGB() {
        return isRGB;
    }
    public boolean isHSV() {
        return !isRGB;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isRGB) {
            sb.append("{").append(rgb >> 16).append(" ").append((rgb >> 8) & 0xFF).append(" ").append(rgb & 0xFF).append("}");
        } else {
            sb.append("hsv {").append(hsv[0]).append(" ").append(hsv[1]).append(" ").append(hsv[2]).append("}");
        }
        return sb.toString();
    }
}
