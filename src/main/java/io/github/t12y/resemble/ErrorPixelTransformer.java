package io.github.t12y.resemble;

public interface ErrorPixelTransformer {
    void transform(int offset);
}

abstract class BaseErrorPixelTransformer implements ErrorPixelTransformer {
    protected int[] errorPixelColor;
    protected double[] baselinePixels;
    protected double[] latestPixels;
    protected double[] diffPixels;

    public BaseErrorPixelTransformer(
            int[] errorPixelColor, double[] baselinePixels, double[] latestPixels, double[] diffPixels) {
        this.errorPixelColor = errorPixelColor;
        this.baselinePixels = baselinePixels;
        this.latestPixels = latestPixels;
        this.diffPixels = diffPixels;
    }

    double colorsDistance(int offset) {
        double rDist = Math.abs(baselinePixels[offset    ] - latestPixels[offset    ]);
        double gDist = Math.abs(baselinePixels[offset + 1] - latestPixels[offset + 1]);
        double bDist = Math.abs(baselinePixels[offset + 2] - latestPixels[offset + 2]);
        return Math.round((rDist + gDist + bDist) / 3);
    }
}

class FlatErrorPixelTransformer extends BaseErrorPixelTransformer {
    public FlatErrorPixelTransformer(
            int[] errorPixelColor, double[] baselinePixels, double[] latestPixels, double[] diffPixels) {
        super(errorPixelColor, baselinePixels, latestPixels, diffPixels);
    }

    @Override
    public void transform(int offset) {
        diffPixels[offset    ] = errorPixelColor[0];
        diffPixels[offset + 1] = errorPixelColor[1];
        diffPixels[offset + 2] = errorPixelColor[2];
        diffPixels[offset + 3] = errorPixelColor[3];
    }
}

class MovementErrorPixelTransformer extends BaseErrorPixelTransformer {
    public MovementErrorPixelTransformer(
            int[] errorPixelColor, double[] baselinePixels, double[] latestPixels, double[] diffPixels) {
        super(errorPixelColor, baselinePixels, latestPixels, diffPixels);
    }

    @Override
    public void transform(int offset) {
        diffPixels[offset    ] = Math.round((latestPixels[offset    ] * ((double) errorPixelColor[0] / 255) + errorPixelColor[0]) / 2);
        diffPixels[offset + 1] = Math.round((latestPixels[offset + 1] * ((double) errorPixelColor[1] / 255) + errorPixelColor[1]) / 2);
        diffPixels[offset + 2] = Math.round((latestPixels[offset + 2] * ((double) errorPixelColor[2] / 255) + errorPixelColor[2]) / 2);
        diffPixels[offset + 3] = Math.round(latestPixels[offset + 3]);
    }
}

class FlatDifferenceIntensityErrorPixelTransformer extends BaseErrorPixelTransformer {
    public FlatDifferenceIntensityErrorPixelTransformer(
            int[] errorPixelColor, double[] baselinePixels, double[] latestPixels, double[] diffPixels) {
        super(errorPixelColor, baselinePixels, latestPixels, diffPixels);
    }

    @Override
    public void transform(int offset) {
        diffPixels[offset    ] = errorPixelColor[0];
        diffPixels[offset + 1] = errorPixelColor[1];
        diffPixels[offset + 2] = errorPixelColor[2];
        diffPixels[offset + 3] = colorsDistance(offset);
    }
}

class MovementDifferenceIntensityErrorPixelTransformer extends BaseErrorPixelTransformer {
    public MovementDifferenceIntensityErrorPixelTransformer(
            int[] errorPixelColor, double[] baselinePixels, double[] latestPixels, double[] diffPixels) {
        super(errorPixelColor, baselinePixels, latestPixels, diffPixels);
    }

    @Override
    public void transform(int offset) {
        double ratio = (colorsDistance(offset) / 255) * 0.8;
        diffPixels[offset    ] = Math.round((1 - ratio) * (latestPixels[offset    ] * ((double) errorPixelColor[0] / 255)) + ratio * errorPixelColor[0]);
        diffPixels[offset + 1] = Math.round((1 - ratio) * (latestPixels[offset + 1] * ((double) errorPixelColor[1] / 255)) + ratio * errorPixelColor[1]);
        diffPixels[offset + 2] = Math.round((1 - ratio) * (latestPixels[offset + 2] * ((double) errorPixelColor[2] / 255)) + ratio * errorPixelColor[2]);
        diffPixels[offset + 3] = Math.round(latestPixels[offset + 3]);
    }
}

class DiffOnlyErrorPixelTransformer extends BaseErrorPixelTransformer {
    public DiffOnlyErrorPixelTransformer(
            int[] errorPixelColor, double[] baselinePixels, double[] latestPixels, double[] diffPixels) {
        super(errorPixelColor, baselinePixels, latestPixels, diffPixels);
    }

    @Override
    public void transform(int offset) {
        diffPixels[offset    ] = latestPixels[offset    ];
        diffPixels[offset + 1] = latestPixels[offset + 1];
        diffPixels[offset + 2] = latestPixels[offset + 2];
        diffPixels[offset + 3] = latestPixels[offset + 3];
    }
}