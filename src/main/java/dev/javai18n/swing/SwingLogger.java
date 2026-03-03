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

package dev.javai18n.swing;

import static java.lang.System.LoggerFinder.getLoggerFinder;
import dev.javai18n.core.LocalizableLogger;

/**
 * A LocalizableLogger for the i18n-swing module. Log messages are resolved
 * from the SwingLoggerBundle resource bundle in this module.
 */
public class SwingLogger extends LocalizableLogger
{
    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * The logger used internally by the i18n-swing module.
     */
    public static final SwingLogger SWING_LOGGER = createSwingLogger("dev.javai18n.swing");

    /**
     * A factory method that returns a SwingLogger for the specified name in the default Locale.
     * @param name the name of the logger.
     * @return A SwingLogger for the specified name in the default Locale.
     */
    public static SwingLogger createSwingLogger(String name)
    {
        SwingLogger swingLogger = new SwingLogger(name);
        swingLogger.logger = getLoggerFinder().getLocalizedLogger(
                name, swingLogger.getResourceBundle(), swingLogger.getClass().getModule());
        return swingLogger;
    }

    /**
     * Constructs a new SwingLogger for the specified name with the default Locale.
     * @param name the name of the logger.
     */
    protected SwingLogger(String name)
    {
        super(name);
    }
}
