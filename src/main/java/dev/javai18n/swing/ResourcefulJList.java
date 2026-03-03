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
import javax.swing.JList;
import javax.swing.ListModel;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JList that participates in locale change events, updating its accessible metadata and repainting
 * so that locale-sensitive cell renderers redraw correctly after a locale switch.
 *
 * <p>List item content is supplied by the {@link ListModel} and is the caller's responsibility.
 * When list items are locale-sensitive strings (e.g. day names, month names), callers should
 * repopulate the model in their own locale change handler so that the new strings are displayed.</p>
 *
 * @param <E> The type of the list elements.
 */
public class ResourcefulJList<E> extends JList<E> implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJList with the specified Resource and no list model.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @param <E> The type of the list elements.
     * @return A ResourcefulJList registered to listen to LocaleEvents from the resource's source.
     */
    public static <E> ResourcefulJList<E> create(Resource resource)
    {
        ResourcefulJList<E> list = new ResourcefulJList<>(resource);
        list.initialize();
        return list;
    }

    /**
     * Create a ResourcefulJList with the specified Resource and ListModel.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @param model    The ListModel to display.
     * @param <E> The type of the list elements.
     * @return A ResourcefulJList registered to listen to LocaleEvents from the resource's source.
     */
    public static <E> ResourcefulJList<E> create(Resource resource, ListModel<E> model)
    {
        ResourcefulJList<E> list = new ResourcefulJList<>(resource, model);
        list.initialize();
        return list;
    }

    private final transient SwingResourcefulDelegate delegate;

    protected ResourcefulJList(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    protected ResourcefulJList(Resource resource, ListModel<E> model)
    {
        super(model);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.list", ex);
        }
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
