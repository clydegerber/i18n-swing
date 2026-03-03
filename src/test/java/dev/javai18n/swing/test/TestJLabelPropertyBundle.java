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
import dev.javai18n.swing.JLabelPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JLabelPropertyBundle
 */
public class TestJLabelPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        JLabelPropertyBundle bundle = new JLabelPropertyBundle();
        HashSet names = bundle.validNames();
        assertEquals(9, names.size());
        assertTrue(names.contains(JLabelPropertyBundle.TEXT));
        assertTrue(names.contains(JLabelPropertyBundle.MNEMONIC));
        assertTrue(names.contains(JLabelPropertyBundle.ICON));
        assertTrue(names.contains(JLabelPropertyBundle.DISABLED_ICON));
    }

    /**
     * Verify that all of the attributes added by AbstractButtonPropertyBundle can be set properly.
     */
    @Test
    public void testSetAttributes()
    {
        JLabelPropertyBundle bundle = new JLabelPropertyBundle();
        bundle.setAttribute(JLabelPropertyBundle.ICON, "/dev/javai18n/swing/test/MN_Flag.jpg");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(JLabelPropertyBundle.ICON));
        Icon icon = bundle.getIcon();
        assertNotNull(icon);
        bundle.setAttribute(JLabelPropertyBundle.DISABLED_ICON, "/dev/javai18n/swing/test/MN_Flag.jpg");
        assertEquals(2, bundle.size());
        assertEquals(true, bundle.containsKey(JLabelPropertyBundle.DISABLED_ICON));
        icon = bundle.getDisabledIcon();
        assertNotNull(icon);
        bundle.setAttribute(JLabelPropertyBundle.TEXT, "My text");
        assertEquals(3, bundle.size());
        assertEquals(true, bundle.containsKey(JLabelPropertyBundle.TEXT));
        String text = bundle.getText();
        assertEquals("My text", text);
        bundle.setAttribute(JLabelPropertyBundle.MNEMONIC, 76);
        assertEquals(4, bundle.size());
        assertEquals(true, bundle.containsKey(JLabelPropertyBundle.MNEMONIC));
        int mnemonic = bundle.getMnemonic();
        assertEquals(76, mnemonic);
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JLabelPropertyBundle bundle = new JLabelPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals("Unrecognized attribute name: foo", e.getMessage());
    }
}
