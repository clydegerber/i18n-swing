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
import dev.javai18n.swing.JProgressBarPropertyBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for JProgressBarPropertyBundle.
 */
public class TestJProgressBarPropertyBundle
{
    /**
     * Verify that the set of valid attribute names is correct.
     */
    @Test
    public void testValidNames()
    {
        JProgressBarPropertyBundle bundle = new JProgressBarPropertyBundle();
        HashSet names = bundle.validNames();
        assertEquals(6, names.size());
        assertTrue(names.contains(JProgressBarPropertyBundle.STRING));
    }

    /**
     * Verify that all attributes can be set properly.
     */
    @Test
    public void testSetAttribute()
    {
        JProgressBarPropertyBundle bundle = new JProgressBarPropertyBundle();
        bundle.setAttribute(JProgressBarPropertyBundle.STRING, "Loading...");
        assertEquals(1, bundle.size());
        assertEquals(true, bundle.containsKey(JProgressBarPropertyBundle.STRING));
        String progressString = bundle.getProgressString();
        assertEquals("Loading...", progressString);
    }

    /**
     * Verify that passing an unrecognized attribute name to setAttribute produces an IllegalArgumentException.
     */
    @Test
    public void testUnknownName()
    {
        JProgressBarPropertyBundle bundle = new JProgressBarPropertyBundle();
        Exception e = assertThrows(IllegalArgumentException.class, ()->{bundle.setAttribute("foo", "bar");});
        assertEquals("Unrecognized attribute name: foo", e.getMessage());
    }
}
