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

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJToolBarSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class TestResourcefulJToolBarSeparator
{
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJToolBarSeparator separator = ResourcefulJToolBarSeparator.create(new Resource(source, "TestToolBarSeparator"));
        assertEquals("TestToolBarSeparator name", separator.getName());
        assertEquals("Test toolbar separator tooltip", separator.getToolTipText());
        assertEquals("Test toolbar separator accessible name", separator.getAccessibleContext().getAccessibleName());
    }

    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        ResourcefulJToolBarSeparator separator = ResourcefulJToolBarSeparator.create(new Resource(source, "TestToolBarSeparator"));
        assertEquals("TestToolBarSeparator name", separator.getName());
        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});
        assertEquals("TestToolBarSeparator nom", separator.getName());
        assertEquals("Info-bulle séparateur barre outils test", separator.getToolTipText());
        assertEquals("Nom accessible séparateur barre outils test", separator.getAccessibleContext().getAccessibleName());
    }

    @Test
    public void testAlternativeFactory()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJToolBarSeparator separator = ResourcefulJToolBarSeparator.create(new Resource(source, "TestToolBarSeparator"), new Dimension(5, 5));
        assertNotNull(separator);
    }
}
