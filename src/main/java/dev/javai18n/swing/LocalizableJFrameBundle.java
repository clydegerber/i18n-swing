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

import java.util.ListResourceBundle;

/**
 * A ResourceBundle for the LocalizableJFrame class, provided here for documentation purposes only.
 * Extending classes will want to override the getContents() method to provide values for the LocalizableJFrame
 * as suggested in the comments.
 */
public class LocalizableJFrameBundle extends ListResourceBundle
{
    /**
     * Returns the localizable property objects for a {@link LocalizableJFrame}, keyed by
     * {@link LocalizableJFrame#FRAME_PROPERTIES_KEY}.  The default implementation returns an empty
     * {@link FramePropertyBundle}; extending classes should override this method to provide
     * locale-specific values.
     *
     * @return An array of {@code {key, value}} pairs for use by the {@code ListResourceBundle}
     *         superclass.
     */
    @Override
    protected Object[][] getContents()
    {
        FramePropertyBundle props = new FramePropertyBundle();
        /**
         * Extending classes may want to provide values for LocalizableJFrame attributes, e.g.:
         * props.setTitle("My title");                // From FramePropertyBundle
         * props.setIconImages(myIcons);              // From WindowPropertyBundle
         * props.setFont(myFont);                     // From ComponentPropertyBundle
         * props.setName("My name");                  // From ComponentPropertyBundle
         * props.setAccessibleName("Ac name");        // From Component PropertyBundle
         * props.setAccessibleDescription("Ac desc"); // From ComponentPropertyBundle
         * 
         * Extending class may also want to provide *ProperyBundle objects for the components contained
         * in the LocalizableJFrame, e.g.:
         * AbstractButtonPropertyBundle buttonProps = new AbstractButtonPropertyBundle();
         * buttonProps.setText("My text");
         *
         * And add to the array below:
         * , {"myButtonProps", buttonProps}
         */
        return new Object[][]
        {
            {LocalizableJFrame.FRAME_PROPERTIES_KEY, props}
        };
    }

}
