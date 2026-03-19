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

import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Window;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.accessibility.AccessibleContext;
import javax.swing.JWindow;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.LocalizationDelegate;
import dev.javai18n.core.NoCallbackRegisteredForModuleException;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A {@link JWindow} that participates in locale change events, updating its icon images, accessible
 * metadata, font, and name whenever the application locale changes.
 *
 * <p>{@code JWindow} is an undecorated top-level window with no title bar or window-management
 * buttons.  It is commonly used for splash screens, floating palettes, and custom popups.  A
 * {@code LocalizableJWindow} keeps its accessible description and icon images locale-consistent by
 * listening to the application's locale-change events via {@link Localizable}.</p>
 *
 * <p>Resource bundle lookup uses the key {@value #WINDOW_PROPERTIES_KEY} and expects a
 * {@link WindowPropertyBundle} value.</p>
 */
public class LocalizableJWindow extends JWindow implements Localizable
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * The key used to retrieve the window's localizable properties from the resource bundle.
     */
    public static final String WINDOW_PROPERTIES_KEY = "windowProperties";

    /**
     * Create an ownerless LocalizableJWindow.
     * @return A LocalizableJWindow with locale-sensitive attributes updated from the associated ResourceBundle.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
     */
    public static LocalizableJWindow create() throws HeadlessException
    {
        LocalizableJWindow window = new LocalizableJWindow();
        window.updateLocaleSpecificValues();
        return window;
    }

    /**
     * Create a LocalizableJWindow with the specified owner Frame.
     * @param owner The Frame from which the window is displayed.
     * @return A LocalizableJWindow with locale-sensitive attributes updated from the associated ResourceBundle.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
     */
    public static LocalizableJWindow create(Frame owner) throws HeadlessException
    {
        LocalizableJWindow window = new LocalizableJWindow(owner);
        window.updateLocaleSpecificValues();
        return window;
    }

    /**
     * Create a LocalizableJWindow with the specified owner Window.
     * @param owner The Window from which the window is displayed.
     * @return A LocalizableJWindow with locale-sensitive attributes updated from the associated ResourceBundle.
     */
    public static LocalizableJWindow create(Window owner)
    {
        LocalizableJWindow window = new LocalizableJWindow(owner);
        window.updateLocaleSpecificValues();
        return window;
    }

    /**
     * Create a LocalizableJWindow in the specified GraphicsConfiguration.
     * @param gc The GraphicsConfiguration of the target screen device; if {@code null} the default
     *           screen device is used.
     * @return A LocalizableJWindow with locale-sensitive attributes updated from the associated ResourceBundle.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
     */
    public static LocalizableJWindow create(GraphicsConfiguration gc) throws HeadlessException
    {
        LocalizableJWindow window = new LocalizableJWindow(gc);
        window.updateLocaleSpecificValues();
        return window;
    }

    /**
     * Create a LocalizableJWindow with the specified owner Window in the specified
     * GraphicsConfiguration.
     * @param owner The Window from which the window is displayed.
     * @param gc    The GraphicsConfiguration of the target screen device.
     * @return A LocalizableJWindow with locale-sensitive attributes updated from the associated ResourceBundle.
     */
    public static LocalizableJWindow create(Window owner, GraphicsConfiguration gc)
    {
        LocalizableJWindow window = new LocalizableJWindow(owner, gc);
        window.updateLocaleSpecificValues();
        return window;
    }

    /**
     * Constructs an ownerless LocalizableJWindow.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
     */
    protected LocalizableJWindow() throws HeadlessException
    {
    }

    /**
     * Constructs a LocalizableJWindow with the specified owner Frame.
     * @param owner The Frame from which the window is displayed.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
     */
    protected LocalizableJWindow(Frame owner) throws HeadlessException
    {
        super(owner);
    }

    /**
     * Constructs a LocalizableJWindow with the specified owner Window.
     * @param owner The Window from which the window is displayed.
     */
    protected LocalizableJWindow(Window owner)
    {
        super(owner);
    }

    /**
     * Constructs a LocalizableJWindow in the specified GraphicsConfiguration.
     * @param gc The GraphicsConfiguration of the target screen device.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
     */
    protected LocalizableJWindow(GraphicsConfiguration gc) throws HeadlessException
    {
        super(gc);
    }

    /**
     * Constructs a LocalizableJWindow with the specified owner Window in the specified
     * GraphicsConfiguration.
     * @param owner The Window from which the window is displayed.
     * @param gc    The GraphicsConfiguration of the target screen device.
     */
    protected LocalizableJWindow(Window owner, GraphicsConfiguration gc)
    {
        super(owner, gc);
    }

    /**
     * Updates locale-specific properties (font, name, accessible name, accessible description,
     * and icon images) by reading a {@link WindowPropertyBundle} from the resource bundle under
     * the key {@value #WINDOW_PROPERTIES_KEY}.
     *
     * <p>Called by the {@code create} factory methods at construction time and by
     * {@link #setBundleLocale(Locale)} on each locale change.  Subclasses may override this method
     * to apply additional locale-specific values, and should call
     * {@code super.updateLocaleSpecificValues()} first.  A
     * {@link java.util.MissingResourceException} (e.g. if the key is absent from the bundle) is
     * logged at WARNING level and silently absorbed so that partial initialisation still
     * succeeds.</p>
     */
    protected void updateLocaleSpecificValues()
    {
        try
        {
            WindowPropertyBundle props =
                    (WindowPropertyBundle) getResourceBundle().getObject(WINDOW_PROPERTIES_KEY);
            Font font = props.getFont();
            if (null != font) setFont(font);
            String name = props.getName();
            if (null != name) setName(name);
            AccessibleContext ctx = getAccessibleContext();
            if (null != ctx)
            {
                String accessibleName = props.getAccessibleName();
                if (null != accessibleName) ctx.setAccessibleName(accessibleName);
                String accessibleDescription = props.getAccessibleDescription();
                if (null != accessibleDescription) ctx.setAccessibleDescription(accessibleDescription);
            }
            if (null != props.getIconImages())
            {
                setIconImages(props.getIconImages());
            }
        }
        catch (MissingResourceException e)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.window", e);
        }
    }

    /**
     * A delegate for Localizable functionality.
     */
    private final LocalizationDelegate loc = new LocalizationDelegate(this);

    /**
     * Get the Locale for ResourceBundles provided by this object.
     * @return The Locale for ResourceBundles provided by this object.
     */
    @Override
    public Locale getBundleLocale()
    {
        return loc.getBundleLocale();
    }

    /**
     * Sets the bundle locale, fires a {@link dev.javai18n.core.Localizable.LocaleEvent} to all
     * registered listeners, calls {@link #updateLocaleSpecificValues()} to refresh this window's
     * locale-specific properties, and calls {@link #setLocale(Locale)} to keep the AWT component
     * locale in sync.  Each registered {@code Resourceful} listener component also calls
     * {@code setLocale()} on itself when it processes the event on the EDT.
     *
     * <p>Always use {@code setBundleLocale()} to change the application locale.  Calling
     * {@link #setLocale(Locale)} directly updates the AWT rendering locale but does not reload
     * bundle content or notify any listeners.</p>
     *
     * @param locale The new bundle locale.
     */
    @Override
    public void setBundleLocale(Locale locale)
    {
        loc.setBundleLocale(locale);
        updateLocaleSpecificValues();
        setLocale(locale);
    }

    /**
     * The available Locales for this object.
     * @return An array of the available Locales for this object.
     */
    @Override
    public Locale[] getAvailableLocales()
    {
        return loc.getAvailableLocales();
    }

    /**
     * Returns the ResourceBundle for the Locale that is currently set (by setBundleLocale) for
     * this object.
     * @return The ResourceBundle for the Locale that is currently set (by setBundleLocale) for
     *         this object.
     * @throws NoCallbackRegisteredForModuleException if no bundle callback has been registered
     *         for this module.
     */
    @Override
    public ResourceBundle getResourceBundle() throws NoCallbackRegisteredForModuleException
    {
        return loc.getResourceBundle();
    }

    /**
     * Registers a listener to receive LocaleEvents when the Locale for ResourceBundles provided
     * by this object is changed (via setBundleLocale).
     * @param listener The listener that will receive LocaleEvents.
     */
    @Override
    public void addLocaleEventListener(LocaleEventListener listener)
    {
        loc.addLocaleEventListener(listener);
    }

    /**
     * Unregisters a listener that is receiving LocaleEvents when the Locale for ResourceBundles
     * provided by this object is changed (via setBundleLocale).
     * @param listener The listener that will be unregistered from receiving LocaleEvents.
     */
    @Override
    public void removeLocaleEventListener(LocaleEventListener listener)
    {
        loc.removeLocaleEventListener(listener);
    }
}
