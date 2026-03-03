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

import java.awt.Component;
import java.awt.Font;
import java.util.MissingResourceException;
import javax.accessibility.AccessibleContext;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import dev.javai18n.core.Localizable;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.Resourceful;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;

/**
 * A persistent, locale-aware wrapper around {@link JOptionPane} that sources its dialog title and
 * button option labels from a {@link JOptionPanePropertyBundle} in the application's resource bundle.
 *
 * <p>Unlike the static {@code JOptionPane.show*} convenience methods, which use
 * {@code UIManager} defaults for button labels (typically English regardless of application locale),
 * {@code ResourcefulJOptionPane} reads its title and options from the same resource bundle that drives
 * all other {@code Resourceful} components, ensuring full locale consistency.  It registers as a
 * {@link LocaleEventListener} on its resource's source so that cached title and option strings are
 * refreshed automatically when the application locale changes.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * // Create once (e.g. during frame initialization):
 * deleteDialog = ResourcefulJOptionPane.create(new Resource(this, "ConfirmDeleteDialogProps"));
 *
 * // Show whenever needed — title and button labels are always in the current locale:
 * int result = deleteDialog.showConfirm(parentComponent, dynamicMessage);
 * }</pre>
 */
public class ResourcefulJOptionPane extends JOptionPane implements Resourceful, LocaleEventListener
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a ResourcefulJOptionPane with the specified Resource.
     * @param resource A Resource containing a {@link JOptionPanePropertyBundle} that holds the
     *                 localized title and button option labels.
     * @return A ResourcefulJOptionPane registered to listen to LocaleEvents from the resource's source.
     */
    public static ResourcefulJOptionPane create(Resource resource)
    {
        ResourcefulJOptionPane pane = new ResourcefulJOptionPane(resource);
        pane.initialize();
        return pane;
    }

    private final transient SwingResourcefulDelegate delegate;

    /** Cached title — refreshed on each locale event. */
    private String cachedTitle;

    /** Cached option button labels — refreshed on each locale event. */
    private String[] cachedOptions;

    protected ResourcefulJOptionPane(Resource resource)
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
            JOptionPanePropertyBundle props = (JOptionPanePropertyBundle) getResource().getObject();
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
            cachedTitle = props.getTitle();
            cachedOptions = props.getOptions();
        }
        catch (MissingResourceException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.WARNING, "missing.resource.for.option.pane", ex);
        }
    }

    /**
     * Show a confirmation dialog using the bundle's title and option button labels.
     *
     * <p>The {@code message} argument is the dynamic content shown above the buttons (e.g. a
     * formatted string including a file name).  The dialog returns the index of the selected option
     * (0 = first option, typically "Yes"/"OK"), or {@link JOptionPane#CLOSED_OPTION} if dismissed.</p>
     *
     * @param parentComponent The component over which the dialog is displayed.
     * @param message         The message to display in the dialog body.
     * @return The index of the selected option, or {@link JOptionPane#CLOSED_OPTION}.
     */
    public int showConfirm(Component parentComponent, Object message)
    {
        String title = cachedTitle != null ? cachedTitle : "";
        String[] options = cachedOptions != null && cachedOptions.length > 0
                ? cachedOptions
                : new String[]{"OK"};
        return JOptionPane.showOptionDialog(parentComponent, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
    }

    /**
     * Show an input dialog using the bundle's title and option button labels.
     *
     * <p>The dialog displays the {@code prompt} message together with a text field.  The bundle's
     * OPTIONS array supplies the button labels (first option = confirm/OK, last = cancel).  Returns
     * the trimmed text entered by the user, or {@code null} if cancelled or dismissed.</p>
     *
     * @param parentComponent The component over which the dialog is displayed.
     * @param prompt          The prompt message displayed above the input field.
     * @return The text entered by the user, or {@code null} if cancelled.
     */
    public String showInput(Component parentComponent, Object prompt)
    {
        String title = cachedTitle != null ? cachedTitle : "";
        String[] options = cachedOptions != null && cachedOptions.length > 0
                ? cachedOptions
                : new String[]{"OK", "Cancel"};
        JTextField textField = new JTextField(25);
        int result = JOptionPane.showOptionDialog(parentComponent,
                new Object[]{prompt, textField}, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (result == 0)
        {
            String text = textField.getText();
            return (text != null && !text.isEmpty()) ? text : null;
        }
        return null;
    }

    /**
     * Show a message dialog using the bundle's title.
     *
     * @param parentComponent The component over which the dialog is displayed.
     * @param message         The message to display.
     */
    public void showMessage(Component parentComponent, Object message)
    {
        String title = cachedTitle != null ? cachedTitle : "";
        JOptionPane.showMessageDialog(parentComponent, message, title,
                JOptionPane.INFORMATION_MESSAGE);
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
