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
import javax.swing.JSpinner;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A {@link JSpinner.ListEditor} that participates in locale change events, updating its accessible
 * metadata, font, name, and tooltip whenever the application locale changes.
 *
 * <p>{@code JSpinner.ListEditor} displays items from a {@link javax.swing.SpinnerListModel} as
 * strings.  When the application locale changes, the caller is responsible for repopulating the
 * model with locale-appropriate values.  This class handles the accessible metadata that assistive
 * technologies use to describe the editor.</p>
 */
public class ResourcefulJSpinnerListEditor extends JSpinner.ListEditor
        implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJSpinnerListEditor bound to the specified spinner.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @param spinner  The {@link JSpinner} this editor is associated with.
     * @return A ResourcefulJSpinnerListEditor registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJSpinnerListEditor create(Resource resource, JSpinner spinner)
    {
        ResourcefulJSpinnerListEditor editor = new ResourcefulJSpinnerListEditor(resource, spinner);
        editor.initialize();
        return editor;
    }

    private final transient SwingResourcefulDelegate delegate;

    protected ResourcefulJSpinnerListEditor(Resource resource, JSpinner spinner)
    {
        super(spinner);
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
            SWING_LOGGER.log(System.Logger.Level.DEBUG,
                    "missing.resource.for.spinner.list.editor", ex);
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
