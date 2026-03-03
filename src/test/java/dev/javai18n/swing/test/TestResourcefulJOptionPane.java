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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJOptionPane;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourcefulJOptionPane.
 */
public class TestResourcefulJOptionPane
{
    /**
     * Verify that initial properties (tooltip, accessible name, cached title, and cached options)
     * are populated from the default-locale bundle on creation.
     */
    @Test
    public void testInitialProperties() throws Exception
    {
        AppFrame source = AppFrame.create();
        ResourcefulJOptionPane pane = ResourcefulJOptionPane.create(new Resource(source, "TestOptionPane"));
        assertEquals("Test dialog tooltip", pane.getToolTipText());
        assertEquals("Test dialog accessible name", pane.getAccessibleContext().getAccessibleName());
        assertEquals("Test Dialog", cachedTitle(pane));
        assertArrayEquals(new String[]{"OK", "Cancel"}, cachedOptions(pane));
    }

    /**
     * Verify that the cached title, cached options, tooltip, and accessible name are all refreshed
     * when the application locale changes.
     */
    @Test
    public void testLocaleChange() throws Exception
    {
        AppFrame source = AppFrame.create();
        ResourcefulJOptionPane pane = ResourcefulJOptionPane.create(new Resource(source, "TestOptionPane"));
        assertEquals("Test Dialog", cachedTitle(pane));
        assertArrayEquals(new String[]{"OK", "Cancel"}, cachedOptions(pane));

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals("Info-bulle dialogue test", pane.getToolTipText());
        assertEquals("Nom accessible dialogue test", pane.getAccessibleContext().getAccessibleName());
        assertEquals("Dialogue Test", cachedTitle(pane));
        assertArrayEquals(new String[]{"OK", "Annuler"}, cachedOptions(pane));
    }

    // --- helpers ---

    private static String cachedTitle(ResourcefulJOptionPane pane) throws Exception
    {
        Field f = ResourcefulJOptionPane.class.getDeclaredField("cachedTitle");
        f.setAccessible(true);
        return (String) f.get(pane);
    }

    private static String[] cachedOptions(ResourcefulJOptionPane pane) throws Exception
    {
        Field f = ResourcefulJOptionPane.class.getDeclaredField("cachedOptions");
        f.setAccessible(true);
        return (String[]) f.get(pane);
    }
}
