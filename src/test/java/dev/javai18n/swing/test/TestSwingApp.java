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

import java.awt.Image;
import java.awt.Taskbar;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import dev.javai18n.swing.SwingModuleRegistrar;

/**
 *
 */
public class TestSwingApp
{
    public void display()
    {
        System.setProperty("apple.awt.application.name", "File Explorer");
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            // Fall back to default L&F
        }

        AppFrame frame = AppFrame.create();
        try
        {
            Image image = ImageIO.read(TestSwingApp.class.getResourceAsStream("/dev/javai18n/swing/test/MN_Flag.jpg"));
            frame.setIconImage(image);
            Taskbar.getTaskbar().setIconImage(image);
        }
        catch (IOException ex)
        {
        }
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingModuleRegistrar.ensureRegistered();
        new TestSwingApp().display();
    }
}
