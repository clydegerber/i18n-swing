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
import javax.swing.JMenuBar;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JMenuBar that supports localizing the name, tool tip, font, accessible name and accessible description.
 */
public class ResourcefulJMenuBar extends JMenuBar implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Construct a ResourcefulJMenuBar with the specified Resource.
     * @param resource A Resource containing an JComponentPropertyBundle that holds the localized values for the
     *                 JMenuBar
     * @return A ResourcefulJMenuBar with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJMenuBar create(Resource resource)
    {
        ResourcefulJMenuBar menuBar = new ResourcefulJMenuBar(resource);
        menuBar.initialize();
        return menuBar;
    }

    /**
     * A delegate for Resourceful and LocaleEventListener functionality.
     */
    private final transient SwingResourcefulDelegate delegate;

    /**
     * Construct a ResourcefulJMenuBar with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JMenuBar
     */
    protected ResourcefulJMenuBar(Resource resource)
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
     * Update Locale specific values for the JMenuBar from the JComponentPropertyBundle returned by the resource.
     * If the JComponentProperty value is not found, no update is done. If a property is not found in the
     * JComponentPropertyBundle, that value will not be updated.
     */
    protected void updateLocaleSpecificValues()
    {
        try
        {
            JComponentPropertyBundle props = (JComponentPropertyBundle) getResource().getObject();
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
        }
        catch (MissingResourceException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.menu.bar", ex);
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
     * Get the Resource holding Locale specific values for the JMenuBar.
     * @return the Resource holding Locale specific values for the JMenuBar.
     */
    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding Locale specific values for the JMenuBar.
     * @param resource the Resource holding Locale specific values for the JMenuBar.
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
