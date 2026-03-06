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
 * Provides a collection of localizable property values for a ResourcefulJOptionPane, extending
 * JComponentPropertyBundle with a dialog title and an array of localized button option labels.
 */
public class JOptionPanePropertyBundle extends JComponentPropertyBundle
{
    /** Creates a new JOptionPanePropertyBundle. */
    public JOptionPanePropertyBundle()
    {
    }

    /**
     * A key for the dialog title string.
     */
    public static final String TITLE = "Title";

    /**
     * A key for the array of localized button option label strings.
     */
    public static final String OPTIONS = "Options";

    /**
     * Returns the String associated with the TITLE key.
     * @return the String associated with the TITLE key.
     */
    public String getTitle()
    {
        return getString(TITLE);
    }

    /**
     * Sets the String associated with the TITLE key.
     * @param title The String to associate with the TITLE key.
     */
    public void setTitle(String title)
    {
        put(TITLE, title);
    }

    /**
     * Returns a defensive copy of the String array associated with the OPTIONS key.
     * @return a copy of the String array of button option labels, or null if none is set.
     */
    public String[] getOptions()
    {
        String[] options = (String[]) get(OPTIONS);
        return options == null ? null : options.clone();
    }

    /**
     * Sets the String array associated with the OPTIONS key.
     * @param options The String array of localized button option labels.
     */
    public void setOptions(String[] options)
    {
        put(OPTIONS, options);
    }

    private static final Set<String> OWN_NAMES = Set.of(TITLE, OPTIONS);

    /**
     * Set the specified attribute name to the specified attribute value.
     * @param attributeName The name of the attribute.
     * @param attributeValue  The value of the attribute.
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
            case TITLE -> setTitle((String) attributeValue);
            case OPTIONS -> setOptions((String[]) attributeValue);
        }
    }

    /**
     * Returns the set of valid attribute names for a JOptionPanePropertyBundle.
     * @return the set of valid attribute names for a JOptionPanePropertyBundle.
     */
    @Override
    public HashSet<String> validNames()
    {
        HashSet<String> nameSet = super.validNames();
        nameSet.add(TITLE);
        nameSet.add(OPTIONS);
        return nameSet;
    }
}
