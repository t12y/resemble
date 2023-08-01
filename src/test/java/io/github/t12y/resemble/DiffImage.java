package io.github.t12y.resemble;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class DiffImage {
    private static final String ASSETS_PATH = "assets/";

    public double[] baselinePixels;
    public double[] latestPixels;
    public int height;
    public int width;

    private DiffImage(InputStream baseline, InputStream latest) throws IOException {
        BufferedImage baselineImage = ImageIO.read(baseline);
        BufferedImage latestImage = ImageIO.read(latest);

        this.height = baselineImage.getHeight();
        this.width = baselineImage.getWidth();

        this.baselinePixels = unpackPixels(baselineImage.getRGB(0, 0, width, height, null, 0, width));
        this.latestPixels = unpackPixels(latestImage.getRGB(0, 0, width, height, null, 0, width));
    }

    public static Result resemble(String baselinePath, String latestPath, Options options) throws IOException {
        ClassLoader cl = DiffImage.class.getClassLoader();
        DiffImage d;

        try (
            InputStream baseline = cl.getResource(ASSETS_PATH + baselinePath).openStream();
            InputStream latest = cl.getResource(ASSETS_PATH + latestPath).openStream();
        ) {
            d = new DiffImage(baseline, latest);
        }

        options.width = d.width;
        options.height = d.height;

        return Resemble.analyzeImages(d.baselinePixels, d.latestPixels, options);
    }

    public static boolean diff(String baselinePath, String latestPath, String diffPath, Options options) throws IOException {
        double[] expectedPixels;
        try (
            InputStream diff = DiffImage.class.getClassLoader().getResource(ASSETS_PATH + diffPath).openStream();
        ) {
            BufferedImage diffImage = ImageIO.read(diff);
            int height = diffImage.getHeight();
            int width = diffImage.getWidth();

            expectedPixels = unpackPixels(diffImage.getRGB(0, 0, width, height, null, 0, width));
        }


        double[] actualPixels = resemble(baselinePath, latestPath, options).diffPixels;

        return Arrays.equals(expectedPixels, actualPixels);
    }

    public static double compare(String baselinePath, String latestPath, Options options) throws IOException {
        return round(resemble(baselinePath, latestPath, options).mismatchedPercent);
    }

    private static double round(double d) {
        return Math.round(d * 100.0) / 100.0;
    }

    private static double[] unpackPixels(int[] packed) {
        int packedLength = packed.length;
        double[] unpacked = new double[packedLength * 4];
        int unpackedIndex;
        int packedPixel;

        for (int i = 0; i < packedLength; i++) {
            packedPixel = packed[i];
            unpackedIndex = i * 4;

            unpacked[unpackedIndex    ] = 0xff & (packedPixel >> 16);
            unpacked[unpackedIndex + 1] = 0xff & (packedPixel >> 8);
            unpacked[unpackedIndex + 2] = 0xff & packedPixel;
            unpacked[unpackedIndex + 3] = 0xff & (packedPixel >>> 24);
        }

        return unpacked;
    }
}