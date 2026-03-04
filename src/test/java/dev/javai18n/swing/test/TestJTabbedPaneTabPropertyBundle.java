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

import java.util.HashSet;
import javax.swing.Icon;
import dev.javai18n.swing.JTabbedPaneTabPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JTabbedPaneTabPropertyBundle.
 */
public class TestJTabbedPaneTabPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        JTabbedPaneTabPropertyBundle bundle = new JTabbedPaneTabPropertyBundle();
        HashSet names = bundle.validNames();
        assertEquals(9, names.size());
        assertTrue(names.contains(JTabbedPaneTabPropertyBundle.TITLE));
        assertTrue(names.contains(JTabbedPaneTabPropertyBundle.ICON));
        assertTrue(names.contains(JTabbedPaneTabPropertyBundle.DISABLED_ICON));
        assertTrue(names.contains(JTabbedPaneTabPropertyBundle.MNEMONIC));
    }

    /**
     * Verify that all attributes can be set properly.
     */
    @Test
    public void testSetAttribute()
    {
        JTabbedPaneTabPropertyBundle bundle = new JTabbedPaneTabPropertyBundle();
        bundle.setAttribute(JTabbedPaneTabPropertyBundle.TITLE, "Tab Title");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(JTabbedPaneTabPropertyBundle.TITLE));
        String title = bundle.getTitle();
        assertEquals("Tab Title", title);
        bundle.setAttribute(JTabbedPaneTabPropertyBundle.MNEMONIC, 84);
        assertEquals(2, bundle.size());
        assertEquals(true, bundle.containsKey(JTabbedPaneTabPropertyBundle.MNEMONIC));
        int mnemonic = bundle.getMnemonic();
        assertEquals(84, mnemonic);
        bundle.setAttribute(JTabbedPaneTabPropertyBundle.ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(3, bundle.size());
        assertEquals(true, bundle.containsKey(JTabbedPaneTabPropertyBundle.ICON));
        Icon icon = bundle.getIcon();
        assertNotNull(icon);
        bundle.setAttribute(JTabbedPaneTabPropertyBundle.DISABLED_ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(4, bundle.size());
        assertEquals(true, bundle.containsKey(JTabbedPaneTabPropertyBundle.DISABLED_ICON));
        Icon disabledIcon = bundle.getDisabledIcon();
        assertNotNull(disabledIcon);
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JTabbedPaneTabPropertyBundle bundle = new JTabbedPaneTabPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals("Unrecognized attribute name: foo", e.getMessage());
    }
}
