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

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJColorChooser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class TestResourcefulJColorChooser
{
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJColorChooser colorChooser = ResourcefulJColorChooser.create(new Resource(source, "TestColorChooser"));
        assertEquals("TestColorChooser name", colorChooser.getName());
        assertEquals("Test color chooser tooltip", colorChooser.getToolTipText());
        assertEquals("Test color chooser accessible name", colorChooser.getAccessibleContext().getAccessibleName());
    }

    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        ResourcefulJColorChooser colorChooser = ResourcefulJColorChooser.create(new Resource(source, "TestColorChooser"));
        assertEquals("TestColorChooser name", colorChooser.getName());
        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});
        assertEquals("TestColorChooser nom", colorChooser.getName());
        assertEquals("Info-bulle sélecteur couleur test", colorChooser.getToolTipText());
        assertEquals("Nom accessible sélecteur couleur test", colorChooser.getAccessibleContext().getAccessibleName());
    }

    @Test
    public void testAlternativeFactories()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJColorChooser colorChooserWithColor = ResourcefulJColorChooser.create(new Resource(source, "TestColorChooser"), Color.RED);
        assertNotNull(colorChooserWithColor);
        ResourcefulJColorChooser colorChooserWithModel = ResourcefulJColorChooser.create(new Resource(source, "TestColorChooser"), new DefaultColorSelectionModel());
        assertNotNull(colorChooserWithModel);
    }
}
