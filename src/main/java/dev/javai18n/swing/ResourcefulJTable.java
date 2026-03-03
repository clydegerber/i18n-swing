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
import javax.swing.JTable;
import javax.swing.table.TableModel;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A JTable that participates in locale change events, allowing it to refresh column headers and cell
 * renderers when the application locale changes.
 *
 * <p>Column header names are typically provided by the {@link TableModel} (via
 * {@code TableModel.getColumnName()}), not by the property bundle. When the locale changes,
 * {@code ResourcefulJTable} calls {@code createDefaultColumnsFromModel()} to force the table to
 * re-read column names from the model and then calls {@code repaint()} so that locale-sensitive
 * cell renderers (e.g. date formatters, number formatters) also redraw with the new locale.
 * This means that any {@code TableModel.getColumnName()} implementation that reads from the
 * current application locale will automatically produce correct column headers after a locale
 * switch.</p>
 */
public class ResourcefulJTable extends JTable implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJTable with the specified Resource and no table model.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @return A ResourcefulJTable registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJTable create(Resource resource)
    {
        ResourcefulJTable table = new ResourcefulJTable(resource);
        table.initialize();
        return table;
    }

    /**
     * Create a ResourcefulJTable with the specified Resource and TableModel.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @param model    The TableModel to display.
     * @return A ResourcefulJTable registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJTable create(Resource resource, TableModel model)
    {
        ResourcefulJTable table = new ResourcefulJTable(resource, model);
        table.initialize();
        return table;
    }

    private final transient SwingResourcefulDelegate delegate;

    protected ResourcefulJTable(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    protected ResourcefulJTable(Resource resource, TableModel model)
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.table", ex);
        }
        // Force column headers to re-read names from the model using the new locale,
        // then repaint cells so that locale-sensitive renderers (dates, numbers) redraw.
        createDefaultColumnsFromModel();
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
