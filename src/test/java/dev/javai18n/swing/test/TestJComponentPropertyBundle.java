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
import dev.javai18n.swing.JComponentPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JComponentPropertyBundle
 */
public class TestJComponentPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        JComponentPropertyBundle bundle = new JComponentPropertyBundle();
        Set<String> names = bundle.validNames();
        assertEquals(5, names.size());
        assertTrue(names.contains(JComponentPropertyBundle.TOOL_TIP_TEXT));
    }

    /**
     * Verify that setAttribute() function as expected with valid input.
     */
    @Test
    public void testSetAttribute()
    {
        JComponentPropertyBundle bundle = new JComponentPropertyBundle();
        bundle.setAttribute(JComponentPropertyBundle.TOOL_TIP_TEXT, "foo");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(JComponentPropertyBundle.TOOL_TIP_TEXT));
        assertEquals("foo", bundle.get(JComponentPropertyBundle.TOOL_TIP_TEXT));
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JComponentPropertyBundle bundle = new JComponentPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals("Unrecognized attribute name: foo", e.getMessage());
    }
}
