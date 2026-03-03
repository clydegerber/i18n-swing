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
import javax.swing.JToolTip;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JToolTip that participates in locale change events, updating its tip text, accessible
 * metadata, font, name, and tooltip whenever the application locale changes.
 *
 * <p>Although {@code JToolTip} instances are usually created and managed by Swing itself (via the
 * component's {@code createToolTip()} method), they can be directly instantiated when building
 * custom tooltip factories or advanced UI components.  The primary locale-sensitive property is
 * the tip text set via {@link JToolTip#setTipText}, sourced from
 * {@link JToolTipPropertyBundle#TIP_TEXT}.</p>
 */
public class ResourcefulJToolTip extends JToolTip implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJToolTip with the specified Resource.
     * @param resource A Resource containing a {@link JToolTipPropertyBundle}.
     * @return A ResourcefulJToolTip registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJToolTip create(Resource resource)
    {
        ResourcefulJToolTip toolTip = new ResourcefulJToolTip(resource);
        toolTip.initialize();
        return toolTip;
    }

    private final transient SwingResourcefulDelegate delegate;

    protected ResourcefulJToolTip(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    protected final void initialize()
    {
        delegate.initialize();
    }

    protected void updateLocaleSpecificValues()
    {
        try
        {
            JToolTipPropertyBundle props = (JToolTipPropertyBundle) getResource().getObject();
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
            String tipText = props.getTipText();
            if (null != tipText) setTipText(tipText);
        }
        catch (MissingResourceException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.tool.tip", ex);
        }
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
