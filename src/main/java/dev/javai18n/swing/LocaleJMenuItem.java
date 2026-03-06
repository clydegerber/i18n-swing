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

import java.text.Collator;
import java.util.Locale;
import dev.javai18n.core.Resource;

/**
 * A {@link ResourcefulJMenuItem} that represents an application locale, displaying the locale's
 * display name in the currently active locale followed by the locale's own self-display name in
 * brackets (e.g. {@code "French [français]"}).
 *
 * <p>Implements {@link Comparable} to support locale-aware sorting of menu items by display
 * name.  Instances are created via {@link #create(Locale, Resource)} and are typically added to a
 * locale-selection menu that is fully rebuilt on each locale change.</p>
 */
public class LocaleJMenuItem extends ResourcefulJMenuItem implements Comparable<LocaleJMenuItem>
{
    private static final long serialVersionUID = 1L;

    static
    {
        SwingModuleRegistrar.ensureRegistered();
    }

    /**
     * Create a LocaleJMenuItem for the specified Locale and Resource.
     *
     * <p>Unlike other {@code Resourceful} component factories, this method calls
     * {@link #updateLocaleSpecificValues()} directly rather than {@code initialize()}, so the
     * item is not registered as a locale-change listener.  This is intentional: locale menu items
     * are expected to be fully rebuilt whenever the application locale changes (e.g. via a
     * {@code populateLocaleMenu()} call), so persistent listener registration is unnecessary.</p>
     *
     * @param locale   The Locale this menu item represents.
     * @param resource The Resource holding Locale-specific values for this menu item.
     * @return A LocaleJMenuItem with its text and collator initialised for the current locale.
     */
    public static LocaleJMenuItem create(Locale locale, Resource resource)
    {
        LocaleJMenuItem menuItem = new LocaleJMenuItem(locale, resource);
        menuItem.updateLocaleSpecificValues();
        return menuItem;
    }

    /**
     * Construct a LocaleMenuItem for a specified Locale and Resource.
     * @param locale   The Locale this menu item represents.
     * @param resource The Resource holding Locale-specific values for this menu item.
     */
    protected LocaleJMenuItem(Locale locale, Resource resource)
    {
        super(resource);
        this.itemLocale = locale;
    }

    /**
     * Update Locale-specific values. If this menu item's text is blank, construct a text string using the
     * locale's name as represented in the current locale followed by the name as represented in it's own locale.
     */
    @Override
    public void updateLocaleSpecificValues()
    {
        super.updateLocaleSpecificValues();
        updateText();
        collator = Collator.getInstance(getResource().getSource().getBundleLocale());
    }

    /**
     * The Locale that this menu item represents.
     */
    private Locale itemLocale;

    /**
     * The collator for this object's locale.
     */
    private Collator collator;

    /**
     * Return the Locale this item represents.
     * @return The Locale this item represents.
     */
    public Locale getItemLocale()
    {
        return itemLocale;
    }

    /**
     * If no text has been supplied for the item, set it to the name of the locale in the current locale followed
     * by the name of the locale in that locale.
     */
    protected void updateText()
    {
        String text =  itemLocale.getDisplayName(getResource().getSource().getBundleLocale());
        if (text.isEmpty())
        {
            text = "ROOT";
        }
        else
        {
            text += " [" + itemLocale.getDisplayName(itemLocale) + "]";
        }
        setText(text);
    }

    /**
     * Compares the text of this object to the text of the specified other LocaleJMenuItem for the purposes of sorting.
     * @param other Another LocaleJMenuItem object to compare.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     */
    public int compareTo(LocaleJMenuItem other)
    {
        return collator.compare(this.getText(), other.getText());
    }

    /**
     * Returns the value from getText().
     * @return the value from getText().
     */
    public String toString()
    {
        return getText();
    }
}
