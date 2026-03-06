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

import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import dev.javai18n.core.AttributeCollection;

/**
 * Provides a collection of localizable property values for a Component.
 *
 * <p>Properties are stored in a private map and accessed via typed getter and setter
 * methods.  Subclasses add their own keys by delegating to the protected
 * {@link #get(String)} and {@link #put(String, Object)} methods.</p>
 */
public class ComponentPropertyBundle implements AttributeCollection
{
    /** Creates a new ComponentPropertyBundle. */
    public ComponentPropertyBundle()
    {
    }

    /**
     * A key for the name of the Component.
     */
    public static final String NAME = "Name";

    /**
     * A key for the font of the Component.
     */
    public static final String FONT = "Font";

    /**
     * A key for the accessible name of the Component.
     */
    public static final String ACCESSIBLE_NAME = "AccessibleName";

    /**
     * A key for the accessible description of the Component.
     */
    public static final String ACCESSIBLE_DESCRIPTION = "AccessibleDescription";

    private final HashMap<String, Object> map = new HashMap<>();

    /**
     * Returns the value associated with the specified key, or {@code null} if no
     * mapping exists for the key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or {@code null}.
     */
    public Object get(String key)
    {
        return map.get(key);
    }

    /**
     * Associates the specified value with the specified key in this bundle,
     * replacing any previous mapping for the key.  Callers should prefer the typed
     * setter methods (e.g. {@link #setName(String)}) over calling this method directly.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    protected void put(String key, Object value)
    {
        map.put(key, value);
    }

    /**
     * Returns {@code true} if this bundle contains a mapping for the specified key.
     *
     * @param key The key whose presence is to be tested.
     * @return {@code true} if this bundle contains a mapping for the specified key.
     */
    public boolean containsKey(String key)
    {
        return map.containsKey(key);
    }

    /**
     * Returns the number of attribute mappings currently stored in this bundle.
     *
     * @return The number of attribute mappings in this bundle.
     */
    public int size()
    {
        return map.size();
    }

    /**
     * Returns the String associated with the specified key.
     * @param key The key to the desired String property.
     * @return The String associated with the specified key or null if not found.
     */
    public String getString(String key)
    {
        return (String) get(key);
    }

    /**
     * Returns the String associated with the COMPONENT_NAME key.
     * @return the String associated with the COMPONENT_NAME key.
     */
    public String getName()
    {
        return getString(NAME);
    }

    /**
     * Sets the String associated with the COMPONENT_NAME key.
     * @param name The String to associate with the COMPONENT_NAME key.
     */
    public void setName(String name)
    {
        put(NAME, name);
    }

    /**
     * Returns the Font associated with the FONT key.
     * @return the Font associated with the FONT key.
     */
    public Font getFont()
    {
        return (Font) get(FONT);
    }

    /**
     * Sets the Font associated with the FONT key.
     * @param font the Font to associate with the FONT key.
     */
    public void setFont(Font font)
    {
        put(FONT, font);
    }

    /**
     * Returns the String associated with the ACCESSIBLE_NAME key.
     * @return the String associated with the ACCESSIBLE_NAME key.
     */
    public String getAccessibleName()
    {
        return getString(ACCESSIBLE_NAME);
    }

    /**
     * Sets the String associated with the ACCESSIBLE_NAME key.
     * @param name The String to associate with the ACCESSIBLE_NAME key.
     */
    public void setAccessibleName(String name)
    {
        put(ACCESSIBLE_NAME, name);
    }

    /**
     * Returns the String associated with the ACCESSIBLE_DESCRIPTION key.
     * @return the String associated with the ACCESSIBLE_DESCRIPTION key.
     */
    public String getAccessibleDescription()
    {
        return getString(ACCESSIBLE_DESCRIPTION);
    }

    /**
     * Sets the String associated with the ACCESSIBLE_DESCRIPTION key.
     * @param description The String to associate with the ACCESSIBLE_DESCRIPTION key.
     */
    public void setAccessibleDescription(String description)
    {
        put(ACCESSIBLE_DESCRIPTION, description);
    }

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
        if (attributeName.equals(NAME))
        {
            setName((String) attributeValue);
            return;
        }
        if (attributeName.equals(ACCESSIBLE_NAME))
        {
            setAccessibleName((String) attributeValue);
            return;
        }
        if (attributeName.equals(ACCESSIBLE_DESCRIPTION))
        {
            setAccessibleDescription((String) attributeValue);
            return;
        }
        if (attributeName.equals(FONT))
        {
            if (attributeValue instanceof Font font)
            {
                setFont(font);
            }
            else
            {
                setFont(Font.decode((String) attributeValue));
            }
            return;
        }
        throw new IllegalArgumentException("Unrecognized attribute name: " + attributeName);
    }

    /**
     * Returns the set of valid attribute names for a ComponentPropertyBundle.
     * @return the set of valid attribute names for a ComponentPropertyBundle.
     */
    public HashSet<String> validNames()
    {
        HashSet<String> nameSet = new HashSet<>();
        nameSet.add(NAME);
        nameSet.add(FONT);
        nameSet.add(ACCESSIBLE_NAME);
        nameSet.add(ACCESSIBLE_DESCRIPTION);
        return nameSet;
    }
}
