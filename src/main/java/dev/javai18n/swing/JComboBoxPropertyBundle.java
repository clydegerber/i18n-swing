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
 * Provides a collection of localizable property values for an JComboBox, extending JComponentPropertyBundle.
 */
public class JComboBoxPropertyBundle extends JComponentPropertyBundle
{
    /** Creates a new JComboBoxPropertyBundle. */
    public JComboBoxPropertyBundle()
    {
    }

    /**
     * A key for the String array of possible values for the JComboBox.
     */
    public static final String VALUES = "Values";

    /**
     * Get a defensive copy of the string array associated with the VALUES key.
     * @return a copy of the String array associated with the VALUES key, or null if none is associated with it.
     */
    public String[] getValues()
    {
        String[] values = (String[]) get(VALUES);
        return values == null ? null : values.clone();
    }

    /**
     * Set the Values associated with the VALUES key.
     * @param values the String array of values to be associated with the VALUES key.
     */
    public void setValues(String[] values)
    {
        put(VALUES, values);
    }

    private static final Set<String> OWN_NAMES = Set.of(VALUES);

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
        setValues((String[]) attributeValue);
    }

    /**
     * Returns the set of valid attribute names for a JComboBoxPropertyBundle.
     * @return the set of valid attribute names for a JComboBoxPropertyBundle.
     */
    @Override
    public Set<String> validNames()
    {
        Set<String> nameSet = super.validNames();
        nameSet.add(VALUES);
        return nameSet;
    }
}
