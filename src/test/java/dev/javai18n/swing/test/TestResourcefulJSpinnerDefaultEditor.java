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

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJSpinnerDefaultEditor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourcefulJSpinnerDefaultEditor.
 */
public class TestResourcefulJSpinnerDefaultEditor
{
    /**
     * Verify that initial properties are set from the default locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        JSpinner spinner = new JSpinner();
        ResourcefulJSpinnerDefaultEditor editor =
                ResourcefulJSpinnerDefaultEditor.create(
                        new Resource(source, "TestSpinnerDefaultEditor"), spinner);
        assertEquals("TestSpinnerDefaultEditor name", editor.getName());
        assertEquals("Test spinner default editor tooltip", editor.getToolTipText());
        assertEquals("Test spinner default editor accessible name",
                editor.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that properties update when the application locale changes.
     */
    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        JSpinner spinner = new JSpinner();
        ResourcefulJSpinnerDefaultEditor editor =
                ResourcefulJSpinnerDefaultEditor.create(
                        new Resource(source, "TestSpinnerDefaultEditor"), spinner);
        assertEquals("TestSpinnerDefaultEditor name", editor.getName());

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals("TestSpinnerDefaultEditor nom", editor.getName());
        assertEquals("Info-bulle éditeur compteur défaut test", editor.getToolTipText());
        assertEquals("Nom accessible éditeur compteur défaut test",
                editor.getAccessibleContext().getAccessibleName());
    }
}
