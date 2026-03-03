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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A {@link JTableHeader} that participates in locale change events, updating its accessible
 * metadata, font, name, and tooltip and repainting its column headers whenever the application
 * locale changes.
 *
 * <p>Column header cell renderers typically read their text from the {@link javax.swing.table.TableModel}
 * at paint time.  Calling {@link #repaint()} on a locale change therefore causes any
 * locale-sensitive renderers to redraw with the new locale without requiring a full model reset.
 * If the column names themselves change with the locale (i.e. the model's
 * {@code getColumnName()} reads from the current locale), the owning {@link javax.swing.JTable}
 * should call {@code createDefaultColumnsFromModel()} — which {@link ResourcefulJTable} does
 * automatically.</p>
 */
public class ResourcefulJTableHeader extends JTableHeader implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJTableHeader with the specified Resource and default column model.
     * @param resource A Resource containing a JComponentPropertyBundle.
     * @return A ResourcefulJTableHeader registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJTableHeader create(Resource resource)
    {
        ResourcefulJTableHeader header = new ResourcefulJTableHeader(resource);
        header.initialize();
        return header;
    }

    /**
     * Create a ResourcefulJTableHeader with the specified Resource and column model.
     * @param resource    A Resource containing a JComponentPropertyBundle.
     * @param columnModel The {@link TableColumnModel} that supplies column definitions.
     * @return A ResourcefulJTableHeader registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJTableHeader create(Resource resource, TableColumnModel columnModel)
    {
        ResourcefulJTableHeader header = new ResourcefulJTableHeader(resource, columnModel);
        header.initialize();
        return header;
    }

    private final transient SwingResourcefulDelegate delegate;

    protected ResourcefulJTableHeader(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    protected ResourcefulJTableHeader(Resource resource, TableColumnModel columnModel)
    {
        super(columnModel);
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
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.table.header", ex);
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
