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

module dev.javai18n.swing
{
    requires java.desktop;
    requires transitive dev.javai18n.core;
    exports dev.javai18n.swing;
    exports dev.javai18n.swing.spi;
    opens dev.javai18n.swing;
    uses dev.javai18n.swing.spi.LocalizableJDialogProvider;
    uses dev.javai18n.swing.spi.LocalizableJFrameProvider;
    uses dev.javai18n.swing.spi.SwingLoggerProvider;
    provides dev.javai18n.swing.spi.LocalizableJDialogProvider with dev.javai18n.swing.spi.ModuleProviderImpl;
    provides dev.javai18n.swing.spi.LocalizableJFrameProvider with dev.javai18n.swing.spi.ModuleProviderImpl;
    provides dev.javai18n.swing.spi.SwingLoggerProvider with dev.javai18n.swing.spi.ModuleProviderImpl;
}
