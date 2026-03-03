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
 * Provides a collection of localizable property values for a JInternalFrame, extending JComponentPropertyBundle.
 */
public class JInternalFramePropertyBundle extends JComponentPropertyBundle
{
    /**
     * A key for the title for the JInternalFrame.
     */
    public static final String TITLE = "Title";

    /**
     * A key for the frame icon for the JInternalFrame.
     */
    public static final String FRAME_ICON = "FrameIcon";

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
     * Returns the Icon associated with the FRAME_ICON key.
     * @return the Icon associated with the FRAME_ICON key, or null if none is associated with it.
     */
    public Icon getFrameIcon()
    {
        return (Icon) get(FRAME_ICON);
    }

    /**
     * Sets the Icon associated with the FRAME_ICON key.
     * @param icon the Icon to be associated with the FRAME_ICON key.
     */
    public void setFrameIcon(Icon icon)
    {
        put(FRAME_ICON, icon);
    }

    private static final Set<String> OWN_NAMES = Set.of(TITLE, FRAME_ICON);

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
        if (attributeName.equals(TITLE))
        {
            setTitle((String) attributeValue);
            return;
        }
        BufferedImage image = BufferedImageResourceLoader.getBufferedImageResource((String) attributeValue);
        setFrameIcon(new ImageIcon(image));
    }

    /**
     * Returns the set of valid attribute names for a JInternalFramePropertyBundle.
     * @return the set of valid attribute names for a JInternalFramePropertyBundle.
     */
    @Override
    public HashSet<String> validNames()
    {
        HashSet<String> nameSet = super.validNames();
        nameSet.add(TITLE);
        nameSet.add(FRAME_ICON);
        return nameSet;
    }
}
