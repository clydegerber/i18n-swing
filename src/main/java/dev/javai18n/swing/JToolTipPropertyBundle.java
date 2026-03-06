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
 * Provides a collection of localizable property values for a {@link ResourcefulJToolTip},
 * extending {@link JComponentPropertyBundle} with the tip text — the primary locale-sensitive
 * content of a {@link javax.swing.JToolTip}.
 *
 * <p>Note that {@code ToolTipText} (inherited from {@link JComponentPropertyBundle}) sets a
 * secondary tooltip displayed when the cursor hovers over the {@code JToolTip} itself, which is
 * rarely used in practice. {@code TipText} is the text shown inside the tooltip balloon.</p>
 */
public class JToolTipPropertyBundle extends JComponentPropertyBundle
{
    /** Creates a new JToolTipPropertyBundle. */
    public JToolTipPropertyBundle()
    {
    }

    /**
     * A key for the text displayed inside the tooltip balloon (see
     * {@link javax.swing.JToolTip#setTipText}).
     */
    public static final String TIP_TEXT = "TipText";

    /**
     * Returns the String associated with the TIP_TEXT key.
     * @return the tip text, or {@code null} if not set.
     */
    public String getTipText()
    {
        return getString(TIP_TEXT);
    }

    /**
     * Sets the String associated with the TIP_TEXT key.
     * @param tipText the text to display inside the tooltip balloon.
     */
    public void setTipText(String tipText)
    {
        put(TIP_TEXT, tipText);
    }

    private static final Set<String> OWN_NAMES = Set.of(TIP_TEXT);

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
        setTipText((String) attributeValue);
    }

    /**
     * Returns the set of valid attribute names for a JToolTipPropertyBundle.
     * @return the set of valid attribute names for a JToolTipPropertyBundle.
     */
    @Override
    public Set<String> validNames()
    {
        Set<String> nameSet = super.validNames();
        nameSet.add(TIP_TEXT);
        return nameSet;
    }
}
