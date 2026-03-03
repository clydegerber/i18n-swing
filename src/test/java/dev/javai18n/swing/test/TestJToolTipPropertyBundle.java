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
import dev.javai18n.swing.JToolTipPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JToolTipPropertyBundle.
 */
public class TestJToolTipPropertyBundle
{
    /**
     * Verify that the set of valid attribute names includes TIP_TEXT in addition to those inherited
     * from JComponentPropertyBundle (5 names). Total expected: 5 + 1 = 6.
     */
    @Test
    public void testValidNames()
    {
        JToolTipPropertyBundle bundle = new JToolTipPropertyBundle();
        HashSet<String> names = bundle.validNames();
        assertEquals(6, names.size());
        assertTrue(names.contains(JToolTipPropertyBundle.TIP_TEXT));
    }

    /**
     * Verify that TIP_TEXT can be set via setAttribute and retrieved via getTipText().
     */
    @Test
    public void testSetAttribute()
    {
        JToolTipPropertyBundle bundle = new JToolTipPropertyBundle();
        bundle.setAttribute(JToolTipPropertyBundle.TIP_TEXT, "Hover text");
        assertEquals("Hover text", bundle.getTipText());
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JToolTipPropertyBundle bundle = new JToolTipPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> bundle.setAttribute("UnknownKey", "value"));
        assertEquals("Unrecognized attribute name: UnknownKey", e.getMessage());
    }
}
