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
import java.util.MissingResourceException;
import javax.accessibility.AccessibleContext;
import javax.swing.JMenu;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.AbstractButtonPropertyBundle.MNEMONIC;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JMenu that supports localizing the name, tool tip, font, text, mnemonic, accessible name, accessible
 * description and icons (normal, pressed, selected, disabled, disabled selected, rollover and rollover selected).
 */
public class ResourcefulJMenu extends JMenu implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Construct a ResourcefulJMenu with the specified Resource.
     * @param resource A Resource containing an AbstractButtonPropertyBundle that holds the localized values for the
     *                 JMenu
     * @return A ResourcefulJMenu with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJMenu create(Resource resource)
    {
        ResourcefulJMenu menu = new ResourcefulJMenu(resource);
        menu.initialize();
        return menu;
    }

    /**
     * A delegate for Resourceful and LocaleEventListener functionality.
     */
    private final transient SwingResourcefulDelegate delegate;

    /**
     * Construct a ResourcefulJMenu with the specified Resource.
     * @param resource A Resource containing an AbstractButtonPropertyBundle that holds the localized values for the
     *                 JMenu
     */
    protected ResourcefulJMenu(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Initialize this component by setting its locale, updating locale-specific values, and registering to listen
     * for locale events from the resource's source.
     */
    protected final void initialize()
    {
        delegate.initialize();
    }

    /**
     * Applies locale-specific values from the associated resource bundle to this component.
     */
    protected void updateLocaleSpecificValues()
    {
        try
        {
            AbstractButtonPropertyBundle props = (AbstractButtonPropertyBundle) getResource().getObject();
            Font font = props.getFont();
            if (null != font) setFont(font);
            String name = props.getName();
            if (null != name) setName(name);
            String toolTipText = props.getToolTipText();
            if (null != toolTipText) setToolTipText(toolTipText);
            AccessibleContext ctx = getAccessibleContext();
            if (null != ctx)
            {
                String accessibleName = props.getAccessibleName();
                if (null != accessibleName) ctx.setAccessibleName(accessibleName);
                String accessibleDescription = props.getAccessibleDescription();
                if (null != accessibleDescription) ctx.setAccessibleDescription(accessibleDescription);
            }
            String text = props.getText();
            if (null != text) setText(text);
            if (props.containsKey(MNEMONIC))
            {
                int mnemonic = props.getMnemonic();
                setMnemonic(mnemonic);
            }
        }
        catch (MissingResourceException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.menu", ex);
        }
    }

    /**
     * Update locale-specific values to reflect the new Locale.
     * @param event The LocaleEvent that has been raised.
     */
    @Override
    public void processLocaleEvent(Localizable.LocaleEvent event)
    {
        delegate.processLocaleEvent(event);
    }

    /**
     * Get the Resource holding Locale specific values for the JMenu.
     * @return the Resource holding Locale specific values for the JMenu.
     */
    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding Locale specific values for the JMenu.
     * @param resource the Resource holding Locale specific values for the JMenu.
     */
    @Override
    public void setResource(Resource resource)
    {
        delegate.setResource(resource);
    }

    /**
     * Unregister this component as a locale-event listener on its resource's source.
     * Call this when the component is being permanently discarded so that it does not
     * accumulate in the source's listener list.
     */
    public void dispose()
    {
        delegate.dispose();
    }
}
