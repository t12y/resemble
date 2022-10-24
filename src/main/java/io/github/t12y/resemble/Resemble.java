package io.github.t12y.resemble;

import java.lang.Math;

public class Resemble {
    private static final int ANTIALIASING_DISTANCE = 1;

    private final double[] baselinePixels;
    private final double[] latestPixels;
    private final Options options;

    private Resemble(double[] baselinePixels, double[] latestPixels, Options options) {
        this.baselinePixels = baselinePixels;
        this.latestPixels = latestPixels;
        this.options = options;
    }

    public static double analyzeImages(double[] baselinePixels, double[] latestPixels, Options options) {
        return new Resemble(baselinePixels, latestPixels, options).analyzeImages();
    }

    private double analyzeImages() {
        int mismatchCount = 0;
        int offset;

        for (int x = 0; x < options.width; x++) {
            for (int y = 0; y < options.height; y++) {
                offset = (y * options.width + x) * 4;

                if (offset > baselinePixels.length || !withinComparedArea(x, y) || isIgnoredColor(offset)) {
                    continue;
                }

                if (options.ignoreColors) {
                    if (!isPixelBrightnessSimilar(offset)) mismatchCount++;
                    continue;
                }

                if (!isRGBSimilar(offset)) {
                    if (options.ignoreAntialiasing && (isAntialiased(offset, baselinePixels, x, y) || isAntialiased(offset, latestPixels, x, y))) {
                        if (isPixelBrightnessSimilar(offset)) continue;
                    }

                    mismatchCount++;
                }
            }
        }

        return ((double)mismatchCount / (options.height * options.width)) * 100.0;
    }

    private boolean withinComparedArea(int x, int y) {
        if (options.ignoredBoxes == null) return true;

        for (int[] box : options.ignoredBoxes) {
            if (x >= box[0] && x <= box[1] && y >= box[2] && y <= box[3]) {
                return false;
            }
        }

        return true;
    }

    private boolean isIgnoredColor(int offset) {
        if (options.ignoreAreasColoredWith == null) return false;

        double rDifference = Math.abs(latestPixels[offset] - options.ignoreAreasColoredWith[0]);
        double gDifference = Math.abs(latestPixels[offset + 1] - options.ignoreAreasColoredWith[1]);
        double bDifference = Math.abs(latestPixels[offset + 2] - options.ignoreAreasColoredWith[2]);
        double avgDifference = (rDifference + gDifference + bDifference) / 3;

        return avgDifference == 0;
    }

    private boolean isRGBSimilar(int offset) {
        boolean isSimilar = isColorSimilar(baselinePixels[offset], latestPixels[offset], options.redTolerance);
        isSimilar = isSimilar && isColorSimilar(baselinePixels[offset + 1], latestPixels[offset + 1], options.greenTolerance);
        isSimilar = isSimilar && isColorSimilar(baselinePixels[offset + 2], latestPixels[offset + 2], options.blueTolerance);
        isSimilar = isSimilar && isColorSimilar(baselinePixels[offset + 3], latestPixels[offset + 3], options.alphaTolerance);

        return isSimilar;
    }

    private boolean isRGBSame(int baselineOffset, int latestOffset, double[] pixels) {
        boolean isSame = pixels[baselineOffset] == pixels[latestOffset];
        isSame = isSame && pixels[baselineOffset + 1] == pixels[latestOffset + 1];
        isSame = isSame && pixels[baselineOffset + 2] == pixels[latestOffset + 2];

        return isSame;
    }

    private static boolean isColorSimilar(double baselineColor, double latestColor, double tolerance) {
        if (baselineColor == latestColor) return true;
        return Math.abs(baselineColor - latestColor) < tolerance;
    }

    private static double getBrightness(int offset, double[] pixels) {
        return 0.3 * pixels[offset] + 0.59 * pixels[offset + 1] + 0.11 * pixels[offset + 2];
    }

    private boolean isPixelBrightnessSimilar(int offset) {
        double baselineBrightness = getBrightness(offset, baselinePixels);
        double latestBrightness = getBrightness(offset, latestPixels);

        boolean isSimilar = isColorSimilar(baselinePixels[offset + 3], latestPixels[offset + 3], options.alphaTolerance);
        isSimilar = isSimilar && isColorSimilar(baselineBrightness, latestBrightness, options.minBrightness);

        return isSimilar;
    }

    private static double getHue(int offset, double[] pixels) {
        double r = pixels[offset] / 255.0;
        double g = pixels[offset + 1] / 255.0;
        double b = pixels[offset + 2] / 255;
        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        double h = 0;
        double d;

        if (max == min) {
            h = 0.0; // achromatic
        } else {
            d = max - min;
            if (max == r) {
                h = (g - b) / d + (g < b ? 6.0 : 0.0);
            } else if (max == g) {
                h = (b - r) / d + 2.0;
            } else if (max == b) {
                h = (r - g) / d + 4.0;
            } else {
                h /= 6.0;
            }
        }

        return h;
    }

    private boolean isAntialiased(int sourceOffset, double[] pixels, int x, int y) {
        int hasSiblingWithDifferentHue = 0;
        int hasHighContrastSibling = 0;
        int hasEquivalentSibling = 0;
        int targetOffset;

        final double sourceBrightness = getBrightness(sourceOffset, pixels);
        final double sourceHue = getHue(sourceOffset, pixels);

        for (int i = ANTIALIASING_DISTANCE * -1; i <= ANTIALIASING_DISTANCE; i++) {
            for (int j = ANTIALIASING_DISTANCE * -1; j <= ANTIALIASING_DISTANCE; j++) {
                if (i == 0 && j == 0) continue;

                targetOffset = ((x + j) * options.width + (y + i)) * 4;
                if (targetOffset > pixels.length || targetOffset < 0) {
                    continue;
                }

                double targetBrightness = getBrightness(targetOffset, pixels);
                double targetHue = getHue(targetOffset, pixels);

                if (Math.abs(sourceBrightness - targetBrightness) > options.maxBrightness) {
                    hasHighContrastSibling++;
                }

                if (isRGBSame(sourceOffset, targetOffset, pixels)) {
                    hasEquivalentSibling++;
                }

                if (Math.abs(targetHue - sourceHue) > 0.3) {
                    hasSiblingWithDifferentHue++;
                }

                if (hasSiblingWithDifferentHue > 1 || hasHighContrastSibling > 1) {
                    return true;
                }
            }
        }

        return hasEquivalentSibling < 2;
    }
}

