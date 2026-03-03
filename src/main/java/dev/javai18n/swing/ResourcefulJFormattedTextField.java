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
import javax.swing.JFormattedTextField;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JFormattedTextField that participates in locale change events.
 *
 * <p>On locale change, this component updates its locale, accessible name, accessible description,
 * tooltip, font, and component name from the resource bundle, and calls {@code revalidate()} and
 * {@code repaint()}.  Because the formatter itself (e.g. a {@link java.text.DateFormat} or
 * {@link java.text.NumberFormat}) carries its own locale reference baked in at construction time,
 * callers that need the formatter itself to change locale should install a new formatter in their
 * own {@link dev.javai18n.core.Localizable.LocaleEventListener} registered on the same source.</p>
 */
public class ResourcefulJFormattedTextField extends JFormattedTextField implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJFormattedTextField with the specified Resource and no formatter.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @return A ResourcefulJFormattedTextField registered to listen to LocaleEvents.
     */
    public static ResourcefulJFormattedTextField create(Resource resource)
    {
        ResourcefulJFormattedTextField field = new ResourcefulJFormattedTextField(resource);
        field.initialize();
        return field;
    }

    /**
     * Create a ResourcefulJFormattedTextField with the specified Resource and formatter.
     * @param resource  A Resource containing a JComponentPropertyBundle.
     * @param formatter The formatter to use.
     * @return A ResourcefulJFormattedTextField registered to listen to LocaleEvents.
     */
    public static ResourcefulJFormattedTextField create(Resource resource, AbstractFormatter formatter)
    {
        ResourcefulJFormattedTextField field = new ResourcefulJFormattedTextField(resource, formatter);
        field.initialize();
        return field;
    }

    private final transient SwingResourcefulDelegate delegate;

    protected ResourcefulJFormattedTextField(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    protected ResourcefulJFormattedTextField(Resource resource, AbstractFormatter formatter)
    {
        super(formatter);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.formatted.text.field", ex);
        }
        revalidate();
        repaint();
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
