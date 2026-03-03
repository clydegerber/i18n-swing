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

import java.util.Locale;
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJSeparator;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourcefulJSeparator.
 */
public class TestResourcefulJSeparator
{

    /**
     * Verify that initial properties are set from the default locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJSeparator separator = ResourcefulJSeparator.create(new Resource(source, "TestSeparator"));
        assertEquals("TestSeparator name", separator.getName());
        assertEquals("Test separator tooltip", separator.getToolTipText());
    }

    /**
     * Verify that properties update when the locale is changed.
     */
    @Test
    public void testLocaleChange()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJSeparator separator = ResourcefulJSeparator.create(new Resource(source, "TestSeparator"));
        assertEquals("TestSeparator name", separator.getName());
        source.setBundleLocale(Locale.FRANCE);
        assertDoesNotThrow(() -> SwingUtilities.invokeAndWait(() -> {}));
        assertEquals("TestSeparator nom", separator.getName());
        assertEquals("Info-bulle séparateur test", separator.getToolTipText());
    }
}
