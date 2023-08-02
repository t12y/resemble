package io.github.t12y.resemble;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.t12y.resemble.DiffImage.compare;
import static io.github.t12y.resemble.DiffImage.diff;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompareImagesTest {
    @Test
    void testBasicComparison() throws IOException {
        assertEquals(8.66, compare("People.jpg", "People2.jpg", Options.ignoreLess()));
        assertTrue(diff("People.jpg", "People2.jpg", "PeopleComparedToPeople2.png", Options.ignoreLess()));
    }

    @Test
    void testIdentical() throws IOException {
        assertEquals(0.0, compare("People.jpg", "People.jpg", Options.ignoreNothing()));
        assertTrue(diff("People.jpg", "People.jpg", "People.jpg", Options.ignoreNothing()));
    }
}