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

import java.util.Set;
import dev.javai18n.swing.JComboBoxPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JComboBoxPropertyBundle
 */
public class TestJComboBoxPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        JComboBoxPropertyBundle bundle = new JComboBoxPropertyBundle();
        Set<String> names = bundle.validNames();
        assertEquals(6, names.size());
        assertTrue(names.contains(JComboBoxPropertyBundle.VALUES));
    }

    /**
     * Verify that all of the attributes added by JCombBoxPropertyBundle can be set properly.
     */
    @Test
    public void testSetAttributes()
    {
        JComboBoxPropertyBundle bundle = new JComboBoxPropertyBundle();
        String[] values = {"foo", "bar", "baz"};
        bundle.setAttribute(JComboBoxPropertyBundle.VALUES, values);
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(JComboBoxPropertyBundle.VALUES));
        String[] actual = bundle.getValues();
        assertNotNull(actual);
        assertEquals(values.length, actual.length);
        assertArrayEquals(values, actual);
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JComboBoxPropertyBundle bundle = new JComboBoxPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals("Unrecognized attribute name: foo", e.getMessage());
    }
}
