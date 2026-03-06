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

import java.util.HashSet;
import java.util.Set;

/**
 * Provides a collection of localizable property values for a {@link ResourcefulJFileChooser},
 * extending {@link JComponentPropertyBundle} with a dialog title, an approve-button label, and an
 * approve-button tooltip — the three {@link javax.swing.JFileChooser} properties that typically
 * carry locale-specific text.
 */
public class JFileChooserPropertyBundle extends JComponentPropertyBundle
{
    /** Creates a new JFileChooserPropertyBundle. */
    public JFileChooserPropertyBundle()
    {
    }

    /**
     * A key for the dialog title string displayed in the file-chooser window's title bar.
     */
    public static final String DIALOG_TITLE = "DialogTitle";

    /**
     * A key for the text label of the approve button (e.g. "Open", "Save").
     */
    public static final String APPROVE_BUTTON_TEXT = "ApproveButtonText";

    /**
     * A key for the tooltip text of the approve button.
     */
    public static final String APPROVE_BUTTON_TOOLTIP = "ApproveButtonToolTip";

    /**
     * Returns the String associated with the DIALOG_TITLE key.
     * @return the dialog title, or {@code null} if not set.
     */
    public String getDialogTitle()
    {
        return getString(DIALOG_TITLE);
    }

    /**
     * Sets the String associated with the DIALOG_TITLE key.
     * @param title the dialog title.
     */
    public void setDialogTitle(String title)
    {
        put(DIALOG_TITLE, title);
    }

    /**
     * Returns the String associated with the APPROVE_BUTTON_TEXT key.
     * @return the approve-button text, or {@code null} if not set.
     */
    public String getApproveButtonText()
    {
        return getString(APPROVE_BUTTON_TEXT);
    }

    /**
     * Sets the String associated with the APPROVE_BUTTON_TEXT key.
     * @param text the approve-button text.
     */
    public void setApproveButtonText(String text)
    {
        put(APPROVE_BUTTON_TEXT, text);
    }

    /**
     * Returns the String associated with the APPROVE_BUTTON_TOOLTIP key.
     * @return the approve-button tooltip text, or {@code null} if not set.
     */
    public String getApproveButtonToolTip()
    {
        return getString(APPROVE_BUTTON_TOOLTIP);
    }

    /**
     * Sets the String associated with the APPROVE_BUTTON_TOOLTIP key.
     * @param toolTip the approve-button tooltip text.
     */
    public void setApproveButtonToolTip(String toolTip)
    {
        put(APPROVE_BUTTON_TOOLTIP, toolTip);
    }

    private static final Set<String> OWN_NAMES = Set.of(DIALOG_TITLE, APPROVE_BUTTON_TEXT, APPROVE_BUTTON_TOOLTIP);

    /**
     * Set the specified attribute name to the specified attribute value.
     * @param attributeName The name of the attribute.
     * @param attributeValue  The value of the attribute.
     * @throws IllegalArgumentException if {@code attributeName} is not a recognized attribute
     *         for this bundle type.
     */
    @Override
    public void setAttribute(String attributeName, Object attributeValue)
    {
        if (!OWN_NAMES.contains(attributeName))
        {
            super.setAttribute(attributeName, attributeValue);
            return;
        }
        switch (attributeName)
        {
            case DIALOG_TITLE           -> setDialogTitle((String) attributeValue);
            case APPROVE_BUTTON_TEXT    -> setApproveButtonText((String) attributeValue);
            case APPROVE_BUTTON_TOOLTIP -> setApproveButtonToolTip((String) attributeValue);
        }
    }

    /**
     * Returns the set of valid attribute names for a JFileChooserPropertyBundle.
     * @return the set of valid attribute names for a JFileChooserPropertyBundle.
     */
    @Override
    public Set<String> validNames()
    {
        Set<String> nameSet = super.validNames();
        nameSet.add(DIALOG_TITLE);
        nameSet.add(APPROVE_BUTTON_TEXT);
        nameSet.add(APPROVE_BUTTON_TOOLTIP);
        return nameSet;
    }
}
