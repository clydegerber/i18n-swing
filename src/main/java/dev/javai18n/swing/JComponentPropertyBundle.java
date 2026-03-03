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
 * Provides a collection of localizable property values for a JComponent, extending ComponentPropertyBundle.
 */
public class JComponentPropertyBundle extends ComponentPropertyBundle
{
   /**
     * A key for the tool tip text for the Component.
     */
    public static final String TOOL_TIP_TEXT = "ToolTipText";

    /**
     * Returns the String associated with the TOOL_TIP_TEXT key.
     * @return the String associated with the TOOL_TIP_TEXT key.
     */
    public String getToolTipText()
    {
        return getString(TOOL_TIP_TEXT);
    }

    /**
     * Sets the String associated with the TOOL_TIP_TEXT key.
     * @param text The String to associate with the TOOL_TIP_TEXT key.
     */
    public void setToolTipText(String text)
    {
        put(TOOL_TIP_TEXT, text);
    }

    private static final Set<String> OWN_NAMES = Set.of(TOOL_TIP_TEXT);

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
        setToolTipText((String) attributeValue);
    }

    /**
     * Returns the set of valid attribute names for a JComponentPropertyBundle.
     * @return the set of valid attribute names for a JComponentPropertyBundle.
     */
    @Override
    public HashSet<String> validNames()
    {
        HashSet<String> nameSet = super.validNames();
        nameSet.add(TOOL_TIP_TEXT);
        return nameSet;
    }
}
