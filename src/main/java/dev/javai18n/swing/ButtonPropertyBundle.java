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
 * Provides a collection of localizable property values for a Button, extending ComponentPropertyBundle.
 */
public class ButtonPropertyBundle extends ComponentPropertyBundle
{
    /** Creates a new ButtonPropertyBundle. */
    public ButtonPropertyBundle()
    {
    }

   /**
     * A key for the label of the Button.
     */
    public static final String LABEL = "label";

    /**
     * Returns the String associated with the LABEL key.
     * @return the String associated with the LABEL key.
     */
    public String getLabel()
    {
        return getString(LABEL);
    }

    /**
     * Sets the String associated with the LABEL key.
     * @param label The String to associate with the LABEL key.
     */
    public void setLabel(String label)
    {
        put(LABEL, label);
    }

    private static final Set<String> OWN_NAMES = Set.of(LABEL);

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
        setLabel((String) attributeValue);
    }

    /**
     * Returns the set of valid attribute names for a ButtonPropertyBundle.
     * @return the set of valid attribute names for a ButtonPropertyBundle.
     */
    @Override
    public Set<String> validNames()
    {
        Set<String> nameSet = super.validNames();
        nameSet.add(LABEL);
        return nameSet;
    }
}
