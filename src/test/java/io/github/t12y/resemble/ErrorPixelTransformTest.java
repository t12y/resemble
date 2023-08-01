package io.github.t12y.resemble;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;

import static io.github.t12y.resemble.DiffImage.diff;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorPixelTransformTest {
    @ParameterizedTest
    @EnumSource(ErrorType.class)
    void testErrorPixelTransform(ErrorType errorType) throws IOException {
        Options options = Options.ignoreLess();
        options.errorPixelColor = new int[]{255, 255, 0, 255};
        options.errorType = errorType;
        String diffPath = "pixelErrorTransform/" +  errorType.name() + ".png";
        assertTrue(diff("ghost1.png", "ghost2.png", diffPath, options));
    }
}
