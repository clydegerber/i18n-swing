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
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.accessibility.AccessibleContext;
import javax.swing.JFrame;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.LocalizationDelegate;
import dev.javai18n.core.NoCallbackRegisteredForModuleException;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A LocalizableJFrame class.
 */
public class LocalizableJFrame extends JFrame implements Localizable
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Returns a LocalizableJFrame with the system default GraphicsConfiguration. Creates and initializes a new
     *         LocalizableJFrame.
     * @return A LocalizableJFrame with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static LocalizableJFrame create()
    {
        LocalizableJFrame frame = new LocalizableJFrame();

        frame.updateLocaleSpecificValues();

        return frame;
    }

    /**
     * Returns a LocalizableJFrame in the specified GraphicsConfiguration of a screen device.
     * @param gc The GraphicsConfiguration that is used to construct the new Frame; if gc is null, the system default
     * GraphicsConfiguration is assumed.
     * @return A LocalizableJFrame with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static LocalizableJFrame create(GraphicsConfiguration gc)
    {
        LocalizableJFrame frame = new LocalizableJFrame(gc);

        frame.updateLocaleSpecificValues();

        return frame;
    }

    /**
     * Constructs a LocalizableJFrame with a title from the associated ResourceBundle.
     * @throws HeadlessException if GraphicsEnvironment.isHeadless() returns true.
     */
    protected LocalizableJFrame() throws HeadlessException
    {
    }

    /**
     * Constructs a LocalizableJFrame in the specified GraphicsConfiguration of a screen device.
     * @param gc The GraphicsConfiguration that is used to construct the new Frame; if gc is null, the system default
     * GraphicsConfiguration is assumed.
     */
    protected LocalizableJFrame(GraphicsConfiguration gc)
    {
        super(gc);
    }

    /**
     * The key value used to retrieve the Frame's localizable properties.
     */
    public static final String FRAME_PROPERTIES_KEY = "FrameProperties";

    /**
     * Updates locale-specific properties (title, font, name, accessible name, accessible
     * description, and icon images) by reading a {@link FramePropertyBundle} from the resource
     * bundle under the key {@value #FRAME_PROPERTIES_KEY}.
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
            FramePropertyBundle props = (FramePropertyBundle) getResourceBundle().getObject(FRAME_PROPERTIES_KEY);
            Font font = props.getFont();
            if (null != font) setFont(font);
            String name = props.getName();
            if (null != name) setName(name);
            String title = props.getTitle();
            if (null != title) setTitle(title);
            AccessibleContext ctx = getAccessibleContext();
            if (null != ctx)
            {
                String accessibleName = props.getAccessibleName();
                if (null != accessibleName) ctx.setAccessibleName(accessibleName);
                String accessibleDescription = props.getAccessibleDescription();
                if (null != accessibleDescription) ctx.setAccessibleDescription(accessibleDescription);
            }
            setIconImages(props.getIconImages());
        }
        catch(MissingResourceException e)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.frame", e);
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
     * registered listeners, calls {@link #updateLocaleSpecificValues()} to refresh this frame's
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
     * Returns the ResourceBundle for the Locale that is currently set (by setBundleLocale) for this object.
     * @return The ResourceBundle for the Locale that is currently set (by setBundleLocale) for this object.
     * @throws NoCallbackRegisteredForModuleException if a getBundle callback has not been registered for the module.
     */
    @Override
    public ResourceBundle getResourceBundle() throws NoCallbackRegisteredForModuleException
    {
        return loc.getResourceBundle();
    }

    /**
     * Registers a listener to receive LocaleEvents when the Locale for ResourceBundles provided by this object is
     * changed (via setBundleLocale).
     * @param listener The listener that will receive LocaleEvents.
     */
    @Override
    public void addLocaleEventListener(LocaleEventListener listener)
    {
        loc.addLocaleEventListener(listener);
    }

    /**
     * Unregisters a listener that is receiving LocaleEvents when the Locale for ResourceBundles provided by this
     * object is changed (via setBundleLocale).
     */
    @Override
    public void removeLocaleEventListener(LocaleEventListener listener)
    {
        loc.removeLocaleEventListener(listener);
    }
}
