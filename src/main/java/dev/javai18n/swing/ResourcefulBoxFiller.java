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

import java.awt.Dimension;
import java.awt.Font;
import java.util.MissingResourceException;
import javax.accessibility.AccessibleContext;
import javax.swing.Box;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A {@link Box.Filler} that participates in locale change events, updating its accessible
 * metadata, font, and name whenever the application locale changes.
 *
 * <p>{@code Box.Filler} is a transparent, invisible spacing component used inside {@link Box}
 * layouts to provide fixed, minimum, or flexible gaps between other components.  Its primary
 * locale-sensitive properties are its accessible name and description, which assistive technologies
 * may expose to users.</p>
 */
public class ResourcefulBoxFiller extends Box.Filler implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulBoxFiller with the specified Resource and size constraints.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @param min      The minimum size of the filler.
     * @param pref     The preferred size of the filler.
     * @param max      The maximum size of the filler.
     * @return A ResourcefulBoxFiller registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulBoxFiller create(Resource resource,
            Dimension min, Dimension pref, Dimension max)
    {
        ResourcefulBoxFiller filler = new ResourcefulBoxFiller(resource, min, pref, max);
        filler.initialize();
        return filler;
    }

    private final transient SwingResourcefulDelegate delegate;

    /**
     * Constructs a Box.Filler bound to the given resource and size constraints.
     * @param resource The resource identifying the locale source and bundle key.
     * @param min      The minimum size of the filler.
     * @param pref     The preferred size of the filler.
     * @param max      The maximum size of the filler.
     */
    protected ResourcefulBoxFiller(Resource resource, Dimension min, Dimension pref, Dimension max)
    {
        super(min, pref, max);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.box.filler", ex);
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
