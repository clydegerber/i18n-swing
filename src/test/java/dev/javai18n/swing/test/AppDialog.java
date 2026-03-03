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

package dev.javai18n.swing.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.text.MessageFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.UIManager;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.AbstractButtonPropertyBundle;
import dev.javai18n.swing.LocalizableJDialog;
import dev.javai18n.swing.ResourcefulJButton;
import dev.javai18n.swing.ResourcefulJLabel;
import dev.javai18n.swing.ResourcefulJPanel;

/**
 * An About dialog that extends LocalizableJDialog.
 */
public class AppDialog extends LocalizableJDialog
{
    static
    {
        SwingTestModuleRegistrar.ensureRegistered();
    }

    /**
     * Create and initialize a new ownerless AppDialog.
     * @return An AppDialog with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static AppDialog create()
    {
        AppDialog dialog = new AppDialog();

        dialog.updateLocaleSpecificValues();

        return dialog;
    }

    /**
     * Create and initialize a new AppDialog with the specified owner Frame.
     * @param owner The Frame from which the dialog is displayed.
     * @return An AppDialog with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static AppDialog create(Frame owner)
    {
        AppDialog dialog = new AppDialog(owner);

        dialog.updateLocaleSpecificValues();

        return dialog;
    }

    /**
     * Construct an ownerless AppDialog.
     */
    protected AppDialog()
    {
    }

    /**
     * Construct an AppDialog with the specified owner Frame.
     * @param owner The Frame from which the dialog is displayed.
     */
    protected AppDialog(Frame owner)
    {
        super(owner);
    }

    /**
     * The supported Locales for this dialog.
     * @return An array of supported Locales.
     */
    @Override
    public Locale[] getAvailableLocales()
    {
        Locale[] locales =
        {
            Locale.US,
            Locale.FRANCE,
            Locale.GERMANY
        };
        return locales;
    }

    /**
     * Initialize the About dialog content with Resourceful widgets.
     */
    public void initialize()
    {
        setLayout(new BorderLayout());

        ResourcefulJPanel contentPanel = ResourcefulJPanel.create(new Resource(this, "PanelProps"));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ResourcefulJLabel titleLabel = ResourcefulJLabel.create(new Resource(this, "AboutTitleLabel"));
        contentPanel.add(titleLabel);
        contentPanel.add(javax.swing.Box.createVerticalStrut(10));

        ResourcefulJLabel versionLabel = ResourcefulJLabel.create(new Resource(this, "AboutVersionLabel"));
        contentPanel.add(versionLabel);
        contentPanel.add(javax.swing.Box.createVerticalStrut(10));

        ResourcefulJLabel localeLabel = ResourcefulJLabel.create(new Resource(this, "AboutLocaleLabel"));
        contentPanel.add(localeLabel);
        contentPanel.add(javax.swing.Box.createVerticalStrut(5));

        ResourcefulJLabel localeValueLabel = ResourcefulJLabel.create(new Resource(this, "LocaleValueLabelProps"));
        AbstractButtonPropertyBundle localeValueProps = (AbstractButtonPropertyBundle) getResourceBundle().getObject("LocaleValueLabelProps");
        localeValueLabel.setText(new MessageFormat(localeValueProps.getText(), getBundleLocale())
                .format(new Object[]{getBundleLocale().getDisplayName(getBundleLocale())}));
        contentPanel.add(localeValueLabel);
        contentPanel.add(javax.swing.Box.createVerticalStrut(5));

        ResourcefulJLabel lafLabel = ResourcefulJLabel.create(new Resource(this, "LookAndFeelLabelProps"));
        AbstractButtonPropertyBundle lafProps = (AbstractButtonPropertyBundle) getResourceBundle().getObject("LookAndFeelLabelProps");
        lafLabel.setText(new MessageFormat(lafProps.getText(), getBundleLocale())
                .format(new Object[]{UIManager.getLookAndFeel().getName()}));
        contentPanel.add(lafLabel);

        add(contentPanel, BorderLayout.CENTER);

        ResourcefulJPanel buttonPanel = ResourcefulJPanel.create(new Resource(this, "PanelProps"));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        ResourcefulJButton closeButton = ResourcefulJButton.create(new Resource(this, "CloseButtonProps"));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setModal(true);
    }
}
