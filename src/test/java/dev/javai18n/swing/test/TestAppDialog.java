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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for AppDialog.
 */
public class TestAppDialog
{
    /**
     * Verify that initial properties are set from the default locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppDialog dialog = AppDialog.create();
        assertEquals("About File Explorer", dialog.getTitle());
        assertEquals("AboutDialog name", dialog.getName());
    }

    /**
     * Verify that initialize() constructs all Resourceful components without throwing.
     * This catches bundle-entry type mismatches (e.g. AbstractButtonPropertyBundle used
     * where JLabelPropertyBundle is expected) that only surface when a component is created.
     */
    @Test
    public void testInitialize()
    {
        assertDoesNotThrow(() ->
        {
            AppDialog dialog = AppDialog.create();
            dialog.initialize();
        });
    }

    /**
     * Verify that properties update when the locale is changed.
     */
    @Test
    public void testLocaleChange()
    {
        AppDialog dialog = AppDialog.create();
        assertEquals("About File Explorer", dialog.getTitle());
        dialog.setBundleLocale(Locale.FRANCE);
        assertEquals("À propos de l'Explorateur de fichiers", dialog.getTitle());
        assertEquals("AboutDialog nom", dialog.getName());
    }
}
