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

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.accessibility.AccessibleContext;
import javax.swing.JDialog;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.LocalizationDelegate;
import dev.javai18n.core.NoCallbackRegisteredForModuleException;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A LocalizableJDialog class that supports localizing the title, font, name, accessible name, accessible
 * description and icon images from an associated ResourceBundle.
 */
public class LocalizableJDialog extends JDialog implements Localizable
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Creates and initializes a new ownerless LocalizableJDialog.
     * @return A LocalizableJDialog with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static LocalizableJDialog create()
    {
        LocalizableJDialog dialog = new LocalizableJDialog();

        dialog.updateLocaleSpecificValues();

        return dialog;
    }

    /**
     * Creates and initializes a new LocalizableJDialog with the specified owner Frame.
     * @param owner The Frame from which the dialog is displayed.
     * @return A LocalizableJDialog with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static LocalizableJDialog create(Frame owner)
    {
        LocalizableJDialog dialog = new LocalizableJDialog(owner);

        dialog.updateLocaleSpecificValues();

        return dialog;
    }

    /**
     * Creates and initializes a new LocalizableJDialog with the specified owner Dialog.
     * @param owner The Dialog from which the dialog is displayed.
     * @return A LocalizableJDialog with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static LocalizableJDialog create(Dialog owner)
    {
        LocalizableJDialog dialog = new LocalizableJDialog(owner);

        dialog.updateLocaleSpecificValues();

        return dialog;
    }

    /**
     * Creates and initializes a new LocalizableJDialog with the specified owner Window.
     * @param owner The Window from which the dialog is displayed.
     * @return A LocalizableJDialog with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static LocalizableJDialog create(Window owner)
    {
        LocalizableJDialog dialog = new LocalizableJDialog(owner);

        dialog.updateLocaleSpecificValues();

        return dialog;
    }

    /**
     * Constructs an ownerless LocalizableJDialog with a title from the associated ResourceBundle.
     * @throws HeadlessException if GraphicsEnvironment.isHeadless() returns true.
     */
    protected LocalizableJDialog() throws HeadlessException
    {
    }

    /**
     * Constructs a LocalizableJDialog with the specified owner Frame.
     * @param owner The Frame from which the dialog is displayed.
     */
    protected LocalizableJDialog(Frame owner)
    {
        super(owner);
    }

    /**
     * Constructs a LocalizableJDialog with the specified owner Dialog.
     * @param owner The Dialog from which the dialog is displayed.
     */
    protected LocalizableJDialog(Dialog owner)
    {
        super(owner);
    }

    /**
     * Constructs a LocalizableJDialog with the specified owner Window.
     * @param owner The Window from which the dialog is displayed.
     */
    protected LocalizableJDialog(Window owner)
    {
        super(owner);
    }

    /**
     * The key value used to retrieve the Dialog's localizable properties.
     */
    public static final String DIALOG_PROPERTIES_KEY = "DialogProperties";

    /**
     * Updates locale-specific properties (title, font, name, accessible name, accessible
     * description, and icon images) by reading a {@link FramePropertyBundle} from the resource
     * bundle under the key {@value #DIALOG_PROPERTIES_KEY}.
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
            FramePropertyBundle props = (FramePropertyBundle) getResourceBundle().getObject(DIALOG_PROPERTIES_KEY);
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
            if (null != props.getIconImages())
            {
                setIconImages(props.getIconImages());
            }
        }
        catch (MissingResourceException e)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.dialog", e);
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
     * Set the Locale for ResourceBundles provided by this object. Also sets the Locale for this object.
     * @param locale The Locale for ResourceBundles provided by this object and for this object.
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
     * @throws NoCallbackRegisteredForModuleException
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
     * @param listener The listener that will be unregistered from receiving LocaleEvents.
     */
    @Override
    public void removeLocaleEventListener(LocaleEventListener listener)
    {
        loc.removeLocaleEventListener(listener);
    }
}
