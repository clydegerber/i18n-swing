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
import javax.swing.table.DefaultTableColumnModel;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJTableHeader;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ResourcefulJTableHeader}.
 *
 * <p>Verifies that the table header's name, tooltip, and accessible name are initialised from the
 * default-locale bundle and updated automatically when the application locale changes.</p>
 */
public class TestResourcefulJTableHeader
{
    /**
     * Verify that initial properties are set from the default-locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJTableHeader header =
                ResourcefulJTableHeader.create(new Resource(source, "TestTableHeader"));
        assertEquals("TestTableHeader name", header.getName());
        assertEquals("Test table header tooltip", header.getToolTipText());
        assertEquals("Test table header accessible name",
                header.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that properties update when the application locale changes.
     */
    @Test
    public void testLocaleChange()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJTableHeader header =
                ResourcefulJTableHeader.create(new Resource(source, "TestTableHeader"));
        assertEquals("TestTableHeader name", header.getName());

        source.setBundleLocale(Locale.FRANCE);
        assertDoesNotThrow(() -> SwingUtilities.invokeAndWait(() -> {}));

        assertEquals("TestTableHeader nom", header.getName());
        assertEquals("Info-bulle en-tête tableau test", header.getToolTipText());
        assertEquals("Nom accessible en-tête tableau test",
                header.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that a ResourcefulJTableHeader created with an explicit column model is not null.
     */
    @Test
    public void testWithColumnModel()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJTableHeader header = ResourcefulJTableHeader.create(
                new Resource(source, "TestTableHeader"), new DefaultTableColumnModel());
        assertNotNull(header);
    }
}
