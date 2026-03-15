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
import dev.javai18n.swing.LocalizableJWindow;

/**
 * A minimal LocalizableJWindow subclass used in unit tests.  Its resource bundle
 * ({@code AppWindowBundle}) contains a {@code windowProperties} entry that exercises
 * all locale-sensitive properties of {@link LocalizableJWindow}.
 */
public class AppWindow extends LocalizableJWindow
{
    static
    {
        SwingTestModuleRegistrar.ensureRegistered();
    }

    /**
     * Create and initialize a new ownerless AppWindow.
     * @return An AppWindow with locale-sensitive attributes updated from the associated ResourceBundle.
     */
    public static AppWindow create()
    {
        AppWindow window = new AppWindow();
        window.setBundleLocale(Locale.ROOT);
        window.updateLocaleSpecificValues();
        return window;
    }

    AppWindow()
    {
        super();
    }
}
