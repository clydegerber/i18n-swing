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

import java.awt.Font;
import java.util.Set;
import dev.javai18n.swing.ComponentPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ComponentPropertyBundle
 */
public class TestComponentPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        ComponentPropertyBundle bundle = new ComponentPropertyBundle();
        Set<String> names = bundle.validNames();
        assertEquals(4, names.size());
        assertTrue(names.contains(ComponentPropertyBundle.NAME));
        assertTrue(names.contains(ComponentPropertyBundle.FONT));
        assertTrue(names.contains(ComponentPropertyBundle.ACCESSIBLE_NAME));
        assertTrue(names.contains(ComponentPropertyBundle.ACCESSIBLE_DESCRIPTION));
    }

    /**
     * Verify that setAttribute() function as expected with valid input.
     */
    @Test
    public void testSetAttribute()
    {
        ComponentPropertyBundle bundle = new ComponentPropertyBundle();
        bundle.setAttribute(ComponentPropertyBundle.NAME, "foo");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(ComponentPropertyBundle.NAME));
        assertEquals("foo", bundle.get(ComponentPropertyBundle.NAME));
        assertEquals("foo", bundle.getName());
        bundle.setAttribute(ComponentPropertyBundle.ACCESSIBLE_NAME, "bar");
        assertEquals(2, bundle.size());
        assertEquals(true, bundle.containsKey(ComponentPropertyBundle.ACCESSIBLE_NAME));
        assertEquals("bar", bundle.get(ComponentPropertyBundle.ACCESSIBLE_NAME));
        assertEquals("bar", bundle.getAccessibleName());
        bundle.setAttribute(ComponentPropertyBundle.ACCESSIBLE_DESCRIPTION, "desc");
        assertEquals(3, bundle.size());
        assertEquals(true, bundle.containsKey(ComponentPropertyBundle.ACCESSIBLE_DESCRIPTION));
        assertEquals("desc", bundle.get(ComponentPropertyBundle.ACCESSIBLE_DESCRIPTION));
        assertEquals("desc", bundle.getAccessibleDescription());
        Font font = Font.decode("Arial-BOLD-28");
        bundle.setAttribute(ComponentPropertyBundle.FONT, font);
        assertEquals(4, bundle.size());
        assertEquals(true, bundle.containsKey(ComponentPropertyBundle.FONT));
        assertEquals(font, bundle.get(ComponentPropertyBundle.FONT));
        assertEquals(font, bundle.getFont());
    }

    /**
     * Verify that the FONT attribute can be set via a String.
     */
    @Test
    public void testFontFromString()
    {
        ComponentPropertyBundle bundle = new ComponentPropertyBundle();
        bundle.setAttribute(ComponentPropertyBundle.FONT, "Arial-BOLD-28");
        Font font = Font.decode("Arial-BOLD-28");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(ComponentPropertyBundle.FONT));
        assertEquals(font, bundle.get(ComponentPropertyBundle.FONT));
        assertEquals(font, bundle.getFont());
    }

    /**
     * Verify that an invalid Font string produces a Font in the "Dialog" family with PLAIN style and size 12.
     */
    @Test
    public void testFontFromInvalidString()
    {
        ComponentPropertyBundle bundle = new ComponentPropertyBundle();
        bundle.setAttribute(ComponentPropertyBundle.FONT, "&$*^@");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(ComponentPropertyBundle.FONT));
        Font font = bundle.getFont();
        assertEquals(12, font.getSize());
        assertTrue(font.isPlain());
        assertEquals("Dialog", font.getFamily());
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        ComponentPropertyBundle bundle = new ComponentPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals("Unrecognized attribute name: foo", e.getMessage());
    }

    /**
     * Verify that passing an Object to an attribute that expects a String produces a ClassCastException.
     */
    @Test
    public void testInvalidCast()
    {
        Object o = new Object();
        ComponentPropertyBundle bundle = new ComponentPropertyBundle();
        Exception e = assertThrows(ClassCastException.class,
                ()->{bundle.setAttribute(ComponentPropertyBundle.NAME, o);});
        assertEquals("class java.lang.Object cannot be cast to class java.lang.String " +
                "(java.lang.Object and java.lang.String are in module java.base of loader 'bootstrap')",
                e.getMessage());
    }
}
