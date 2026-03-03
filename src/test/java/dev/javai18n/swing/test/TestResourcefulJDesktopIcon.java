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
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJDesktopIcon;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ResourcefulJDesktopIcon}.
 *
 * <p>Verifies that the desktop icon's name, tooltip, and accessible name are initialised from the
 * default-locale bundle and updated automatically when the application locale changes.</p>
 */
public class TestResourcefulJDesktopIcon
{
    /**
     * Verify that initial properties are set from the default-locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        TestComponentSource source = TestComponentSource.create();
        JInternalFrame frame = new JInternalFrame("Test");
        ResourcefulJDesktopIcon icon =
                ResourcefulJDesktopIcon.create(new Resource(source, "TestDesktopIcon"), frame);
        assertEquals("TestDesktopIcon name", icon.getName());
        assertEquals("Test desktop icon tooltip", icon.getToolTipText());
        assertEquals("Test desktop icon accessible name",
                icon.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that properties update when the application locale changes.
     */
    @Test
    public void testLocaleChange()
    {
        TestComponentSource source = TestComponentSource.create();
        JInternalFrame frame = new JInternalFrame("Test");
        ResourcefulJDesktopIcon icon =
                ResourcefulJDesktopIcon.create(new Resource(source, "TestDesktopIcon"), frame);
        assertEquals("TestDesktopIcon name", icon.getName());

        source.setBundleLocale(Locale.FRANCE);
        assertDoesNotThrow(() -> SwingUtilities.invokeAndWait(() -> {}));

        assertEquals("TestDesktopIcon nom", icon.getName());
        assertEquals("Info-bulle icône bureau test", icon.getToolTipText());
        assertEquals("Nom accessible icône bureau test",
                icon.getAccessibleContext().getAccessibleName());
    }
}
