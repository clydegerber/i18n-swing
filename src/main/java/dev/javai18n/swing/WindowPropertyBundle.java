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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides a collection of localizable property values for a Window, extending ComponentPropertyBundle.
 */
public class WindowPropertyBundle extends ComponentPropertyBundle
{
    /** Creates a new WindowPropertyBundle. */
    public WindowPropertyBundle()
    {
    }

   /**
     * A key for the Icon Images for the Window.
     */
    public static final String ICON_IMAGES = "IconImages";

    /**
     * Returns an unmodifiable view of the List of Icon Images associated with the ICON_IMAGES key.
     * @return an unmodifiable view of the List of Icon Images associated with the ICON_IMAGES key.
     */
    public List<Image> getIconImages()
    {
        List<Image> list = (List<Image>) get(ICON_IMAGES);
        return list == null ? null : Collections.unmodifiableList(list);
    }

    /**
     * Sets the List of Icon Images associated with the ICON_IMAGES key.
     * @param imageList The List of Images to associate with the ICON_IMAGES key.
     */
    public void setIconImages(List<Image> imageList)
    {
        put(ICON_IMAGES, imageList);
    }

    private static final Set<String> OWN_NAMES = Set.of(ICON_IMAGES);

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
        if (attributeName.equals(ICON_IMAGES))
        {
            if (attributeValue instanceof List<?> list)
            {
                setIconImages((List<Image>) list);
            }
            else
            {
                ArrayList<Image> images = new ArrayList<>();
                if (attributeValue instanceof String[] array)
                {
                    for (String str : array)
                    {
                        addImageToList(str, images);
                    }
                }
                else if (attributeValue instanceof String str)
                {
                    addImageToList(str, images);
                }
                else
                {
                    throw new IllegalArgumentException("Unable to process image location of type " + attributeValue.getClass().getName());
                }
                setIconImages(images);
            }
            return;
        }
        throw new IllegalArgumentException("Unrecognized attribute name: " + attributeName);
    }

    /**
     * Construct a BufferedImage from the location specified by str and add it to the specified list of images.
     * @param str The location of the image.
     * @param images An ArrayList to which the constructed Image will be added.
     */
    protected void addImageToList(String str, ArrayList<Image> images)
    {
        BufferedImage image = BufferedImageResourceLoader.getBufferedImageResource(str);
        images.add(image);
    }

    /**
     * Returns the set of valid attribute names for a WindowPropertyBundle.
     * @return the set of valid attribute names for a WindowPropertyBundle.
     */
    @Override
    public Set<String> validNames()
    {
        Set<String> nameSet = super.validNames();
        nameSet.add(ICON_IMAGES);
        return nameSet;
    }
}
