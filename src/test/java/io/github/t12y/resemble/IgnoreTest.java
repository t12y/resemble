package io.github.t12y.resemble;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.t12y.resemble.DiffImage.compare;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IgnoreTest {
    @Test
    void testIgnoreAntialiasing() throws IOException {
        assertEquals(0.0, compare("text.png", "textAa.png", Options.ignoreAntialiasing()));
    }

    @Test
    void testIgnoreAntialiasingWithCustomTolerance() throws IOException {
        Options options = new Options();
        options.ignoreAntialiasing = true;
        options.redTolerance = 16.0;
        options.greenTolerance = 16.0;
        options.blueTolerance = 16.0;

        assertEquals(100.0, compare("square1.png", "square2.png", options));
    }

    @Test
    void testIgnoreAntialiasingOff() throws IOException {
        assertEquals(5.19, compare("text.png", "textAa.png", Options.ignoreLess()));
    }
}
