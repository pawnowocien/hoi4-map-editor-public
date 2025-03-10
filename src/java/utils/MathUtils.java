package src.java.utils;

import java.util.Random;

public class MathUtils {
    public static Random generator = new Random();

    public static int getRandomInt(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }
    public static int getRandomRGB() {
        return generator.nextInt(255 * 255 * 255);
    }
    public static int clampInt(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}
