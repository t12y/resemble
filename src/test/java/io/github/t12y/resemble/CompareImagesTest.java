package io.github.t12y.resemble;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.t12y.resemble.DiffImage.compare;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompareImagesTest {
    @Test
    void testBasicComparison() throws IOException {
        assertEquals(8.66, compare("People.jpg", "People2.jpg", Options.ignoreLess()));
    }
}