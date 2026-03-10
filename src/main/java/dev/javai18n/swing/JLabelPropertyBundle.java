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

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Provides a collection of localizable property values for an JLabel, extending JComponentPropertyBundle.
 */
public class JLabelPropertyBundle extends JComponentPropertyBundle
{
    /** Creates a new JLabelPropertyBundle. */
    public JLabelPropertyBundle()
    {
    }

    /**
     * A key for the Icon for the JLabel.
     */
    public static final String ICON = "Icon";

    /**
     * A key for the disabled Icon for the JLabel.
     */
    public static final String DISABLED_ICON = "DisabledIcon";

    /**
     * A key for the JLabel mnemonic.
     */
    public static final String MNEMONIC = "Mnemonic";

    /**
     * A key for the JLabel text.
     */
    public static final String TEXT = "Text";

    /**
     * Get the Icon associated with the ICON key.
     * @return the Icon associated with the ICON key, or null if none is associated with it.
     */
    public Icon getIcon()
    {
        return (Icon) get(ICON);
    }

    /**
     * Set the Icon associated with the ICON key.
     * @param icon the Icon to be associated with the ICON key.
     */
    public void setIcon(Icon icon)
    {
        put(ICON, icon);
    }

    /**
     * Get the Icon associated with the DISABLED_ICON key.
     * @return the Icon associated with the DISABLED_ICON key, or null if none is associated with it.
     */
    public Icon getDisabledIcon()
    {
        return (Icon) get(DISABLED_ICON);
    }

    /**
     * Set the Icon associated with the DISABLED_ICON key.
     * @param icon the Icon to be associated with the DISABLED_ICON key.
     */
    public void setDisabledIcon(Icon icon)
    {
        put(DISABLED_ICON, icon);
    }

    /**
     * Get the integer associated with the MNEMONIC key.
     * @return the integer associated with the MNEMONIC key, or {@code 0} if no mnemonic is set.
     */
    public int getMnemonic()
    {
        Object value = get(MNEMONIC);
        return null != value ? (int) value : 0;
    }

    /**
     * Set the Integer associated with the MNEMONIC key.
     * @param integer the int to be associated with the MNEMONIC key.
     */
    public void setMnemonic(int integer)
    {
        put(MNEMONIC, integer);
    }

    /**
     * Returns the String associated with the TEXT key.
     * @return the String associated with the TEXT key.
     */
    public String getText()
    {
        return getString(TEXT);
    }

    /**
     * Sets the String associated with the TEXT key.
     * @param text The String to associate with the TEXT key.
     */
    public void setText(String text)
    {
        put(TEXT, text);
    }

    private static final Set<String> OWN_NAMES = Set.of(TEXT, MNEMONIC, ICON, DISABLED_ICON);

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
        if (attributeName.equals(TEXT))
        {
            setText((String) attributeValue);
            return;
        }
        if (attributeName.equals(MNEMONIC))
        {
            setMnemonic((int) attributeValue);
            return;
        }
        BufferedImage image = BufferedImageResourceLoader.getBufferedImageResource((String) attributeValue);
        if (null == image) return;
        ImageIcon icon = new ImageIcon(image);
        if (attributeName.equals(ICON))
        {
            setIcon(icon);
            return;
        }
        setDisabledIcon(icon);
    }

    /**
     * Returns the set of valid attribute names for a JLabelPropertyBundle.
     * @return the set of valid attribute names for a JLabelPropertyBundle.
     */
    @Override
    public Set<String> validNames()
    {
        Set<String> nameSet = super.validNames();
        nameSet.add(TEXT);
        nameSet.add(ICON);
        nameSet.add(MNEMONIC);
        nameSet.add(DISABLED_ICON);
        return nameSet;
    }
}
