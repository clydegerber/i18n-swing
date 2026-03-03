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
import dev.javai18n.swing.ButtonPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ButtonPropertyBundle
 */
public class TestButtonPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        ButtonPropertyBundle bundle = new ButtonPropertyBundle();
        HashSet names = bundle.validNames();
        assertEquals(5, names.size());
        assertTrue(names.contains(ButtonPropertyBundle.LABEL));
    }

    /**
     * Verify the behavior of setting the LABEL property.
     */
    @Test
    public void testSetAttributes()
    {
        ButtonPropertyBundle bundle = new ButtonPropertyBundle();
        bundle.setAttribute(ButtonPropertyBundle.LABEL, "foo");
        assertEquals("foo", bundle.getLabel());
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        ButtonPropertyBundle bundle = new ButtonPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals(e.getMessage(), "Unrecognized attribute name: foo");
    }
}
