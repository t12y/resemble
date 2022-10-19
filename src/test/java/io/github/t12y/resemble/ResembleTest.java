package io.github.t12y.resemble;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.t12y.resemble.DiffImage.compare;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResembleTest {
    @Test
    void testPartialDiffWithIgnoredBoxes() throws IOException {
        Options options = Options.ignoreLess();
        options.ignoredBoxes = new int[][]{
                {20, 350, 20, 80},
                {20, 350, 200, 250}
        };

        assertEquals(1.80, compare("text.png", "textAa.png", options));
    }

    @Test
    void testPartialDiffWithSingleIgnoredBox() throws IOException {
        Options options = Options.ignoreLess();
        options.ignoredBoxes = new int[][]{{20, 350, 20, 80}};

        assertEquals(3.52, compare("text.png", "textAa.png", options));
    }

    @Test
    void testPartialDiffWithIgnoredColor() throws IOException {
        Options options = Options.ignoreLess();
        options.ignoreAreasColoredWith = new int[]{255, 0, 0, 255};

        assertEquals(5.5, compare("People2.jpg", "PeopleWithIgnoreMask.png", options));
    }
}
