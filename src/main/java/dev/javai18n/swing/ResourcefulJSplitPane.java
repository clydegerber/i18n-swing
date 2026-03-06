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

import java.awt.Component;
import java.awt.Font;
import java.util.MissingResourceException;
import javax.accessibility.AccessibleContext;
import javax.swing.JSplitPane;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JSplitPane that supports localizing the name, tool tip, font, accessible name and accessible description.
 */
public class ResourcefulJSplitPane extends JSplitPane implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @return A ResourcefulJSplitPane with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJSplitPane create(Resource resource)
    {
        ResourcefulJSplitPane splitPane = new ResourcefulJSplitPane(resource);
        splitPane.initialize();
        return splitPane;
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource and orientation.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @return A ResourcefulJSplitPane with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJSplitPane create(Resource resource, int newOrientation)
    {
        ResourcefulJSplitPane splitPane = new ResourcefulJSplitPane(resource, newOrientation);
        splitPane.initialize();
        return splitPane;
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource, orientation and redrawing style.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param newContinuousLayout A boolean, true for the components to redraw continuously as the divider changes
     *                            position, false to wait until the divider position stops changing to redraw.
     * @return A ResourcefulJSplitPane with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJSplitPane create(Resource resource, int newOrientation, boolean newContinuousLayout)
    {
        ResourcefulJSplitPane splitPane = new ResourcefulJSplitPane(resource, newOrientation, newContinuousLayout);
        splitPane.initialize();
        return splitPane;
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource, orientation, and child components.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param newLeftComponent The Component that will appear on the left of a horizontally-split pane, or at the top
     *                         of a vertically-split pane.
     * @param newRightComponent The Component that will appear on the right of a horizontally-split pane, or at the
     *                          bottom of a vertically-split pane.
     * @return A ResourcefulJSplitPane with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJSplitPane create(Resource resource, int newOrientation,
            Component newLeftComponent, Component newRightComponent)
    {
        ResourcefulJSplitPane splitPane = new ResourcefulJSplitPane(resource, newOrientation,
                newLeftComponent, newRightComponent);
        splitPane.initialize();
        return splitPane;
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource, orientation, redrawing style, and child
     * components.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param newContinuousLayout A boolean, true for the components to redraw continuously as the divider changes
     *                            position, false to wait until the divider position stops changing to redraw.
     * @param newLeftComponent The Component that will appear on the left of a horizontally-split pane, or at the top
     *                         of a vertically-split pane.
     * @param newRightComponent The Component that will appear on the right of a horizontally-split pane, or at the
     *                          bottom of a vertically-split pane.
     * @return A ResourcefulJSplitPane with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJSplitPane create(Resource resource, int newOrientation, boolean newContinuousLayout,
            Component newLeftComponent, Component newRightComponent)
    {
        ResourcefulJSplitPane splitPane = new ResourcefulJSplitPane(resource, newOrientation, newContinuousLayout,
                newLeftComponent, newRightComponent);
        splitPane.initialize();
        return splitPane;
    }

    /**
     * A delegate for Resourceful and LocaleEventListener functionality.
     */
    private final transient SwingResourcefulDelegate delegate;

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     */
    protected ResourcefulJSplitPane(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource and orientation.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     */
    protected ResourcefulJSplitPane(Resource resource, int newOrientation)
    {
        super(newOrientation);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource, orientation and redrawing style.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param newContinuousLayout A boolean, true for the components to redraw continuously as the divider changes
     *                            position, false to wait until the divider position stops changing to redraw.
     */
    protected ResourcefulJSplitPane(Resource resource, int newOrientation, boolean newContinuousLayout)
    {
        super(newOrientation, newContinuousLayout);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource, orientation, and child components.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param newLeftComponent The Component that will appear on the left of a horizontally-split pane, or at the top
     *                         of a vertically-split pane.
     * @param newRightComponent The Component that will appear on the right of a horizontally-split pane, or at the
     *                          bottom of a vertically-split pane.
     */
    protected ResourcefulJSplitPane(Resource resource, int newOrientation,
            Component newLeftComponent, Component newRightComponent)
    {
        super(newOrientation, newLeftComponent, newRightComponent);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Construct a ResourcefulJSplitPane with the specified Resource, orientation, redrawing style, and child
     * components.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JSplitPane.
     * @param newOrientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param newContinuousLayout A boolean, true for the components to redraw continuously as the divider changes
     *                            position, false to wait until the divider position stops changing to redraw.
     * @param newLeftComponent The Component that will appear on the left of a horizontally-split pane, or at the top
     *                         of a vertically-split pane.
     * @param newRightComponent The Component that will appear on the right of a horizontally-split pane, or at the
     *                          bottom of a vertically-split pane.
     */
    protected ResourcefulJSplitPane(Resource resource, int newOrientation, boolean newContinuousLayout,
            Component newLeftComponent, Component newRightComponent)
    {
        super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.split.pane", ex);
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
     * Get the Resource holding Locale specific values for the JSplitPane.
     * @return the Resource holding Locale specific values for the JSplitPane.
     */
    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding Locale specific values for the JSplitPane.
     * @param resource the Resource holding Locale specific values for the JSplitPane.
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
