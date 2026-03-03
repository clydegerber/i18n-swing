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

module dev.javai18n.swing.test
{
    requires static org.junit.jupiter.api;
    requires java.desktop;
    requires dev.javai18n.swing;
    exports dev.javai18n.swing.test;
    opens dev.javai18n.swing.test;
    uses dev.javai18n.swing.test.spi.AppDialogProvider;
    uses dev.javai18n.swing.test.spi.AppFrameProvider;
    uses dev.javai18n.swing.test.spi.AppWindowProvider;
    provides dev.javai18n.swing.test.spi.AppDialogProvider with dev.javai18n.swing.test.spi.ModuleProviderImpl;
    provides dev.javai18n.swing.test.spi.AppFrameProvider with dev.javai18n.swing.test.spi.ModuleProviderImpl;
    provides dev.javai18n.swing.test.spi.AppWindowProvider with dev.javai18n.swing.test.spi.ModuleProviderImpl;
}
