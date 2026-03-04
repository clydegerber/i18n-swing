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
import dev.javai18n.swing.AbstractButtonPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for AbstractButtonPropertyBundle
 */
public class TestAbstractButtonPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        AbstractButtonPropertyBundle bundle = new AbstractButtonPropertyBundle();
        HashSet names = bundle.validNames();
        assertEquals(15, names.size());
        assertTrue(names.contains(AbstractButtonPropertyBundle.TEXT));
        assertTrue(names.contains(AbstractButtonPropertyBundle.LABEL));
        assertTrue(names.contains(AbstractButtonPropertyBundle.MNEMONIC));
        assertTrue(names.contains(AbstractButtonPropertyBundle.ICON));
        assertTrue(names.contains(AbstractButtonPropertyBundle.DISABLED_ICON));
        assertTrue(names.contains(AbstractButtonPropertyBundle.DISABLED_SELECTED_ICON));
        assertTrue(names.contains(AbstractButtonPropertyBundle.PRESSED_ICON));
        assertTrue(names.contains(AbstractButtonPropertyBundle.ROLLOVER_ICON));
        assertTrue(names.contains(AbstractButtonPropertyBundle.ROLLOVER_SELECTED_ICON));
        assertTrue(names.contains(AbstractButtonPropertyBundle.SELECTED_ICON));
    }

    /**
     * Verify that all of the attributes added by AbstractButtonPropertyBundle can be set properly.
     */
    @Test
    public void testSetAttributes()
    {
        AbstractButtonPropertyBundle bundle = new AbstractButtonPropertyBundle();
        bundle.setAttribute(AbstractButtonPropertyBundle.ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.ICON));
        Icon icon = bundle.getIcon();
        assertNotNull(icon);
        bundle.setAttribute(AbstractButtonPropertyBundle.DISABLED_ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(2, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.DISABLED_ICON));
        icon = bundle.getDisabledIcon();
        assertNotNull(icon);
        bundle.setAttribute(AbstractButtonPropertyBundle.DISABLED_SELECTED_ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(3, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.DISABLED_SELECTED_ICON));
        icon = bundle.getDisabledSelectedIcon();
        assertNotNull(icon);
        bundle.setAttribute(AbstractButtonPropertyBundle.ROLLOVER_ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(4, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.ROLLOVER_ICON));
        icon = bundle.getRolloverIcon();
        assertNotNull(icon);
        bundle.setAttribute(AbstractButtonPropertyBundle.ROLLOVER_SELECTED_ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(5, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.ROLLOVER_SELECTED_ICON));
        icon = bundle.getRolloverSelectedIcon();
        assertNotNull(icon);
        bundle.setAttribute(AbstractButtonPropertyBundle.PRESSED_ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(6, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.PRESSED_ICON));
        icon = bundle.getPressedIcon();
        assertNotNull(icon);
        bundle.setAttribute(AbstractButtonPropertyBundle.SELECTED_ICON, "/dev/javai18n/swing/test/folder.png");
        assertEquals(7, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.SELECTED_ICON));
        icon = bundle.getSelectedIcon();
        assertNotNull(icon);
        bundle.setAttribute(AbstractButtonPropertyBundle.TEXT, "My text");
        assertEquals(8, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.TEXT));
        String text = bundle.getText();
        assertEquals("My text", text);
        bundle.setAttribute(AbstractButtonPropertyBundle.LABEL, "My label");
        assertEquals(9, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.LABEL));
        String label = bundle.getLabel();
        assertEquals("My label", label);
        bundle.setAttribute(AbstractButtonPropertyBundle.MNEMONIC, 76);
        assertEquals(10, bundle.size());
        assertEquals(true, bundle.containsKey(AbstractButtonPropertyBundle.MNEMONIC));
        int mnemonic = bundle.getMnemonic();
        assertEquals(76, mnemonic);
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        AbstractButtonPropertyBundle bundle = new AbstractButtonPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals("Unrecognized attribute name: foo", e.getMessage());
    }
}
