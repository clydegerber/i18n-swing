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

package dev.javai18n.swing.test;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourcefulJTree.
 */
public class TestResourcefulJTree
{
    /**
     * Verify that initial properties are set from the default-locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJTree tree = ResourcefulJTree.create(new Resource(source, "TestTree"));
        assertEquals("TestTree name", tree.getName());
        assertEquals("Test tree tooltip", tree.getToolTipText());
        assertEquals("Test tree accessible name", tree.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that properties update when the application locale changes.
     */
    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        ResourcefulJTree tree = ResourcefulJTree.create(new Resource(source, "TestTree"));
        assertEquals("TestTree name", tree.getName());

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals("TestTree nom", tree.getName());
        assertEquals("Info-bulle arbre test", tree.getToolTipText());
        assertEquals("Nom accessible arbre test", tree.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that a ResourcefulJTree created with a TreeModel correctly reflects the model
     * and still responds to locale changes.
     */
    @Test
    public void testWithModel() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        root.add(new DefaultMutableTreeNode("Child A"));
        root.add(new DefaultMutableTreeNode("Child B"));
        DefaultTreeModel model = new DefaultTreeModel(root);

        ResourcefulJTree tree = ResourcefulJTree.create(new Resource(source, "TestTree"), model);
        assertEquals("TestTree name", tree.getName());
        assertEquals(model, tree.getModel());

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        // Properties update; model reference is unchanged
        assertEquals("TestTree nom", tree.getName());
        assertEquals(model, tree.getModel());
    }
}
