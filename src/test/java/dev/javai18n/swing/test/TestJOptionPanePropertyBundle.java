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
import dev.javai18n.swing.JOptionPanePropertyBundle;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JOptionPanePropertyBundle.
 */
public class TestJOptionPanePropertyBundle
{
    /**
     * Verify that the set of valid attribute names includes the TITLE and OPTIONS keys in addition
     * to those inherited from JComponentPropertyBundle.
     */
    @Test
    public void testValidNames()
    {
        JOptionPanePropertyBundle bundle = new JOptionPanePropertyBundle();
        Set<String> names = bundle.validNames();
        // JComponentPropertyBundle contributes 5 names (Font, Name, ToolTipText,
        // AccessibleName, AccessibleDescription); this class adds TITLE and OPTIONS = 7.
        assertEquals(7, names.size());
        assertTrue(names.contains(JOptionPanePropertyBundle.TITLE));
        assertTrue(names.contains(JOptionPanePropertyBundle.OPTIONS));
    }

    /**
     * Verify that TITLE and OPTIONS can be set via setAttribute and retrieved via their getters.
     */
    @Test
    public void testSetAttribute()
    {
        JOptionPanePropertyBundle bundle = new JOptionPanePropertyBundle();
        bundle.setAttribute(JOptionPanePropertyBundle.TITLE, "My Dialog");
        bundle.setAttribute(JOptionPanePropertyBundle.OPTIONS, new String[]{"Yes", "No"});
        assertEquals("My Dialog", bundle.getTitle());
        assertArrayEquals(new String[]{"Yes", "No"}, bundle.getOptions());
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JOptionPanePropertyBundle bundle = new JOptionPanePropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> bundle.setAttribute("UnknownKey", "value"));
        assertEquals("Unrecognized attribute name: UnknownKey", e.getMessage());
    }
}
