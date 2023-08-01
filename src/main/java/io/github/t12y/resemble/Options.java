package io.github.t12y.resemble;

public class Options {
    public int width;
    public int height;

    public int[][] ignoredBoxes; // array of boxes like [left, right, top, bottom]
    public int[] ignoreAreasColoredWith;
    public int[] errorPixelColor;

    public boolean ignoreColors;
    public boolean ignoreAntialiasing;
    public boolean compareOnly;

    public double redTolerance;
    public double greenTolerance;
    public double blueTolerance;
    public double alphaTolerance;
    public double minBrightness;
    public double maxBrightness;
    public double transparency;

    public ErrorType errorType;

    public Options() {
        this.errorPixelColor = new int[]{255, 0, 255, 255};
        this.errorType = ErrorType.Flat;
        this.transparency = 1;
    }

    ErrorPixelTransformer getErrorPixelTransformer(double[] baselinePixels, double[] latestPixels, double[] diffPixels) {
        switch (errorType) {
            case Movement:
                return new MovementErrorPixelTransformer(
                        errorPixelColor, baselinePixels, latestPixels, diffPixels);
            case FlatDifferenceIntensity:
                return new FlatDifferenceIntensityErrorPixelTransformer(
                        errorPixelColor, baselinePixels, latestPixels, diffPixels);
            case MovementDifferenceIntensity:
                return new MovementDifferenceIntensityErrorPixelTransformer(
                        errorPixelColor, baselinePixels, latestPixels, diffPixels);
            case DiffOnly:
                return new DiffOnlyErrorPixelTransformer(
                        errorPixelColor, baselinePixels, latestPixels, diffPixels);
            default:
                return new FlatErrorPixelTransformer(
                        errorPixelColor, baselinePixels, latestPixels, diffPixels);
        }
    }

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
