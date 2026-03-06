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
import java.awt.LayoutManager;
import java.util.MissingResourceException;
import javax.accessibility.AccessibleContext;
import javax.swing.JPanel;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JPanel that supports localizing the name, tool tip, font, accessible name and accessible description for the
 * JPanel.
 */
public class ResourcefulJPanel extends JPanel implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Construct a ResourcefulJPanel with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     * @return A ResourcefulJPanel with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJPanel create(Resource resource)
    {
        ResourcefulJPanel panel = new ResourcefulJPanel(resource);
        panel.initialize();
        return panel;
    }

    /**
     * Construct a ResourcefulJPanel with the specified Resource and buffering strategy.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve
     *                         fast, flicker-free updates
     * @return A ResourcefulJPanel with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJPanel create(Resource resource, boolean isDoubleBuffered)
    {
        ResourcefulJPanel panel = new ResourcefulJPanel(resource, isDoubleBuffered);
        panel.initialize();
        return panel;
    }

    /**
     * Construct a ResourcefulJPanel with the specified Resource, LayoutManager and buffering strategy.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     * @param layout The LayoutManager to use
     * @return A ResourcefulJPanel with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJPanel create(Resource resource, LayoutManager layout)
    {
        ResourcefulJPanel panel = new ResourcefulJPanel(resource, layout);
        panel.initialize();
        return panel;
    }

    /**
     * Construct a ResourcefulJPanel with the specified Resource, LayoutManager and buffering strategy.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     * @param layout The LayoutManager to use
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve
     *                         fast, flicker-free updates
     * @return A ResourcefulJPanel with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJPanel create(Resource resource, LayoutManager layout, boolean isDoubleBuffered)
    {
        ResourcefulJPanel panel = new ResourcefulJPanel(resource, layout, isDoubleBuffered);
        panel.initialize();
        return panel;
    }

    /**
     * A delegate for Resourceful and LocaleEventListener functionality.
     */
    private final transient SwingResourcefulDelegate delegate;

    /**
     * Construct a ResourcefulJPanel with the specified Resource, a double buffer and a flow layout.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     */
    protected ResourcefulJPanel(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJPanel with the specified Resource with FlowLayout and the specified buffering strategy.
     * If isDoubleBuffered is true, the ResourcefulJPanel will use a double buffer.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve
     *                         fast, flicker-free updates
     */
    protected ResourcefulJPanel(Resource resource, boolean isDoubleBuffered)
    {
        super(isDoubleBuffered);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJPanel with the specified Resource and the specified layout manager.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     * @param layout The LayoutManager to use
     */
    protected ResourcefulJPanel(Resource resource, LayoutManager layout)
    {
        super(layout);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJPanel with the specified Resource and the specified layout manager and buffering strategy.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JPanel
     * @param layout The LayoutManager to use
     * @param isDoubleBuffered A boolean, true for double-buffering, which uses additional memory space to achieve
     *                         fast, flicker-free updates
     */
    protected ResourcefulJPanel(Resource resource, LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.panel", ex);
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
     * Get the Resource holding Locale specific values for the JPanel.
     * @return the Resource holding Locale specific values for the JPanel.
     */
    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding Locale specific values for the JPanel.
     * @param resource the Resource holding Locale specific values for the JPanel.
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
