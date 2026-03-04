/*
 * Copyright 2026 Clyde Gerber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.javai18n.swing.test;

import java.awt.Image;
import java.util.HashSet;
import java.util.List;
import dev.javai18n.swing.WindowPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for WindowPropertyBundle
 */
public class TestWindowPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        WindowPropertyBundle bundle = new WindowPropertyBundle();
        HashSet names = bundle.validNames();
        assertEquals(5, names.size());
        assertTrue(names.contains(WindowPropertyBundle.ICON_IMAGES));
    }

    /**
     * Verify that the ICON_IMAGES property can be set properly from a String.
     */
    @Test
    public void testSetIconImageFromString()
    {
        WindowPropertyBundle bundle = new WindowPropertyBundle();
        bundle.setAttribute(WindowPropertyBundle.ICON_IMAGES, "/dev/javai18n/swing/test/folder.png");
        assertEquals(bundle.size(), 1);
        assertEquals(true, bundle.containsKey(WindowPropertyBundle.ICON_IMAGES));
        List<Image> images = bundle.getIconImages();
        assertEquals(1, images.size());
    }

    /**
     * Verify that the ICON_IMAGES property can be set properly from a String.
     */
    @Test
    public void testSetIconImageFromStringArray()
    {
        WindowPropertyBundle bundle = new WindowPropertyBundle();
        String[] array = {"dev/javai18n/swing/test/folder.png"};
        bundle.setAttribute(WindowPropertyBundle.ICON_IMAGES, array);
        assertEquals(bundle.size(), 1);
        assertEquals(true, bundle.containsKey(WindowPropertyBundle.ICON_IMAGES));
        List<Image> images = bundle.getIconImages();
        assertEquals(1, images.size());
    }

    /**
     * Verify the behavior of setting the ICON_IMAGES property from a String that cannot be resolved.
     */
    @Test
    public void testSetIconImageFromInvalidString()
    {
        WindowPropertyBundle bundle = new WindowPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class,
                ()->{bundle.setAttribute(WindowPropertyBundle.ICON_IMAGES, "foo");});
        assertEquals("Unable to load image at foo", e.getMessage());
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        WindowPropertyBundle bundle = new WindowPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals(e.getMessage(), "Unrecognized attribute name: foo");
    }
}
