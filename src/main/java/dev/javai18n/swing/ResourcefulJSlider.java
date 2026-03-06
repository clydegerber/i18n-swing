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
import javax.swing.JSlider;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JSlider that participates in locale change events, updating its accessible metadata and
 * repainting so that locale-sensitive label tables redraw correctly after a locale switch.
 *
 * <p>When a custom label table containing locale-sensitive strings is attached to the slider
 * (via {@link JSlider#setLabelTable(java.util.Dictionary)}), callers should repopulate the label
 * table in their own locale change handler and call {@link JSlider#updateLabelUIs()} to ensure
 * tick-mark labels are redrawn in the new locale.</p>
 */
public class ResourcefulJSlider extends JSlider implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJSlider with the specified Resource and default horizontal orientation.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @return A ResourcefulJSlider registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJSlider create(Resource resource)
    {
        ResourcefulJSlider slider = new ResourcefulJSlider(resource);
        slider.initialize();
        return slider;
    }

    /**
     * Create a ResourcefulJSlider with the specified Resource, orientation, and range.
     * @param resource    A Resource containing a JComponentPropertyBundle.
     * @param orientation {@link JSlider#HORIZONTAL} or {@link JSlider#VERTICAL}.
     * @param min         The slider minimum value.
     * @param max         The slider maximum value.
     * @param value       The slider initial value.
     * @return A ResourcefulJSlider registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJSlider create(Resource resource, int orientation, int min, int max, int value)
    {
        ResourcefulJSlider slider = new ResourcefulJSlider(resource, orientation, min, max, value);
        slider.initialize();
        return slider;
    }

    private final transient SwingResourcefulDelegate delegate;

    /**
     * Constructs a JSlider bound to the given resource with default horizontal orientation.
     * @param resource The resource identifying the locale source and bundle key.
     */
    protected ResourcefulJSlider(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Constructs a JSlider bound to the given resource with the specified orientation and range.
     * @param resource    The resource identifying the locale source and bundle key.
     * @param orientation {@link javax.swing.JSlider#HORIZONTAL} or {@link javax.swing.JSlider#VERTICAL}.
     * @param min         The slider minimum value.
     * @param max         The slider maximum value.
     * @param value       The slider initial value.
     */
    protected ResourcefulJSlider(Resource resource, int orientation, int min, int max, int value)
    {
        super(orientation, min, max, value);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.slider", ex);
        }
        repaint();
    }

    /**
     * Public convenience method that calls the protected {@link JSlider#updateLabelUIs()} so that
     * callers outside the class hierarchy (e.g. an application frame) can trigger a label-UI
     * refresh after repopulating the slider's label table for a new locale.
     */
    public void refreshLabelUIs()
    {
        updateLabelUIs();
    }

    @Override
    public void processLocaleEvent(Localizable.LocaleEvent event)
    {
        delegate.processLocaleEvent(event);
    }

    @Override
    public Resource getResource()
    {
        return delegate.getResource();
    }

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
