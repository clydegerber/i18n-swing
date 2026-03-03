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
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJSpinnerDateEditor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourcefulJSpinnerDateEditor.
 */
public class TestResourcefulJSpinnerDateEditor
{
    /**
     * Verify that initial properties are set from the default locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        ResourcefulJSpinnerDateEditor editor =
                ResourcefulJSpinnerDateEditor.create(
                        new Resource(source, "TestSpinnerDateEditor"), spinner);
        assertEquals("TestSpinnerDateEditor name", editor.getName());
        assertEquals("Test spinner date editor tooltip", editor.getToolTipText());
        assertEquals("Test spinner date editor accessible name",
                editor.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that properties update when the application locale changes.
     */
    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        ResourcefulJSpinnerDateEditor editor =
                ResourcefulJSpinnerDateEditor.create(
                        new Resource(source, "TestSpinnerDateEditor"), spinner);
        assertEquals("TestSpinnerDateEditor name", editor.getName());

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals("TestSpinnerDateEditor nom", editor.getName());
        assertEquals("Info-bulle éditeur compteur date test", editor.getToolTipText());
        assertEquals("Nom accessible éditeur compteur date test",
                editor.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that the two-argument factory accepting an explicit date format pattern
     * creates a non-null editor.
     */
    @Test
    public void testWithDateFormatPattern()
    {
        AppFrame source = AppFrame.create();
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        ResourcefulJSpinnerDateEditor editor =
                ResourcefulJSpinnerDateEditor.create(
                        new Resource(source, "TestSpinnerDateEditor"), spinner, "yyyy-MM-dd");
        assertNotNull(editor);
        assertEquals("TestSpinnerDateEditor name", editor.getName());
    }
}
