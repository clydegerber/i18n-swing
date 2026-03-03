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
import javax.swing.SwingUtilities;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJFileChooser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ResourcefulJFileChooser}.
 *
 * <p>Verifies that the file chooser's name, dialog title, approve-button text, and accessible name
 * are initialised from the default-locale bundle and updated automatically when the application
 * locale changes.</p>
 */
public class TestResourcefulJFileChooser
{
    /**
     * Verify that initial properties are set from the default-locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJFileChooser chooser =
                ResourcefulJFileChooser.create(new Resource(source, "TestFileChooser"));
        assertEquals("TestFileChooser name", chooser.getName());
        assertEquals("Test Dialog", chooser.getDialogTitle());
        assertEquals("Open", chooser.getApproveButtonText());
        assertEquals("Test file chooser accessible name",
                chooser.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that properties update when the application locale changes.
     */
    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        ResourcefulJFileChooser chooser =
                ResourcefulJFileChooser.create(new Resource(source, "TestFileChooser"));
        assertEquals("TestFileChooser name", chooser.getName());

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals("TestFileChooser nom", chooser.getName());
        assertEquals("Boîte de dialogue test", chooser.getDialogTitle());
        assertEquals("Ouvrir", chooser.getApproveButtonText());
        assertEquals("Nom accessible sélecteur fichier test",
                chooser.getAccessibleContext().getAccessibleName());
    }
}
