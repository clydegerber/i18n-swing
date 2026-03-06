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
import javax.swing.JScrollBar;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JScrollBar that supports localizing the name, tool tip, font, accessible name and accessible description.
 */
public class ResourcefulJScrollBar extends JScrollBar implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Construct a ResourcefulJScrollBar with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JScrollBar.
     * @return A ResourcefulJScrollBar with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJScrollBar create(Resource resource)
    {
        ResourcefulJScrollBar scrollBar = new ResourcefulJScrollBar(resource);
        scrollBar.initialize();
        return scrollBar;
    }

    /**
     * Construct a ResourcefulJScrollBar with the specified Resource and orientation.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JScrollBar.
     * @param orientation Either JScrollBar.HORIZONTAL or JScrollBar.VERTICAL.
     * @return A ResourcefulJScrollBar with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJScrollBar create(Resource resource, int orientation)
    {
        ResourcefulJScrollBar scrollBar = new ResourcefulJScrollBar(resource, orientation);
        scrollBar.initialize();
        return scrollBar;
    }

    /**
     * Construct a ResourcefulJScrollBar with the specified Resource, orientation, value, extent, minimum, and maximum.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JScrollBar.
     * @param orientation Either JScrollBar.HORIZONTAL or JScrollBar.VERTICAL.
     * @param value The initial value of the scrollbar.
     * @param extent The size of the viewable area (the "knob").
     * @param min The minimum value of the scrollbar.
     * @param max The maximum value of the scrollbar.
     * @return A ResourcefulJScrollBar with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJScrollBar create(Resource resource, int orientation, int value, int extent, int min, int max)
    {
        ResourcefulJScrollBar scrollBar = new ResourcefulJScrollBar(resource, orientation, value, extent, min, max);
        scrollBar.initialize();
        return scrollBar;
    }

    /**
     * A delegate for Resourceful and LocaleEventListener functionality.
     */
    private final transient SwingResourcefulDelegate delegate;

    /**
     * Construct a ResourcefulJScrollBar with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JScrollBar.
     */
    protected ResourcefulJScrollBar(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJScrollBar with the specified Resource and orientation.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JScrollBar.
     * @param orientation Either JScrollBar.HORIZONTAL or JScrollBar.VERTICAL.
     */
    protected ResourcefulJScrollBar(Resource resource, int orientation)
    {
        super(orientation);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJScrollBar with the specified Resource, orientation, value, extent, minimum, and maximum.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JScrollBar.
     * @param orientation Either JScrollBar.HORIZONTAL or JScrollBar.VERTICAL.
     * @param value The initial value of the scrollbar.
     * @param extent The size of the viewable area (the "knob").
     * @param min The minimum value of the scrollbar.
     * @param max The maximum value of the scrollbar.
     */
    protected ResourcefulJScrollBar(Resource resource, int orientation, int value, int extent, int min, int max)
    {
        super(orientation, value, extent, min, max);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.scroll.bar", ex);
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
     * Get the Resource holding Locale specific values for the JScrollBar.
     * @return the Resource holding Locale specific values for the JScrollBar.
     */
    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding Locale specific values for the JScrollBar.
     * @param resource the Resource holding Locale specific values for the JScrollBar.
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
