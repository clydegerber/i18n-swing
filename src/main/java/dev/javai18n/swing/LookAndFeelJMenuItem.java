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

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import dev.javai18n.core.Resource;

/**
 * A {@link ResourcefulJMenuItem} that represents a Swing Look and Feel, displaying the L&amp;F name
 * as its text.
 *
 * <p>The text is sourced from {@link UIManager.LookAndFeelInfo#getName()}, and is used only if no
 * text is provided in the resource bundle.  Instances are created via
 * {@link #create(UIManager.LookAndFeelInfo, Resource)} and are typically added to a "Look and
 * Feel" submenu.  The {@link #toString()} method returns the L&amp;F's {@code toString()} value,
 * suitable for logging or display in a combo box.</p>
 */
public class LookAndFeelJMenuItem extends ResourcefulJMenuItem
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a LookAndFeelJMenuItem for the specified LookAndFeelInfo and Resource.
     * @param laf      The {@link UIManager.LookAndFeelInfo} this menu item represents.
     * @param resource The Resource holding Locale-specific values for this menu item.
     * @return A LookAndFeelJMenuItem registered to listen to LocaleEvents from the resource's
     *         source.
     */
    public static LookAndFeelJMenuItem create(UIManager.LookAndFeelInfo laf, Resource resource)
    {
        LookAndFeelJMenuItem menuItem = new LookAndFeelJMenuItem(laf, resource);
        menuItem.setHorizontalAlignment(SwingConstants.LEFT);
        menuItem.initialize();
        return menuItem;
    }

    /**
     * Construct a LookAndFeelMenuItem for a specified LookAndFeelInfo and Resource.
     * @param laf      The LookAndFeelInfo this menu item represents.
     * @param resource The Resource holding Locale-specific values for this menu item.
     */
    protected LookAndFeelJMenuItem(UIManager.LookAndFeelInfo laf, Resource resource)
    {
        super(resource);
        this.itemLookAndFeel = laf;
    }

    /**
     * The LookAndFeelInfo that this menu item represents.
     */
    private UIManager.LookAndFeelInfo itemLookAndFeel;

    /**
     * Return the Locale this item represents.
     * @return The Locale this item represents.
     */
    public UIManager.LookAndFeelInfo getItemLookAndFeel()
    {
        return itemLookAndFeel;
    }

    /**
     * Update Locale-specific values. If this menu item's text is blank, construct a text string using the
     * locale's name as represented in the current locale followed by the name as represented in it's own locale.
     */
    @Override
    protected void updateLocaleSpecificValues()
    {
        super.updateLocaleSpecificValues();
        updateText();
    }

    /**
     * If no text has been supplied for the item, set it to the name of the locale in the current locale followed
     * by the name of the locale in that locale.
     */
    protected void updateText()
    {
        // Null check required: called during superclass construction before itemLookAndFeel is set.
        if (null != itemLookAndFeel && getText().isEmpty())
        {
            String text = itemLookAndFeel.getName();
            setText(text);
        }
    }

    /**
     * Returns the value from the item's LookAndFeelInfo.toString().
     * @return the value from the item's LookAndFeelInfo.toString().
     */
    public String toString()
    {
        return itemLookAndFeel.toString();
    }
}
