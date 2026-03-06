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
import dev.javai18n.swing.JFileChooserPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JFileChooserPropertyBundle.
 */
public class TestJFileChooserPropertyBundle
{
    /**
     * Verify that the set of valid attribute names includes DIALOG_TITLE, APPROVE_BUTTON_TEXT, and
     * APPROVE_BUTTON_TOOLTIP in addition to those inherited from JComponentPropertyBundle (5 names).
     * Total expected: 5 + 3 = 8.
     */
    @Test
    public void testValidNames()
    {
        JFileChooserPropertyBundle bundle = new JFileChooserPropertyBundle();
        Set<String> names = bundle.validNames();
        assertEquals(8, names.size());
        assertTrue(names.contains(JFileChooserPropertyBundle.DIALOG_TITLE));
        assertTrue(names.contains(JFileChooserPropertyBundle.APPROVE_BUTTON_TEXT));
        assertTrue(names.contains(JFileChooserPropertyBundle.APPROVE_BUTTON_TOOLTIP));
    }

    /**
     * Verify that the three extra properties can be set via setAttribute and retrieved via getters.
     */
    @Test
    public void testSetAttribute()
    {
        JFileChooserPropertyBundle bundle = new JFileChooserPropertyBundle();
        bundle.setAttribute(JFileChooserPropertyBundle.DIALOG_TITLE, "Open File");
        bundle.setAttribute(JFileChooserPropertyBundle.APPROVE_BUTTON_TEXT, "Open");
        bundle.setAttribute(JFileChooserPropertyBundle.APPROVE_BUTTON_TOOLTIP, "Open the selected file");
        assertEquals("Open File", bundle.getDialogTitle());
        assertEquals("Open", bundle.getApproveButtonText());
        assertEquals("Open the selected file", bundle.getApproveButtonToolTip());
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JFileChooserPropertyBundle bundle = new JFileChooserPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> bundle.setAttribute("UnknownKey", "value"));
        assertEquals("Unrecognized attribute name: UnknownKey", e.getMessage());
    }
}
