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
import dev.javai18n.swing.ResourcefulBoxFiller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestResourcefulBoxFiller {
    @Test
    public void testInitialProperties() {
        AppFrame source = AppFrame.create();
        ResourcefulBoxFiller filler = ResourcefulBoxFiller.create(new Resource(source, "TestBoxFiller"), new Dimension(10, 10), new Dimension(20, 20), new Dimension(30, 30));
        assertEquals("TestBoxFiller name", filler.getName());
        assertEquals("Test box filler tooltip", filler.getToolTipText());
        assertEquals("Test box filler accessible name", filler.getAccessibleContext().getAccessibleName());
    }

    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException {
        AppFrame source = AppFrame.create();
        ResourcefulBoxFiller filler = ResourcefulBoxFiller.create(new Resource(source, "TestBoxFiller"), new Dimension(10, 10), new Dimension(20, 20), new Dimension(30, 30));
        assertEquals("TestBoxFiller name", filler.getName());
        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});
        assertEquals("TestBoxFiller nom", filler.getName());
        assertEquals("Info-bulle remplissage boîte test", filler.getToolTipText());
        assertEquals("Nom accessible remplissage boîte test", filler.getAccessibleContext().getAccessibleName());
    }
}
