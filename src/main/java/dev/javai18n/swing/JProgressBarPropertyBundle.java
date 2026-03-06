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
 * Provides a collection of localizable property values for a JProgressBar, extending JComponentPropertyBundle.
 */
public class JProgressBarPropertyBundle extends JComponentPropertyBundle
{
    /** Creates a new JProgressBarPropertyBundle. */
    public JProgressBarPropertyBundle()
    {
    }

    /**
     * A key for the progress display text for the JProgressBar.
     */
    public static final String STRING = "String";

    /**
     * Returns the String associated with the STRING key.
     * @return the String associated with the STRING key.
     */
    public String getProgressString()
    {
        return getString(STRING);
    }

    /**
     * Sets the String associated with the STRING key.
     * @param string The String to associate with the STRING key.
     */
    public void setProgressString(String string)
    {
        put(STRING, string);
    }

    private static final Set<String> OWN_NAMES = Set.of(STRING);

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
        setProgressString((String) attributeValue);
    }

    /**
     * Returns the set of valid attribute names for a JProgressBarPropertyBundle.
     * @return the set of valid attribute names for a JProgressBarPropertyBundle.
     */
    @Override
    public HashSet<String> validNames()
    {
        HashSet<String> nameSet = super.validNames();
        nameSet.add(STRING);
        return nameSet;
    }
}
