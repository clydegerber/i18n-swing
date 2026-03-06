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
 * A {@link JSpinner.DateEditor} that participates in locale change events, updating its accessible
 * metadata, font, name, and tooltip whenever the application locale changes.
 *
 * <p>{@code JSpinner.DateEditor} formats the spinner's date value using a {@link java.text.SimpleDateFormat}.
 * This class handles the accessible metadata; the format pattern itself must be reinstalled by the
 * caller in response to locale changes (see {@link ResourcefulJSpinner} for the recommended approach).</p>
 */
public class ResourcefulJSpinnerDateEditor extends JSpinner.DateEditor
        implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJSpinnerDateEditor with the default date format pattern.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @param spinner  The {@link JSpinner} this editor is associated with.
     * @return A ResourcefulJSpinnerDateEditor registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJSpinnerDateEditor create(Resource resource, JSpinner spinner)
    {
        ResourcefulJSpinnerDateEditor editor = new ResourcefulJSpinnerDateEditor(resource, spinner);
        editor.initialize();
        return editor;
    }

    /**
     * Create a ResourcefulJSpinnerDateEditor with the specified date format pattern.
     * @param resource          A Resource containing a JComponentPropertyBundle.
     * @param spinner           The {@link JSpinner} this editor is associated with.
     * @param dateFormatPattern A {@link java.text.SimpleDateFormat} pattern string.
     * @return A ResourcefulJSpinnerDateEditor registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJSpinnerDateEditor create(Resource resource, JSpinner spinner,
            String dateFormatPattern)
    {
        ResourcefulJSpinnerDateEditor editor =
                new ResourcefulJSpinnerDateEditor(resource, spinner, dateFormatPattern);
        editor.initialize();
        return editor;
    }

    private final transient SwingResourcefulDelegate delegate;

    /**
     * Constructs a JSpinner.DateEditor bound to the given resource with the default date format.
     * @param resource The resource identifying the locale source and bundle key.
     * @param spinner  The {@link JSpinner} this editor is associated with.
     */
    protected ResourcefulJSpinnerDateEditor(Resource resource, JSpinner spinner)
    {
        super(spinner);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    /**
     * Constructs a JSpinner.DateEditor bound to the given resource with the specified date format pattern.
     * @param resource          The resource identifying the locale source and bundle key.
     * @param spinner           The {@link JSpinner} this editor is associated with.
     * @param dateFormatPattern A {@link java.text.SimpleDateFormat} pattern string.
     */
    protected ResourcefulJSpinnerDateEditor(Resource resource, JSpinner spinner,
            String dateFormatPattern)
    {
        super(spinner, dateFormatPattern);
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
            SWING_LOGGER.log(System.Logger.Level.DEBUG,
                    "missing.resource.for.spinner.date.editor", ex);
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
