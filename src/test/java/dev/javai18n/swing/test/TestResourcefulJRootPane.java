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
import dev.javai18n.swing.ResourcefulJRootPane;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestResourcefulJRootPane
{
    @Test
    public void testInitialProperties()
    {
        TestComponentSource source = TestComponentSource.create();
        ResourcefulJRootPane rootPane = ResourcefulJRootPane.create(new Resource(source, "TestRootPane"));
        assertEquals("TestRootPane name", rootPane.getName());
        assertEquals("Test root pane tooltip", rootPane.getToolTipText());
        assertEquals("Test root pane accessible name", rootPane.getAccessibleContext().getAccessibleName());
    }

    @Test
    public void testLocaleChange()
    {
        TestComponentSource source = TestComponentSource.create();
        ResourcefulJRootPane rootPane = ResourcefulJRootPane.create(new Resource(source, "TestRootPane"));
        assertEquals("TestRootPane name", rootPane.getName());
        source.setBundleLocale(Locale.FRANCE);
        assertDoesNotThrow(() -> SwingUtilities.invokeAndWait(() -> {}));
        assertEquals("TestRootPane nom", rootPane.getName());
        assertEquals("Info-bulle volet racine test", rootPane.getToolTipText());
        assertEquals("Nom accessible volet racine test", rootPane.getAccessibleContext().getAccessibleName());
    }
}
