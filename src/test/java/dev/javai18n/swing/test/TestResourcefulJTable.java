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
import javax.swing.table.DefaultTableModel;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.ResourcefulJTable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourcefulJTable.
 */
public class TestResourcefulJTable
{
    /**
     * Verify that initial properties are set from the default-locale bundle.
     */
    @Test
    public void testInitialProperties()
    {
        AppFrame source = AppFrame.create();
        ResourcefulJTable table = ResourcefulJTable.create(new Resource(source, "TestTable"));
        assertEquals("TestTable name", table.getName());
        assertEquals("Test table tooltip", table.getToolTipText());
        assertEquals("Test table accessible name", table.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that properties update when the application locale changes.
     */
    @Test
    public void testLocaleChange() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        ResourcefulJTable table = ResourcefulJTable.create(new Resource(source, "TestTable"));
        assertEquals("TestTable name", table.getName());

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals("TestTable nom", table.getName());
        assertEquals("Info-bulle tableau test", table.getToolTipText());
        assertEquals("Nom accessible tableau test", table.getAccessibleContext().getAccessibleName());
    }

    /**
     * Verify that a locale change triggers createDefaultColumnsFromModel(), rebuilding the column
     * header from the model's column definitions.
     *
     * <p>This test manually empties the column model after initial creation to simulate a stale
     * state, then triggers a locale change and verifies that the columns are fully restored from
     * the underlying TableModel — confirming that createDefaultColumnsFromModel() was called.</p>
     */
    @Test
    public void testColumnHeaderRefreshOnLocaleChange()
            throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Col1", "Col2", "Col3"}, 0);
        ResourcefulJTable table = ResourcefulJTable.create(new Resource(source, "TestTable"), model);
        assertEquals(3, table.getColumnCount());

        // Manually remove all columns to put the header in a stale state
        while (table.getColumnModel().getColumnCount() > 0)
        {
            table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
        }
        assertEquals(0, table.getColumnCount());

        // Locale change must call createDefaultColumnsFromModel(), restoring columns from the model
        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        assertEquals(3, table.getColumnCount());
    }

    /**
     * Verify that a ResourcefulJTable created with a TableModel correctly reflects the model
     * and still responds to locale changes.
     */
    @Test
    public void testWithModel() throws InterruptedException, InvocationTargetException
    {
        AppFrame source = AppFrame.create();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Name", "Size"}, 0);
        model.addRow(new Object[]{"file.txt", 1024});

        ResourcefulJTable table = ResourcefulJTable.create(new Resource(source, "TestTable"), model);
        assertEquals("TestTable name", table.getName());
        assertEquals(2, table.getColumnCount());
        assertEquals(1, table.getRowCount());
        assertEquals(model, table.getModel());

        source.setBundleLocale(Locale.FRANCE);
        SwingUtilities.invokeAndWait(() -> {});

        // Properties update; model reference and data are unchanged
        assertEquals("TestTable nom", table.getName());
        assertEquals(model, table.getModel());
        assertEquals(2, table.getColumnCount());
        assertEquals(1, table.getRowCount());
    }
}
