package io.github.t12y.resemble;

public class Options {
    public int width;
    public int height;

    public int[][] ignoredBoxes; // array of boxes like [left, right, top, bottom]
    public int[] ignoreAreasColoredWith;

    public boolean ignoreColors;
    public boolean ignoreAntialiasing;

    public double redTolerance;
    public double greenTolerance;
    public double blueTolerance;
    public double alphaTolerance;
    public double minBrightness;
    public double maxBrightness;

    public static Options ignoreNothing() {
        Options options = new Options();
        options.maxBrightness = 255.0;

        return options;
    }

    public static Options ignoreLess() {
        Options options = new Options();
        options.redTolerance = 16.0;
        options.greenTolerance = 16.0;
        options.blueTolerance = 16.0;
        options.alphaTolerance = 16.0;
        options.minBrightness = 16.0;
        options.maxBrightness = 240.0;

        return options;
    }

    public static Options ignoreAntialiasing() {
        Options options = new Options();
        options.redTolerance = 32.0;
        options.greenTolerance = 32.0;
        options.blueTolerance = 32.0;
        options.alphaTolerance = 32.0;
        options.minBrightness = 64.0;
        options.maxBrightness = 96.0;
        options.ignoreAntialiasing = true;

        return options;
    }

    public static Options ignoreColors() {
        Options options = new Options();
        options.alphaTolerance = 16.0;
        options.minBrightness = 16.0;
        options.maxBrightness = 240.0;
        options.ignoreColors = true;

        return options;
    }

    public static Options ignoreAlpha() {
        Options options = new Options();
        options.redTolerance = 16.0;
        options.greenTolerance = 16.0;
        options.blueTolerance = 16.0;
        options.alphaTolerance = 255.0;
        options.minBrightness = 16.0;
        options.maxBrightness = 240.0;

        return options;
    }
}
