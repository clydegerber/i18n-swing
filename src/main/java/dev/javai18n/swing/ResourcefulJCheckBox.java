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
import javax.swing.Icon;
import javax.swing.JCheckBox;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.AbstractButtonPropertyBundle.MNEMONIC;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JCheckBox that supports localizing the name, tool tip, font, text, mnemonic, accessible name, accessible
 * description and icons (normal, pressed, selected, disabled, disabled selected, rollover and rollover selected).
 */
public class ResourcefulJCheckBox extends JCheckBox implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Construct a ResourcefulJCheckBox with the specified Resource.
     * @param resource A Resource containing an AbstractButtonPropertyBundle that holds the localized values for the
     *                 JCheckBox.
     * @return A ResourcefulJCheckBox with updated Locale-specific values that is registered to listen to LocaleEvents
     *         generated from the specified Resource's source.
     */
    public static ResourcefulJCheckBox create(Resource resource)
    {
        ResourcefulJCheckBox checkBox = new ResourcefulJCheckBox(resource);
        checkBox.initialize();
        return checkBox;
    }

    /**
     * A delegate for Resourceful and LocaleEventListener functionality.
     */
    private final transient SwingResourcefulDelegate delegate;

    /**
     * Constructs a JCheckBox bound to the given resource.
     * @param resource The resource identifying the locale source and bundle key.
     */
    protected ResourcefulJCheckBox(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Registers this component as a locale-event listener on its resource source and
     * applies the initial locale-specific values from the resource bundle.
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
            String label = props.getLabel();
            if (null != label) setText(label);
            String text = props.getText();
            if (null != text) setText(text);
            if (props.containsKey(MNEMONIC))
            {
                int mnemonic = props.getMnemonic();
                setMnemonic(mnemonic);
            }
            Icon icon = props.getIcon();
            if (null != icon) setIcon(icon);
            Icon disabledIcon = props.getDisabledIcon();
            if (null != disabledIcon) setDisabledIcon(disabledIcon);
            Icon disabledSelectedIcon = props.getDisabledSelectedIcon();
            if (null != disabledSelectedIcon) setDisabledSelectedIcon(disabledSelectedIcon);
            Icon pressedIcon = props.getPressedIcon();
            if (null != pressedIcon) setPressedIcon(pressedIcon);
            Icon rolloverIcon = props.getRolloverIcon();
            if (null != rolloverIcon) setRolloverIcon(rolloverIcon);
            Icon rolloverSelectedIcon = props.getRolloverSelectedIcon();
            if (null != rolloverSelectedIcon) setRolloverSelectedIcon(rolloverSelectedIcon);
            Icon selectedIcon = props.getSelectedIcon();
            if (null != selectedIcon) setSelectedIcon(selectedIcon);
        }
        catch (MissingResourceException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.check.box", ex);
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
     * Get the Resource holding Locale specific values for the JCheckBox.
     * @return the Resource holding Locale specific values for the JCheckBox.
     */
    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding Locale specific values for the JCheckBox.
     * @param resource the Resource holding Locale specific values for the JCheckBox.
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
