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
import java.io.File;
import java.util.MissingResourceException;
import javax.accessibility.AccessibleContext;
import javax.swing.JFileChooser;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A persistent, locale-aware wrapper around {@link JFileChooser} that sources its dialog title,
 * approve-button label, and approve-button tooltip from a {@link JFileChooserPropertyBundle} in
 * the application's resource bundle.
 *
 * <p>Unlike a plain {@code JFileChooser} whose dialog title and approve-button text must be set
 * programmatically and do not update automatically when the locale changes,
 * {@code ResourcefulJFileChooser} registers as a {@link LocaleEventListener} on its resource's
 * source so that these strings are refreshed automatically on each locale switch.  File filters
 * and their descriptions remain the caller's responsibility.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * openDialog = ResourcefulJFileChooser.create(new Resource(this, "OpenFileDialogProps"));
 *
 * // Show whenever needed — title and approve-button label are always in the current locale:
 * int result = openDialog.showOpenDialog(parentComponent);
 * if (result == JFileChooser.APPROVE_OPTION) { File f = openDialog.getSelectedFile(); ... }
 * }</pre>
 */
public class ResourcefulJFileChooser extends JFileChooser implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJFileChooser pointing to the user's default directory.
     * @param resource A Resource containing a {@link JFileChooserPropertyBundle}.
     * @return A ResourcefulJFileChooser registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJFileChooser create(Resource resource)
    {
        ResourcefulJFileChooser chooser = new ResourcefulJFileChooser(resource);
        chooser.initialize();
        return chooser;
    }

    /**
     * Create a ResourcefulJFileChooser pointing to the specified directory path.
     * @param resource             A Resource containing a {@link JFileChooserPropertyBundle}.
     * @param currentDirectoryPath The path of the directory to display initially.
     * @return A ResourcefulJFileChooser registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJFileChooser create(Resource resource, String currentDirectoryPath)
    {
        ResourcefulJFileChooser chooser = new ResourcefulJFileChooser(resource,
                currentDirectoryPath);
        chooser.initialize();
        return chooser;
    }

    /**
     * Create a ResourcefulJFileChooser pointing to the specified directory.
     * @param resource         A Resource containing a {@link JFileChooserPropertyBundle}.
     * @param currentDirectory The directory to display initially.
     * @return A ResourcefulJFileChooser registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJFileChooser create(Resource resource, File currentDirectory)
    {
        ResourcefulJFileChooser chooser = new ResourcefulJFileChooser(resource, currentDirectory);
        chooser.initialize();
        return chooser;
    }

    private final transient SwingResourcefulDelegate delegate;

    protected ResourcefulJFileChooser(Resource resource)
    {
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    protected ResourcefulJFileChooser(Resource resource, String currentDirectoryPath)
    {
        super(currentDirectoryPath);
        this.delegate = new SwingResourcefulDelegate(resource, this::setLocale,
                this::updateLocaleSpecificValues);
    }

    protected ResourcefulJFileChooser(Resource resource, File currentDirectory)
    {
        super(currentDirectory);
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
            JFileChooserPropertyBundle props =
                    (JFileChooserPropertyBundle) getResource().getObject();
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
            String dialogTitle = props.getDialogTitle();
            if (null != dialogTitle) setDialogTitle(dialogTitle);
            String approveButtonText = props.getApproveButtonText();
            if (null != approveButtonText) setApproveButtonText(approveButtonText);
            String approveButtonToolTip = props.getApproveButtonToolTip();
            if (null != approveButtonToolTip) setApproveButtonToolTipText(approveButtonToolTip);
        }
        catch (MissingResourceException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.file.chooser", ex);
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
