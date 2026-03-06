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
 * Provides a collection of localizable property values for an AbstractButton, extending JComponentPropertyBundle.
 */
public class AbstractButtonPropertyBundle extends JComponentPropertyBundle
{
    /** Creates a new AbstractButtonPropertyBundle. */
    public AbstractButtonPropertyBundle()
    {
    }

    /**
     * A key for the Icon for the Button.
     */
    public static final String ICON = "Icon";

    /**
     * A key for the selected Icon for the Button.
     */
    public static final String SELECTED_ICON = "SelectedIcon";

    /**
     * A key for the pressed Icon for the Button.
     */
    public static final String PRESSED_ICON = "PressedIcon";

    /**
     * A key for the rollover Icon for the Button.
     */
    public static final String ROLLOVER_ICON = "RolloverIcon";

    /**
     * A key for the rollover selected Icon for the Button.
     */
    public static final String ROLLOVER_SELECTED_ICON = "RolloverSelectedIcon";

    /**
     * A key for the disabled Icon for the Button.
     */
    public static final String DISABLED_ICON = "DisabledIcon";

    /**
     * A key for the disabled selected Icon for the Button - that is, the file name or URL where the Icon is located.
     */
    public static final String DISABLED_SELECTED_ICON = "DisabledSelectedIcon";

    /**
     * A key for the Button mnemonic.
     */
    public static final String MNEMONIC = "Mnemonic";

    /**
     * A generic key for the Button text.
     */
    public static final String TEXT = "Text";

    /**
     * A key for the label of the Button.
     */
    public static final String LABEL = "Label";

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
     * Get the Icon associated with the PRESSED_ICON key.
     * @return the Icon associated with the PRESSED_ICON key, or null if none is associated with it.
     */
    public Icon getPressedIcon()
    {
        return (Icon) get(PRESSED_ICON);
    }

    /**
     * Set the Icon associated with the PRESSED_ICON key.
     * @param icon the Icon to be associated with the PRESSED_ICON key.
     */
    public void setPressedIcon(Icon icon)
    {
        put(PRESSED_ICON, icon);
    }

    /**
     * Get the Icon associated with the SELECTED_ICON key.
     * @return the Icon associated with the SELECTED_ICON key, or null if none is associated with it.
     */
    public Icon getSelectedIcon()
    {
        return (Icon) get(SELECTED_ICON);
    }

    /**
     * Set the Icon associated with the SELECTED_ICON key.
     * @param icon the Icon to be associated with the SELECTED_ICON key.
     */
    public void setSelectedIcon(Icon icon)
    {
        put(SELECTED_ICON, icon);
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
     * Get the Icon associated with the DISABLED_SELECTED_ICON key.
     * @return the Icon associated with the DISABLED_SELECTED_ICON key, or null if none is associated with it.
     */
    public Icon getDisabledSelectedIcon()
    {
        return (Icon) get(DISABLED_SELECTED_ICON);
    }

    /**
     * Set the Icon associated with the DISABLED_SELECTED_ICON key.
     * @param icon the Icon to be associated with the DISABLED_SELECTED_ICON key.
     */
    public void setDisabledSelectedIcon(Icon icon)
    {
        put(DISABLED_SELECTED_ICON, icon);
    }

    /**
     * Get the Icon associated with the ROLLOVER_ICON key.
     * @return the Icon associated with the ROLLOVER_ICON key, or null if none is associated with it.
     */
    public Icon getRolloverIcon()
    {
        return (Icon) get(ROLLOVER_ICON);
    }

    /**
     * Set the Icon associated with the ROLLOVER_ICON key.
     * @param icon the Icon to be associated with the ROLLOVER_ICON key.
     */
    public void setRolloverIcon(Icon icon)
    {
        put(ROLLOVER_ICON, icon);
    }

    /**
     * Get the Icon associated with the ROLLOVER_SELECTED_ICON key.
     * @return the Icon associated with the ROLLOVER_SELECTED_ICON key, or null if none is associated with it.
     */
    public Icon getRolloverSelectedIcon()
    {
        return (Icon) get(ROLLOVER_SELECTED_ICON);
    }

    /**
     * Set the Icon associated with the ROLLOVER_SELECTED_ICON key.
     * @param icon the Icon to be associated with the ROLLOVER_SELECTED_ICON key.
     */
    public void setRolloverSelectedIcon(Icon icon)
    {
        put(ROLLOVER_SELECTED_ICON, icon);
    }

    /**
     * Get the integer associated with the MNEMONIC key.
     * @return the integer associated with the MNEMONIC key, or {@code 0} if no mnemonic is set.
     */
    public int getMnemonic()
    {
        Object value = get(MNEMONIC);
        return value != null ? (int) value : 0;
    }

    /**
     * Set the integer associated with the MNEMONIC key.
     * @param integer the integer object to be associated with the MNEMONIC key.
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

    private static final Set<String> OWN_NAMES = Set.of(
        TEXT, LABEL, MNEMONIC,
        ICON, PRESSED_ICON, ROLLOVER_ICON, SELECTED_ICON,
        DISABLED_ICON, DISABLED_SELECTED_ICON, ROLLOVER_SELECTED_ICON);

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
        if (attributeName.equals(LABEL))
        {
            setLabel((String) attributeValue);
            return;
        }
        if (attributeName.equals(MNEMONIC))
        {
            setMnemonic((int) attributeValue);
            return;
        }
        BufferedImage image = BufferedImageResourceLoader.getBufferedImageResource((String) attributeValue);
        ImageIcon icon = new ImageIcon(image);
        switch (attributeName)
        {
            case ICON                  -> setIcon(icon);
            case PRESSED_ICON          -> setPressedIcon(icon);
            case ROLLOVER_ICON         -> setRolloverIcon(icon);
            case SELECTED_ICON         -> setSelectedIcon(icon);
            case DISABLED_ICON         -> setDisabledIcon(icon);
            case DISABLED_SELECTED_ICON  -> setDisabledSelectedIcon(icon);
            case ROLLOVER_SELECTED_ICON  -> setRolloverSelectedIcon(icon);
        }
    }

    /**
     * Returns the set of valid attribute names for an AbstractButtonPropertyBundle.
     * @return the set of valid attribute names for an AbstractButtonPropertyBundle.
     */
    @Override
    public Set<String> validNames()
    {
        Set<String> nameSet = super.validNames();
        nameSet.add(TEXT);
        nameSet.add(ICON);
        nameSet.add(LABEL);
        nameSet.add(MNEMONIC);
        nameSet.add(PRESSED_ICON);
        nameSet.add(ROLLOVER_ICON);
        nameSet.add(SELECTED_ICON);
        nameSet.add(DISABLED_ICON);
        nameSet.add(DISABLED_SELECTED_ICON);
        nameSet.add(ROLLOVER_SELECTED_ICON);
        return nameSet;
    }
}
