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
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import javax.accessibility.AccessibleContext;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JTabbedPane that supports localizing the name, tool tip, font, accessible name and accessible description
 * of the pane itself, as well as per-tab titles, icons, disabled icons and mnemonics.
 */
public class ResourcefulJTabbedPane extends JTabbedPane implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Construct a ResourcefulJTabbedPane with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JTabbedPane itself.
     * @return A ResourcefulJTabbedPane with updated Locale-specific values that is registered to listen to
     *         LocaleEvents generated from the specified Resource's source.
     */
    public static ResourcefulJTabbedPane create(Resource resource)
    {
        ResourcefulJTabbedPane tabbedPane = new ResourcefulJTabbedPane(resource);
        tabbedPane.initialize();
        return tabbedPane;
    }

    /**
     * A delegate for Resourceful and LocaleEventListener functionality.
     */
    private final transient SwingResourcefulDelegate delegate;

    /**
     * The list of per-tab Resources, one for each tab added via addResourcefulTab.
     */
    private final transient List<Resource> tabResources = new ArrayList<>();

    /**
     * Construct a ResourcefulJTabbedPane with the specified Resource.
     * @param resource A Resource containing a JComponentPropertyBundle that holds the localized values for the
     *                 JTabbedPane itself.
     */
    protected ResourcefulJTabbedPane(Resource resource)
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
     * Add a tab with locale-sensitive properties. The tab's title, icon, disabled icon, tool tip and mnemonic
     * are read from the JTabbedPaneTabPropertyBundle associated with the tab's Resource.
     * @param tabResource A Resource containing a JTabbedPaneTabPropertyBundle that holds the localized values
     *                    for the tab.
     * @param component The component to be displayed when this tab is clicked.
     */
    public void addResourcefulTab(Resource tabResource, Component component)
    {
        tabResources.add(tabResource);
        addTab(null, component);
        int index = getTabCount() - 1;
        updateTabProperties(index, tabResource);
    }

    /**
     * Update the properties of a single tab from its JTabbedPaneTabPropertyBundle.
     * @param index The tab index.
     * @param tabResource The Resource for the tab.
     */
    private void updateTabProperties(int index, Resource tabResource)
    {
        try
        {
            JTabbedPaneTabPropertyBundle tabProps = (JTabbedPaneTabPropertyBundle) tabResource.getObject();
            String title = tabProps.getTitle();
            if (null != title) setTitleAt(index, title);
            String toolTipText = tabProps.getToolTipText();
            if (null != toolTipText) setToolTipTextAt(index, toolTipText);
            Icon icon = tabProps.getIcon();
            if (null != icon) setIconAt(index, icon);
            Icon disabledIcon = tabProps.getDisabledIcon();
            if (null != disabledIcon) setDisabledIconAt(index, disabledIcon);
            if (tabProps.containsKey(JTabbedPaneTabPropertyBundle.MNEMONIC))
            {
                int mnemonic = tabProps.getMnemonic();
                setMnemonicAt(index, mnemonic);
            }
        }
        catch (MissingResourceException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.tabbed.pane", ex);
        }
    }

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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.tabbed.pane", ex);
        }
        // Update per-tab properties
        for (int i = 0; i < tabResources.size(); i++)
        {
            updateTabProperties(i, tabResources.get(i));
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
     * Get the Resource holding Locale specific values for the JTabbedPane.
     * @return the Resource holding Locale specific values for the JTabbedPane.
     */
    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding Locale specific values for the JTabbedPane.
     * @param resource the Resource holding Locale specific values for the JTabbedPane.
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
