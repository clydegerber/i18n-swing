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
 * Provides a collection of localizable property values for a Frame, extending WindowPropertyBundle.
 */
public class FramePropertyBundle extends WindowPropertyBundle
{
    /** Creates a new FramePropertyBundle. */
    public FramePropertyBundle()
    {
    }

   /**
     * A key for the title for the Frame.
     */
    public static final String TITLE = "Title";

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

    private static final Set<String> OWN_NAMES = Set.of(TITLE);

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
        setTitle((String) attributeValue);
    }

    /**
     * Returns the set of valid attribute names for a FramePropertyBundle.
     * @return the set of valid attribute names for a FramePropertyBundle.
     */
    @Override
    public HashSet<String> validNames()
    {
        HashSet<String> nameSet = super.validNames();
        nameSet.add(TITLE);
        return nameSet;
    }
}
