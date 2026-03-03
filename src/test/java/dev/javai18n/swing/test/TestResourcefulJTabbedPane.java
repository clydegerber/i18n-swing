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
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJTabbedPane;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourcefulJTabbedPane.
 */
public class TestResourcefulJTabbedPane
{

    /**
     * Verify that initial pane properties are set from the default locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        TestComponentSource source = TestComponentSource.create();
        ResourcefulJTabbedPane tabbedPane = ResourcefulJTabbedPane.create(
                new Resource(source, "TestTabbedPane"));
        assertEquals("TestTabbedPane name", tabbedPane.getName());
        assertEquals("Test tabbed pane tooltip", tabbedPane.getToolTipText());
    }

    /**
     * Verify that per-tab properties are set correctly on addResourcefulTab.
     */
    @Test
    public void testTabProperties()
    {
        TestComponentSource source = TestComponentSource.create();
        ResourcefulJTabbedPane tabbedPane = ResourcefulJTabbedPane.create(
                new Resource(source, "TestTabbedPane"));
        tabbedPane.addResourcefulTab(new Resource(source, "TestTab1"), new JLabel("Content 1"));
        tabbedPane.addResourcefulTab(new Resource(source, "TestTab2"), new JLabel("Content 2"));
        assertEquals(2, tabbedPane.getTabCount());
        assertEquals("Tab One", tabbedPane.getTitleAt(0));
        assertEquals("First tab tooltip", tabbedPane.getToolTipTextAt(0));
        assertEquals("Tab Two", tabbedPane.getTitleAt(1));
        assertEquals("Second tab tooltip", tabbedPane.getToolTipTextAt(1));
    }

    /**
     * Verify that pane and tab properties update when the locale is changed.
     */
    @Test
    public void testLocaleChange()
    {
        TestComponentSource source = TestComponentSource.create();
        ResourcefulJTabbedPane tabbedPane = ResourcefulJTabbedPane.create(
                new Resource(source, "TestTabbedPane"));
        tabbedPane.addResourcefulTab(new Resource(source, "TestTab1"), new JLabel("Content 1"));
        tabbedPane.addResourcefulTab(new Resource(source, "TestTab2"), new JLabel("Content 2"));
        assertEquals("Tab One", tabbedPane.getTitleAt(0));
        source.setBundleLocale(Locale.FRANCE);
        assertDoesNotThrow(() -> SwingUtilities.invokeAndWait(() -> {}));
        assertEquals("TestTabbedPane nom", tabbedPane.getName());
        assertEquals("Onglet Un", tabbedPane.getTitleAt(0));
        assertEquals("Onglet Deux", tabbedPane.getTitleAt(1));
        assertEquals("Info-bulle premier onglet", tabbedPane.getToolTipTextAt(0));
    }
}
