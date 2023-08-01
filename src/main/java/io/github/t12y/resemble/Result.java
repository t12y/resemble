package io.github.t12y.resemble;

import java.awt.image.BufferedImage;

public class Result {
    private final int width;
    private final int height;

    public final double mismatchedPercent;
    public final double[] diffPixels;

    public Result(int width, int height, double mismatchedPercent, double[] diffPixels) {
        this.width = width;
        this.height = height;
        this.mismatchedPercent = mismatchedPercent;
        this.diffPixels = diffPixels;
    }

    public BufferedImage toImage() {
        if (diffPixels == null) return null;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < diffPixels.length; i += 4) {
            int red = (int)diffPixels[i];
            int green = (int)diffPixels[i + 1];
            int blue = (int)diffPixels[i + 2];
            int alpha = (int)diffPixels[i + 3];

            int column = (i / 4) % width;
            int row = (i / 4) / width;
            int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

            image.setRGB(column, row, rgb);
        }

        return image;
    }
}
