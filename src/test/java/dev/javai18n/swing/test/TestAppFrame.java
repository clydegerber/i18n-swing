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

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import dev.javai18n.core.JsonResourceBundle;
import dev.javai18n.swing.FramePropertyBundle;
import dev.javai18n.swing.SwingModuleRegistrar;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for AppFrame bundle loading and module-path configuration.
 */
public class TestAppFrame
{
    @BeforeAll
    public static void registerPackage()
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Verify that AppFrame.create() constructs all Resourceful components without throwing.
     * This catches bundle-entry type mismatches that only surface when a component is created.
     */
    @Test
    public void testCreate()
    {
        assertDoesNotThrow(() -> AppFrame.create());
    }

    @Test
    public void testAppFrameJsonBundle()
    {
        Module module = this.getClass().getModule();
        InputStream stream = assertDoesNotThrow(() -> module.getResourceAsStream("dev/javai18n/swing/test/AppFrameBundle.json"));
        JsonResourceBundle jsonBundle = assertDoesNotThrow(()->new JsonResourceBundle(stream));
        FramePropertyBundle fb = (FramePropertyBundle) jsonBundle.getObject("FrameProperties");
        assertEquals("File Explorer", fb.getTitle());
        ResourceBundle rb = ResourceBundle.getBundle("dev.javai18n.swing.test.AppFrame", Locale.ROOT);
        fb = (FramePropertyBundle) rb.getObject("FrameProperties");
        assertEquals("File Explorer", fb.getTitle());
        rb = ResourceBundle.getBundle("dev.javai18n.swing.test.AppFrame", Locale.FRANCE);
        fb = (FramePropertyBundle) rb.getObject("FrameProperties");
        assertEquals("Explorateur de fichiers", fb.getTitle());
    }

    @Test
    public void testRunningInModuleMode()
    {
        Module module = this.getClass().getModule();

        if (module.isNamed())
        {
            System.out.println(" Running in MODULE mode");
            System.out.println(" Module name: " + module.getName());
        }
        else
        {
            System.out.println(" Running in CLASSPATH mode");
            System.out.println(" Module: unnamed");
        }
    }

    @Test
    public void debugModulePathSetup()
    {
        System.out.println("=== MODULE DEBUG INFO ===");
        Module module = this.getClass().getModule();
        System.out.println("Module isNamed: " + module.isNamed());
        System.out.println("Module name: " + module.getName());
        System.out.println("Module descriptor: " + module.getDescriptor());
        System.out.println("=========================");
        System.out.println("jdk.module.path: " + System.getProperty("jdk.module.path"));
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));
        System.out.println("=========================");
    }
}
