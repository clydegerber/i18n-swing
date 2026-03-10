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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.Collator;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import dev.javai18n.swing.ResourcefulJDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import dev.javai18n.core.Resource;
import dev.javai18n.swing.AbstractButtonPropertyBundle;
import dev.javai18n.swing.JLabelPropertyBundle;
import dev.javai18n.swing.LocaleJMenuItem;
import dev.javai18n.swing.LocalizableJFrame;
import dev.javai18n.swing.LookAndFeelJMenuItem;
import dev.javai18n.swing.ResourcefulJButton;
import dev.javai18n.swing.ResourcefulJCheckBox;
import dev.javai18n.swing.ResourcefulJCheckBoxMenuItem;
import dev.javai18n.swing.ResourcefulJEditorPane;
import dev.javai18n.swing.ResourcefulJFormattedTextField;
import dev.javai18n.swing.ResourcefulJInternalFrame;
import dev.javai18n.swing.ResourcefulJLabel;
import dev.javai18n.swing.ResourcefulJList;
import dev.javai18n.swing.ResourcefulJMenu;
import dev.javai18n.swing.ResourcefulJMenuBar;
import dev.javai18n.swing.ResourcefulJMenuItem;
import dev.javai18n.swing.ResourcefulJOptionPane;
import dev.javai18n.swing.ResourcefulJPanel;
import dev.javai18n.swing.ResourcefulJPasswordField;
import dev.javai18n.swing.ResourcefulJPopupMenu;
import dev.javai18n.swing.ResourcefulJRadioButtonMenuItem;
import dev.javai18n.swing.ResourcefulJScrollPane;
import dev.javai18n.swing.ResourcefulJSeparator;
import dev.javai18n.swing.ResourcefulJSlider;
import dev.javai18n.swing.ResourcefulJSpinner;
import dev.javai18n.swing.ResourcefulJSplitPane;
import dev.javai18n.swing.ResourcefulJTable;
import dev.javai18n.swing.ResourcefulJTabbedPane;
import dev.javai18n.swing.ResourcefulJTextArea;
import dev.javai18n.swing.ResourcefulJTextField;
import dev.javai18n.swing.ResourcefulJTextPane;
import dev.javai18n.swing.ResourcefulJToggleButton;
import dev.javai18n.swing.ResourcefulJToolBar;
import dev.javai18n.swing.ResourcefulJTree;
import dev.javai18n.swing.SwingLogger;

/**
 * A file explorer AppFrame that showcases all localized Swing component types.
 */
public class AppFrame extends LocalizableJFrame implements ActionListener
{
    static
    {
        SwingTestModuleRegistrar.ensureRegistered();
    }

    /**
     * Create and initialize a new AppFrame.
     * @return An AppFrame with locale-sensitive attributes updated from an associated ResourceBundle.
     */
    public static AppFrame create()
    {
        AppFrame frame = new AppFrame();

        frame.updateLocaleSpecificValues();

        frame.initialize();

        return frame;
    }

    /**
     * Construct an AppFrame
     */
    protected AppFrame()
    {
    }

    // --- Menus ---
    ResourcefulJMenuBar menuBar;
    ResourcefulJMenu fileMenu, editMenu, viewMenu, preferencesMenu, helpMenu;
    ResourcefulJMenu localeMenu, lookAndFeelMenu, sortByMenu, localeSetMenu, loggerLocaleMenu;

    // --- Locale menu item cache ---
    /** Cached LocaleJMenuItem objects, reused across locale changes to avoid repeated construction. */
    private final List<LocaleJMenuItem> localeMenuItems = new ArrayList<>();
    /** Whether the cache was built for all-locales mode (true) or logger-locales mode (false). */
    private boolean localeMenuBuiltForAllLocales = false;

    // --- View state ---
    private boolean useAllLocales = false;
    private boolean useCurrentLocaleForLogging = false;
    private boolean showHiddenFiles = false;
    private boolean showFileExtensions = true;
    private String sortBy = "name";
    private boolean dirsOnly = false;
    private String fileType = "all";

    // --- Navigation state ---
    private File currentDirectory;
    private final List<File> navigationHistory = new ArrayList<>();
    private int historyIndex = -1;
    /** True while navigateTo/Back/Forward is programmatically updating the tree selection,
     *  preventing the TreeSelectionListener from triggering a redundant navigateTo call. */
    private boolean updatingTreeSelection = false;
    /** Cache of computed recursive directory sizes. */
    private final java.util.HashMap<File, Long> dirSizeCache = new java.util.HashMap<>();
    private SwingWorker<Void, long[]> dirSizeWorker;
    private JLabel sizeStatusLabel;
    /** MessageFormat caches keyed by bundle key; cleared when locale changes. */
    private Locale formatCacheLocale;
    private final java.util.HashMap<String, MessageFormat> bundleTextFormatCache = new java.util.HashMap<>();
    private final java.util.HashMap<String, MessageFormat> bundleStringFormatCache = new java.util.HashMap<>();
    /** NumberFormat for formatSize(); recreated when locale changes. */
    private NumberFormat sizeNumberFormat;
    /** Background worker for directory listing; cancelled on re-navigation. */
    private SwingWorker<List<File>, Void> fileTableWorker;

    // --- UI components ---
    private ResourcefulJTree directoryTree;
    private DefaultTreeModel treeModel;
    private ResourcefulJTable fileTable;
    private FileTableModel fileTableModel;
    private ResourcefulJTextField pathTextField;
    private ResourcefulJTextArea previewTextArea;
    private ResourcefulJEditorPane previewEditorPane;
    private ResourcefulJLabel fileCountLabel;
    private ResourcefulJTabbedPane contentTabbedPane;
    private ResourcefulJCheckBox readOnlyCheckBox;
    private ResourcefulJLabel fileNameLabel, fileSizeLabel, fileTypeLabel, fileModifiedLabel, filePathLabel;
    private ResourcefulJButton backButton, forwardButton, upButton;
    private ResourcefulJToggleButton listViewToggle, detailViewToggle;
    private ResourcefulJPanel previewCardPanel;
    private DefaultListModel<File> fileListModel;
    private ResourcefulJList<File> fileListView;
    private ResourcefulJPanel fileViewCard;

    // --- Sort radio buttons ---
    private ResourcefulJRadioButtonMenuItem sortByNameRadio, sortBySizeRadio, sortByDateRadio;

    // --- View checkboxes ---
    private ResourcefulJCheckBoxMenuItem showHiddenCheckBox, showExtensionsCheckBox;

    // --- Locale set radio buttons ---
    private ResourcefulJRadioButtonMenuItem loggerLocalesRadio, allLocalesRadio;
    private ResourcefulJRadioButtonMenuItem jvmDefaultLocaleRadio, currentLocaleRadio;

    // --- Internal frame for properties ---
    private ResourcefulJInternalFrame infoInternalFrame;
    private ResourcefulJLabel infoContentLabel;

    // --- Dialogs (ResourcefulJOptionPane instead of JOptionPane statics) ---
    private ResourcefulJOptionPane newFolderDialog;
    private ResourcefulJOptionPane deleteConfirmDialog;

    // --- Widgets demo tab components ---
    private ResourcefulJPasswordField demoPasswordField;
    private ResourcefulJSpinner demoSpinner;
    private ResourcefulJFormattedTextField demoFormattedTextField;
    private ResourcefulJSlider demoSlider;
    private ResourcefulJList<String> demoList;
    private DefaultListModel<String> demoListModel;
    private ResourcefulJTextPane demoTextPane;
    private ResourcefulJPopupMenu demoPopupMenu;

    // Sentinel for lazy-loaded tree nodes (not a user-visible string)
    private static final Object LOADING_MARKER = new Object();

    private String getBundleString(String key)
    {
        return getResourceBundle().getString(key);
    }

    private String getBundleText(String key)
    {
        Object bundle = getResourceBundle().getObject(key);
        if (bundle instanceof AbstractButtonPropertyBundle b)
        {
            return b.getText();
        }
        if (bundle instanceof JLabelPropertyBundle b)
        {
            return b.getText();
        }
        throw new IllegalArgumentException("Bundle key '" + key + "' does not have a Text property");
    }

    private void checkFormatCacheLocale()
    {
        if (!getBundleLocale().equals(formatCacheLocale))
        {
            bundleTextFormatCache.clear();
            bundleStringFormatCache.clear();
            sizeNumberFormat = null;
            formatCacheLocale = getBundleLocale();
        }
    }

    private String formatBundleText(String key, Object... args)
    {
        checkFormatCacheLocale();
        MessageFormat mf = bundleTextFormatCache.computeIfAbsent(key,
                k -> new MessageFormat(getBundleText(k), getBundleLocale()));
        return mf.format(args);
    }

    private String formatBundleString(String key, Object... args)
    {
        checkFormatCacheLocale();
        MessageFormat mf = bundleStringFormatCache.computeIfAbsent(key,
                k -> new MessageFormat(getResourceBundle().getString(k), getBundleLocale()));
        return mf.format(args);
    }

    /**
     * The supported Locales for the application.
     * @return An array of supported Locales.
     */
    @Override
    public Locale[] getAvailableLocales()
    {
        if (useAllLocales)
        {
            return Locale.getAvailableLocales();
        }
        return SwingLogger.SWING_LOGGER.getAvailableLocales();
    }

    /**
     * Populate the items in the localeMenu.
     */
    public void populateLocaleMenu()
    {
        boolean needRebuild = localeMenuItems.isEmpty() || (useAllLocales != localeMenuBuiltForAllLocales);
        if (needRebuild)
        {
            for (LocaleJMenuItem item : localeMenuItems)
            {
                item.dispose();
            }
            localeMenuItems.clear();
            localeMenuBuiltForAllLocales = useAllLocales;
            Resource localeMenuItemResource = new Resource(this, "LocaleMenuItemProps");
            for (Locale locale : getAvailableLocales())
            {
                LocaleJMenuItem localeItem = LocaleJMenuItem.create(locale, localeMenuItemResource);
                localeItem.addActionListener(this);
                localeItem.setMinimumSize(new Dimension(10, 25));
                localeMenuItems.add(localeItem);
            }
        }
        else
        {
            for (LocaleJMenuItem item : localeMenuItems)
            {
                item.updateLocaleSpecificValues();
            }
        }

        Collator sortCollator = Collator.getInstance(getBundleLocale());
        localeMenuItems.sort((a, b) -> sortCollator.compare(a.getText(), b.getText()));

        Locale current = getBundleLocale();
        localeMenu.removeAll();
        for (LocaleJMenuItem item : localeMenuItems)
        {
            item.setSelected(item.getItemLocale().equals(current));
            localeMenu.add(item);
        }
    }

    public void populateLookAndFeelMenu()
    {
        Resource lookAndFeelMenuItemResource = new Resource(this, "LookAndFeelMenuItemProps");
        UIManager.LookAndFeelInfo[] lafInfos = getInstalledLookAndFeels();
        for (var lafInfo : lafInfos)
        {
            LookAndFeelJMenuItem lafMenuItem = LookAndFeelJMenuItem.create(lafInfo, lookAndFeelMenuItemResource);
            lookAndFeelMenu.add(lafMenuItem);
            lafMenuItem.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (LookAndFeelJMenuItem.class.isAssignableFrom(source.getClass()))
        {
            LookAndFeelJMenuItem lafItem = (LookAndFeelJMenuItem) source;
            try
            {
                UIManager.setLookAndFeel(lafItem.getItemLookAndFeel().getClassName());
            }
            catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex)
            {
                System.getLogger(AppFrame.class.getName()).log(System.Logger.Level.ERROR, "Failed to set Look and Feel", ex);
            }
            SwingUtilities.updateComponentTreeUI(this);
            pack();
        }
        else if (LocaleJMenuItem.class.isAssignableFrom(source.getClass()))
        {
            LocaleJMenuItem localeItem = (LocaleJMenuItem) source;
            Locale newLocale = localeItem.getItemLocale();
            if (!newLocale.equals(getBundleLocale()))
            {
                setBundleLocale(newLocale);
                populateLocaleMenu();
                if (useCurrentLocaleForLogging)
                {
                    SwingLogger.SWING_LOGGER.setBundleLocale(newLocale);
                }
                refreshFileTable();
                updateWidgetsForLocale(newLocale);
                validate();
                pack();
            }
        }
        else if (cmd != null)
        {
            handleCommand(cmd);
        }
    }

    /**
     * Update widgets whose locale-sensitive content (beyond the resource bundle) must be refreshed
     * programmatically after a locale change.
     */
    private void updateWidgetsForLocale(Locale locale)
    {
        // Refresh demoList with day names in the new locale
        if (demoListModel != null)
        {
            populateDemoList(locale);
        }

        // Reinstall demoSpinner editor with new locale's date format
        if (demoSpinner != null)
        {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            String pattern = ((SimpleDateFormat) df).toPattern();
            // JSpinner.DateEditor is used directly (not ResourcefulJSpinnerDateEditor)
            // because the editor is fully recreated here on every locale change with a
            // fresh locale-specific format pattern.  A Resourceful wrapper would add
            // lifecycle complexity for a component that is immediately discarded.
            JSpinner.DateEditor editor = new JSpinner.DateEditor(demoSpinner, pattern);
            // Override the formatter factory with one that uses the explicit locale-aware
            // DateFormat, because JSpinner.DateEditor internally creates a SimpleDateFormat
            // from the pattern using the spinner's locale at construction time, which may
            // not yet reflect the new locale.
            ((JSpinner.DefaultEditor) editor).getTextField().setFormatterFactory(
                    new javax.swing.text.DefaultFormatterFactory(
                            new javax.swing.text.DateFormatter(df)));
            demoSpinner.setEditor(editor);
        }

        // Reinstall demoFormattedTextField formatter with new locale's date format
        if (demoFormattedTextField != null)
        {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
            // javax.swing.text.DefaultFormatterFactory / DateFormatter are formatter
            // strategy objects, not UI components; no Resourceful wrapper exists for them.
            demoFormattedTextField.setFormatterFactory(
                    new javax.swing.text.DefaultFormatterFactory(
                            new javax.swing.text.DateFormatter(df)));
            demoFormattedTextField.setValue(new Date());
        }

        // Refresh demoSlider tick label (accessible value text uses locale)
        if (demoSlider != null)
        {
            demoSlider.setToolTipText(formatBundleString("DemoSliderTooltipFormat", demoSlider.getValue()));
            updateSliderLabels(locale);
        }

        // Update text pane content with a locale-aware note
        if (demoTextPane != null)
        {
            refreshTextPaneContent(locale);
        }
    }

    private void handleCommand(String cmd)
    {
        switch (cmd)
        {
            case "exit":
                dispose();
                break;
            case "newFolder":
                createNewFolder();
                break;
            case "delete":
                deleteSelected();
                break;
            case "listView":
                ((CardLayout) fileViewCard.getLayout()).show(fileViewCard, "list");
                break;
            case "detailView":
                ((CardLayout) fileViewCard.getLayout()).show(fileViewCard, "detail");
                break;
            case "properties":
                if (fileTable.getSelectedRow() >= 0)
                {
                    showProperties(fileTableModel.getFile(fileTable.getSelectedRow()));
                    contentTabbedPane.setSelectedIndex(2);
                }
                break;
            case "refresh":
                refreshFileTable();
                break;
            case "about":
                showAboutDialog();
                break;
            case "back":
                navigateBack();
                break;
            case "forward":
                navigateForward();
                break;
            case "up":
                navigateUp();
                break;
            case "home":
                navigateTo(new File(System.getProperty("user.home")));
                break;
            case "sortByName":
                sortBy = "name";
                refreshFileTable();
                break;
            case "sortBySize":
                sortBy = "size";
                refreshFileTable();
                break;
            case "sortByDate":
                sortBy = "date";
                refreshFileTable();
                break;
            case "showHidden":
                showHiddenFiles = showHiddenCheckBox.isSelected();
                refreshFileTable();
                break;
            case "showExtensions":
                showFileExtensions = showExtensionsCheckBox.isSelected();
                refreshFileTable();
                break;
            case "loggerLocales":
                useAllLocales = false;
                populateLocaleMenu();
                break;
            case "allLocales":
                useAllLocales = true;
                populateLocaleMenu();
                break;
            case "jvmDefaultLocale":
                useCurrentLocaleForLogging = false;
                SwingLogger.SWING_LOGGER.setBundleLocale(Locale.getDefault());
                break;
            case "currentLocale":
                useCurrentLocaleForLogging = true;
                SwingLogger.SWING_LOGGER.setBundleLocale(getBundleLocale());
                break;
            case "allFiles":
                dirsOnly = false;
                refreshFileTable();
                break;
            case "dirsOnly":
                dirsOnly = true;
                refreshFileTable();
                break;
            case "allFileTypes":
                fileType = "all";
                refreshFileTable();
                break;
            case "textFiles":
                fileType = "text";
                refreshFileTable();
                break;
            case "imageFiles":
                fileType = "image";
                refreshFileTable();
                break;
            default:
                break;
        }
    }

    /**
     * Initialize the frame elements.
     */
    protected void initialize()
    {
        setLayout(new BorderLayout());

        initMenuBar();
        initToolBar();
        initPathBar();
        initContentArea();
        initStatusBar();

        // JFrame is used only for its EXIT_ON_CLOSE constant; LocalizableJFrame
        // (which AppFrame extends) does not redeclare window-operation constants.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // On macOS the "File Explorer > About File Explorer" native menu item fires
        // the Desktop APP_ABOUT handler.  Register showAboutDialog() so both that
        // item and Help > About show the same AppDialog in the current locale.
        // The handler is called on the macOS event thread, so dispatch to the EDT.
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_ABOUT))
        {
            Desktop.getDesktop().setAboutHandler(e -> SwingUtilities.invokeLater(this::showAboutDialog));
        }

        navigateTo(new File(System.getProperty("user.home")));
    }

    private void initMenuBar()
    {
        menuBar = ResourcefulJMenuBar.create(new Resource(this, "MenuBarProps"));

        // --- File menu ---
        fileMenu = ResourcefulJMenu.create(new Resource(this, "FileMenuProps"));
        addMenuItem(fileMenu, "NewFolderMenuItemProps", "newFolder");
        addMenuItem(fileMenu, "DeleteMenuItemProps", "delete");
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "PropertiesMenuItemProps", "properties");
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "ExitMenuItemProps", "exit");
        menuBar.add(fileMenu);

        // --- Edit menu ---
        editMenu = ResourcefulJMenu.create(new Resource(this, "EditMenuProps"));
        addMenuItem(editMenu, "CopyMenuItemProps", "copy");
        addMenuItem(editMenu, "CutMenuItemProps", "cut");
        addMenuItem(editMenu, "PasteMenuItemProps", "paste");
        addMenuItem(editMenu, "SelectAllMenuItemProps", "selectAll");
        menuBar.add(editMenu);

        // --- View menu ---
        viewMenu = ResourcefulJMenu.create(new Resource(this, "ViewMenuProps"));
        addMenuItem(viewMenu, "RefreshMenuItemProps", "refresh");
        viewMenu.addSeparator();

        // Sort By submenu
        sortByMenu = ResourcefulJMenu.create(new Resource(this, "SortByMenuProps"));
        ButtonGroup sortGroup = new ButtonGroup();
        sortByNameRadio = ResourcefulJRadioButtonMenuItem.create(new Resource(this, "SortByNameRadioProps"));
        sortByNameRadio.setActionCommand("sortByName");
        sortByNameRadio.addActionListener(this);
        sortByNameRadio.setSelected(true);
        sortGroup.add(sortByNameRadio);
        sortByMenu.add(sortByNameRadio);

        sortBySizeRadio = ResourcefulJRadioButtonMenuItem.create(new Resource(this, "SortBySizeRadioProps"));
        sortBySizeRadio.setActionCommand("sortBySize");
        sortBySizeRadio.addActionListener(this);
        sortGroup.add(sortBySizeRadio);
        sortByMenu.add(sortBySizeRadio);

        sortByDateRadio = ResourcefulJRadioButtonMenuItem.create(new Resource(this, "SortByDateRadioProps"));
        sortByDateRadio.setActionCommand("sortByDate");
        sortByDateRadio.addActionListener(this);
        sortGroup.add(sortByDateRadio);
        sortByMenu.add(sortByDateRadio);
        viewMenu.add(sortByMenu);

        viewMenu.addSeparator();
        showHiddenCheckBox = ResourcefulJCheckBoxMenuItem.create(new Resource(this, "ShowHiddenCheckBoxProps"));
        showHiddenCheckBox.setActionCommand("showHidden");
        showHiddenCheckBox.addActionListener(this);
        viewMenu.add(showHiddenCheckBox);

        showExtensionsCheckBox = ResourcefulJCheckBoxMenuItem.create(new Resource(this, "ShowExtensionsCheckBoxProps"));
        showExtensionsCheckBox.setSelected(true);
        showExtensionsCheckBox.setActionCommand("showExtensions");
        showExtensionsCheckBox.addActionListener(this);
        viewMenu.add(showExtensionsCheckBox);

        viewMenu.addSeparator();

        // Filter submenu
        ResourcefulJMenu filterMenu = ResourcefulJMenu.create(new Resource(this, "FilterMenuProps"));

        ButtonGroup showGroup = new ButtonGroup();
        ResourcefulJRadioButtonMenuItem allFilesRadio =
                ResourcefulJRadioButtonMenuItem.create(new Resource(this, "AllFilesRadioProps"));
        allFilesRadio.setActionCommand("allFiles");
        allFilesRadio.addActionListener(this);
        allFilesRadio.setSelected(true);
        showGroup.add(allFilesRadio);
        filterMenu.add(allFilesRadio);

        ResourcefulJRadioButtonMenuItem dirsOnlyRadio =
                ResourcefulJRadioButtonMenuItem.create(new Resource(this, "DirsOnlyRadioProps"));
        dirsOnlyRadio.setActionCommand("dirsOnly");
        dirsOnlyRadio.addActionListener(this);
        showGroup.add(dirsOnlyRadio);
        filterMenu.add(dirsOnlyRadio);

        filterMenu.addSeparator();

        ButtonGroup fileTypeGroup = new ButtonGroup();
        ResourcefulJRadioButtonMenuItem allFileTypesRadio =
                ResourcefulJRadioButtonMenuItem.create(new Resource(this, "AllFileTypesRadioProps"));
        allFileTypesRadio.setActionCommand("allFileTypes");
        allFileTypesRadio.addActionListener(this);
        allFileTypesRadio.setSelected(true);
        fileTypeGroup.add(allFileTypesRadio);
        filterMenu.add(allFileTypesRadio);

        ResourcefulJRadioButtonMenuItem textFilesRadio =
                ResourcefulJRadioButtonMenuItem.create(new Resource(this, "TextFilesRadioProps"));
        textFilesRadio.setActionCommand("textFiles");
        textFilesRadio.addActionListener(this);
        fileTypeGroup.add(textFilesRadio);
        filterMenu.add(textFilesRadio);

        ResourcefulJRadioButtonMenuItem imageFilesRadio =
                ResourcefulJRadioButtonMenuItem.create(new Resource(this, "ImageFilesRadioProps"));
        imageFilesRadio.setActionCommand("imageFiles");
        imageFilesRadio.addActionListener(this);
        fileTypeGroup.add(imageFilesRadio);
        filterMenu.add(imageFilesRadio);

        viewMenu.add(filterMenu);
        menuBar.add(viewMenu);

        // --- Preferences menu ---
        preferencesMenu = ResourcefulJMenu.create(new Resource(this, "PreferencesMenuProps"));
        localeMenu = ResourcefulJMenu.create(new Resource(this, "LocaleMenuProps"));
        preferencesMenu.add(localeMenu);
        populateLocaleMenu();
        lookAndFeelMenu = ResourcefulJMenu.create(new Resource(this, "LookAndFeelMenuProps"));
        preferencesMenu.add(lookAndFeelMenu);
        populateLookAndFeelMenu();

        preferencesMenu.addSeparator();

        // Locale Set submenu
        localeSetMenu = ResourcefulJMenu.create(new Resource(this, "LocaleSetMenuProps"));
        ButtonGroup localeSetGroup = new ButtonGroup();
        loggerLocalesRadio = ResourcefulJRadioButtonMenuItem.create(new Resource(this, "LoggerLocalesRadioProps"));
        loggerLocalesRadio.setActionCommand("loggerLocales");
        loggerLocalesRadio.addActionListener(this);
        loggerLocalesRadio.setSelected(true);
        localeSetGroup.add(loggerLocalesRadio);
        localeSetMenu.add(loggerLocalesRadio);

        allLocalesRadio = ResourcefulJRadioButtonMenuItem.create(new Resource(this, "AllLocalesRadioProps"));
        allLocalesRadio.setActionCommand("allLocales");
        allLocalesRadio.addActionListener(this);
        localeSetGroup.add(allLocalesRadio);
        localeSetMenu.add(allLocalesRadio);
        preferencesMenu.add(localeSetMenu);

        // Logger Locale submenu
        loggerLocaleMenu = ResourcefulJMenu.create(new Resource(this, "LoggerLocaleMenuProps"));
        ButtonGroup loggerLocaleGroup = new ButtonGroup();
        jvmDefaultLocaleRadio = ResourcefulJRadioButtonMenuItem.create(new Resource(this, "JvmDefaultLocaleRadioProps"));
        jvmDefaultLocaleRadio.setActionCommand("jvmDefaultLocale");
        jvmDefaultLocaleRadio.addActionListener(this);
        jvmDefaultLocaleRadio.setSelected(true);
        loggerLocaleGroup.add(jvmDefaultLocaleRadio);
        loggerLocaleMenu.add(jvmDefaultLocaleRadio);

        currentLocaleRadio = ResourcefulJRadioButtonMenuItem.create(new Resource(this, "CurrentLocaleRadioProps"));
        currentLocaleRadio.setActionCommand("currentLocale");
        currentLocaleRadio.addActionListener(this);
        loggerLocaleGroup.add(currentLocaleRadio);
        loggerLocaleMenu.add(currentLocaleRadio);
        preferencesMenu.add(loggerLocaleMenu);

        menuBar.add(preferencesMenu);

        // --- Help menu ---
        helpMenu = ResourcefulJMenu.create(new Resource(this, "HelpMenuProps"));
        addMenuItem(helpMenu, "AboutMenuItemProps", "about");
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void addMenuItem(ResourcefulJMenu menu, String resourceKey, String actionCommand)
    {
        ResourcefulJMenuItem item = ResourcefulJMenuItem.create(new Resource(this, resourceKey));
        item.setActionCommand(actionCommand);
        item.addActionListener(this);
        menu.add(item);
    }

    private void initToolBar()
    {
        ResourcefulJToolBar toolBar = ResourcefulJToolBar.create(new Resource(this, "NavToolBarProps"));

        backButton = ResourcefulJButton.create(new Resource(this, "BackButtonProps"));
        backButton.setActionCommand("back");
        backButton.addActionListener(this);
        toolBar.add(backButton);

        forwardButton = ResourcefulJButton.create(new Resource(this, "ForwardButtonProps"));
        forwardButton.setActionCommand("forward");
        forwardButton.addActionListener(this);
        toolBar.add(forwardButton);

        upButton = ResourcefulJButton.create(new Resource(this, "UpButtonProps"));
        upButton.setActionCommand("up");
        upButton.addActionListener(this);
        toolBar.add(upButton);

        ResourcefulJButton homeButton = ResourcefulJButton.create(new Resource(this, "HomeButtonProps"));
        homeButton.setActionCommand("home");
        homeButton.addActionListener(this);
        toolBar.add(homeButton);

        ResourcefulJButton refreshButton = ResourcefulJButton.create(new Resource(this, "RefreshButtonProps"));
        refreshButton.setActionCommand("refresh");
        refreshButton.addActionListener(this);
        toolBar.add(refreshButton);

        ResourcefulJButton newFolderButton = ResourcefulJButton.create(new Resource(this, "NewFolderButtonProps"));
        newFolderButton.setActionCommand("newFolder");
        newFolderButton.addActionListener(this);
        toolBar.add(newFolderButton);

        // Separator
        ResourcefulJSeparator separator = ResourcefulJSeparator.create(new Resource(this, "ToolBarSeparatorProps"));
        toolBar.add(separator);

        // View toggle buttons
        ButtonGroup viewGroup = new ButtonGroup();
        listViewToggle = ResourcefulJToggleButton.create(new Resource(this, "ListViewToggleProps"));
        listViewToggle.setActionCommand("listView");
        listViewToggle.addActionListener(this);
        detailViewToggle = ResourcefulJToggleButton.create(new Resource(this, "DetailViewToggleProps"));
        detailViewToggle.setActionCommand("detailView");
        detailViewToggle.addActionListener(this);
        viewGroup.add(listViewToggle);
        viewGroup.add(detailViewToggle);
        detailViewToggle.setSelected(true);
        toolBar.add(listViewToggle);
        toolBar.add(detailViewToggle);

        add(toolBar, BorderLayout.NORTH);
    }

    private void initPathBar()
    {
        ResourcefulJPanel pathPanel = ResourcefulJPanel.create(new Resource(this, "PathPanelProps"));
        pathPanel.setLayout(new BorderLayout(5, 0));
        pathPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        ResourcefulJLabel pathLabel = ResourcefulJLabel.create(new Resource(this, "PathLabelProps"));
        pathPanel.add(pathLabel, BorderLayout.WEST);

        pathTextField = ResourcefulJTextField.create(new Resource(this, "PathTextFieldProps"));
        pathTextField.addActionListener(e ->
        {
            File dir = new File(pathTextField.getText());
            if (dir.isDirectory())
            {
                navigateTo(dir);
            }
        });
        pathPanel.add(pathTextField, BorderLayout.CENTER);

        // Path bar goes between toolbar and content - use a wrapper panel
        // We'll add it to the NORTH using a compound panel
        // Actually, move toolbar + pathbar into a compound north panel
        Component toolbar = ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.NORTH);
        ResourcefulJPanel northPanel = ResourcefulJPanel.create(new Resource(this, "PanelProps"));
        northPanel.setLayout(new BorderLayout());
        if (toolbar != null)
        {
            getContentPane().remove(toolbar);
            northPanel.add(toolbar, BorderLayout.NORTH);
        }
        northPanel.add(pathPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);
    }

    private void initContentArea()
    {
        // Directory tree — ResourcefulJTree participates in locale events
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(getBundleString("ComputerNodeText"));
        for (File root : File.listRoots())
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(root);
            node.add(new DefaultMutableTreeNode(LOADING_MARKER));
            rootNode.add(node);
        }
        treeModel = new DefaultTreeModel(rootNode);
        directoryTree = ResourcefulJTree.create(new Resource(this, "DirectoryTreeProps"), treeModel);
        directoryTree.setRootVisible(false);
        directoryTree.setShowsRootHandles(true);
        directoryTree.setCellRenderer(new DefaultTreeCellRenderer()
        {
            @Override
            public Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel,
                    boolean expanded, boolean leaf, int row, boolean hasFocus)
            {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof DefaultMutableTreeNode)
                {
                    Object userObj = ((DefaultMutableTreeNode) value).getUserObject();
                    if (userObj instanceof File)
                    {
                        File file = (File) userObj;
                        String name = file.getName();
                        setText(name.isEmpty() ? file.getPath() : name);
                    }
                    else if (userObj == LOADING_MARKER)
                    {
                        // Read from bundle at render time — locale-aware
                        setText(getBundleString("LoadingText"));
                    }
                }
                return this;
            }
        });

        directoryTree.addTreeExpansionListener(new TreeExpansionListener()
        {
            @Override
            public void treeExpanded(TreeExpansionEvent event)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                if (node.getChildCount() != 1) return;
                DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode) node.getChildAt(0);
                if (LOADING_MARKER != firstChild.getUserObject()) return;
                Object userObj = node.getUserObject();
                if (!(userObj instanceof File)) return;

                final File dir = (File) userObj;
                final boolean capturedShowHidden = showHiddenFiles;
                new SwingWorker<List<File>, Void>()
                {
                    @Override
                    protected List<File> doInBackground()
                    {
                        return listSubdirectories(dir, capturedShowHidden);
                    }

                    @Override
                    protected void done()
                    {
                        // applyTreeSelection may have already populated this node; skip if so.
                        if (node.getChildCount() != 1) return;
                        if (LOADING_MARKER != ((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject()) return;
                        try
                        {
                            node.removeAllChildren();
                            for (File child : get())
                            {
                                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                                childNode.add(new DefaultMutableTreeNode(LOADING_MARKER));
                                node.add(childNode);
                            }
                            treeModel.nodeStructureChanged(node);
                        }
                        catch (CancellationException ignored) { }
                        catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
                        catch (ExecutionException ex)
                        {
                            System.getLogger(AppFrame.class.getName()).log(
                                    System.Logger.Level.WARNING, "Failed to load subdirectories", ex.getCause());
                        }
                    }
                }.execute();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {}
        });

        directoryTree.addTreeSelectionListener(e ->
        {
            if (updatingTreeSelection) return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) directoryTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof File)
            {
                File dir = (File) node.getUserObject();
                if (dir.isDirectory())
                {
                    navigateTo(dir);
                }
            }
        });

        ResourcefulJScrollPane treeScrollPane = ResourcefulJScrollPane.create(new Resource(this, "TreeScrollPaneProps"));
        treeScrollPane.setViewportView(directoryTree);
        treeScrollPane.setPreferredSize(new Dimension(200, 400));

        // File table — ResourcefulJTable participates in locale events (refreshes column headers)
        fileTableModel = new FileTableModel();
        fileTable = ResourcefulJTable.create(new Resource(this, "FileTableProps"), fileTableModel);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int row = fileTable.rowAtPoint(e.getPoint());
                if (row >= 0)
                {
                    File file = fileTableModel.getFile(row);
                    if (e.getClickCount() == 2)
                    {
                        if (file.isDirectory())
                        {
                            navigateTo(file);
                        }
                        else
                        {
                            previewFile(file);
                            contentTabbedPane.setSelectedIndex(1);
                        }
                    }
                    else
                    {
                        showProperties(file);
                    }
                }
            }
        });

        // Right-click popup menu on the file table
        demoPopupMenu = ResourcefulJPopupMenu.create(new Resource(this, "FileTablePopupMenuProps"));
        ResourcefulJMenuItem popupRefreshItem = ResourcefulJMenuItem.create(new Resource(this, "PopupRefreshMenuItemProps"));
        popupRefreshItem.setActionCommand("refresh");
        popupRefreshItem.addActionListener(this);
        demoPopupMenu.add(popupRefreshItem);
        ResourcefulJMenuItem popupPropertiesItem = ResourcefulJMenuItem.create(new Resource(this, "PopupPropertiesMenuItemProps"));
        popupPropertiesItem.setActionCommand("properties");
        popupPropertiesItem.addActionListener(this);
        demoPopupMenu.add(popupPropertiesItem);
        ResourcefulJMenuItem popupDeleteItem = ResourcefulJMenuItem.create(new Resource(this, "PopupDeleteMenuItemProps"));
        popupDeleteItem.setActionCommand("delete");
        popupDeleteItem.addActionListener(this);
        demoPopupMenu.add(popupDeleteItem);
        fileTable.setComponentPopupMenu(demoPopupMenu);

        ResourcefulJScrollPane tableScrollPane = ResourcefulJScrollPane.create(new Resource(this, "TableScrollPaneProps"));
        tableScrollPane.setViewportView(fileTable);

        // List (icon) view — shown when "List" toggle is active
        fileListModel = new DefaultListModel<>();
        fileListView = ResourcefulJList.create(new Resource(this, "FileListViewProps"), fileListModel);
        fileListView.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        fileListView.setVisibleRowCount(-1);
        fileListView.setFixedCellWidth(110);
        fileListView.setFixedCellHeight(70);
        fileListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileListView.setCellRenderer(new FileListCellRenderer());
        fileListView.addListSelectionListener(e ->
        {
            if (!e.getValueIsAdjusting())
            {
                File selected = fileListView.getSelectedValue();
                if (selected != null) showProperties(selected);
            }
        });
        fileListView.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    File file = fileListView.getSelectedValue();
                    if (file == null) return;
                    if (file.isDirectory())
                        navigateTo(file);
                    else
                    {
                        previewFile(file);
                        contentTabbedPane.setSelectedIndex(1);
                    }
                }
            }
        });
        ResourcefulJScrollPane listScrollPane = ResourcefulJScrollPane.create(new Resource(this, "ListScrollPaneProps"));
        listScrollPane.setViewportView(fileListView);

        // Card panel switching between detail (table) and list (icon) view
        fileViewCard = ResourcefulJPanel.create(new Resource(this, "PanelProps"));
        fileViewCard.setLayout(new CardLayout());
        fileViewCard.add(tableScrollPane, "detail");
        fileViewCard.add(listScrollPane, "list");

        // Preview tab content
        previewTextArea = ResourcefulJTextArea.create(new Resource(this, "PreviewTextAreaProps"));
        previewTextArea.setEditable(false);
        previewTextArea.setRows(10);
        previewTextArea.setColumns(40);

        previewEditorPane = ResourcefulJEditorPane.create(new Resource(this, "PreviewEditorPaneProps"));
        previewEditorPane.setEditable(false);
        previewEditorPane.setContentType("text/html");

        ResourcefulJScrollPane previewScrollPane = ResourcefulJScrollPane.create(new Resource(this, "PreviewScrollPaneProps"));
        previewScrollPane.setViewportView(previewTextArea);

        previewCardPanel = ResourcefulJPanel.create(new Resource(this, "PanelProps"));
        previewCardPanel.setLayout(new CardLayout());
        previewCardPanel.add(previewScrollPane, "text");
        previewCardPanel.add(previewEditorPane, "html");

        // Properties tab content
        ResourcefulJPanel propertiesPanel = ResourcefulJPanel.create(new Resource(this, "PropertiesPanelProps"));
        propertiesPanel.setLayout(new BorderLayout());

        ResourcefulJPanel propsInfoPanel = ResourcefulJPanel.create(new Resource(this, "PanelProps"));
        propsInfoPanel.setLayout(new GridBagLayout());
        propsInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 5, 3, 5);

        fileNameLabel = ResourcefulJLabel.create(new Resource(this, "FileNameLabelProps"));
        fileSizeLabel = ResourcefulJLabel.create(new Resource(this, "FileSizeLabelProps"));
        fileTypeLabel = ResourcefulJLabel.create(new Resource(this, "FileTypeLabelProps"));
        fileModifiedLabel = ResourcefulJLabel.create(new Resource(this, "FileModifiedLabelProps"));
        filePathLabel = ResourcefulJLabel.create(new Resource(this, "FilePathLabelProps"));
        readOnlyCheckBox = ResourcefulJCheckBox.create(new Resource(this, "ReadOnlyCheckBoxProps"));
        readOnlyCheckBox.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; propsInfoPanel.add(fileNameLabel, gbc);
        gbc.gridy = 1; propsInfoPanel.add(fileSizeLabel, gbc);
        gbc.gridy = 2; propsInfoPanel.add(fileTypeLabel, gbc);
        gbc.gridy = 3; propsInfoPanel.add(fileModifiedLabel, gbc);
        gbc.gridy = 4; propsInfoPanel.add(filePathLabel, gbc);
        gbc.gridy = 5; propsInfoPanel.add(readOnlyCheckBox, gbc);

        propertiesPanel.add(propsInfoPanel, BorderLayout.NORTH);

        // ResourcefulJDesktopPane with ResourcefulJInternalFrame for Quick Info
        ResourcefulJDesktopPane desktopPane = ResourcefulJDesktopPane.create(new Resource(this, "InfoDesktopPaneProps"));
        desktopPane.setPreferredSize(new Dimension(400, 150));
        infoInternalFrame = ResourcefulJInternalFrame.create(new Resource(this, "InfoInternalFrameProps"));
        infoInternalFrame.setSize(250, 100);
        infoInternalFrame.setVisible(true);
        infoInternalFrame.setResizable(true);
        infoContentLabel = ResourcefulJLabel.create(new Resource(this, "InfoContentLabelProps"));
        infoInternalFrame.getContentPane().add(infoContentLabel);
        desktopPane.add(infoInternalFrame);
        propertiesPanel.add(desktopPane, BorderLayout.CENTER);

        // Widgets demo tab
        ResourcefulJPanel widgetsPanel = initWidgetsPanel();

        // Tabbed pane for content
        contentTabbedPane = ResourcefulJTabbedPane.create(new Resource(this, "ContentTabbedPaneProps"));
        contentTabbedPane.addResourcefulTab(new Resource(this, "FilesTabProps"), fileViewCard);
        contentTabbedPane.addResourcefulTab(new Resource(this, "PreviewTabProps"), previewCardPanel);
        contentTabbedPane.addResourcefulTab(new Resource(this, "PropertiesTabProps"), propertiesPanel);
        contentTabbedPane.addResourcefulTab(new Resource(this, "WidgetsDemoTabProps"), widgetsPanel);

        // Split pane
        ResourcefulJSplitPane splitPane = ResourcefulJSplitPane.create(new Resource(this, "MainSplitPaneProps"));
        splitPane.setLeftComponent(treeScrollPane);
        splitPane.setRightComponent(contentTabbedPane);
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);

        // Initialize dialogs — ResourcefulJOptionPane instead of JOptionPane statics
        newFolderDialog = ResourcefulJOptionPane.create(new Resource(this, "NewFolderDialogProps"));
        deleteConfirmDialog = ResourcefulJOptionPane.create(new Resource(this, "ConfirmDeleteDialogProps"));
    }

    /**
     * Build the Widgets Demo tab panel, exercising every new Resourceful component type.
     */
    private ResourcefulJPanel initWidgetsPanel()
    {
        ResourcefulJPanel panel = ResourcefulJPanel.create(new Resource(this, "WidgetsPanelProps"));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // --- ResourcefulJPasswordField ---
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(ResourcefulJLabel.create(new Resource(this, "DemoPasswordLabelProps")), gbc);
        demoPasswordField = ResourcefulJPasswordField.create(new Resource(this, "DemoPasswordFieldProps"));
        demoPasswordField.setColumns(20);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(demoPasswordField, gbc);
        row++;

        // --- ResourcefulJFormattedTextField (date) ---
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(ResourcefulJLabel.create(new Resource(this, "DemoFormattedLabelProps")), gbc);
        DateFormat initialDf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, getBundleLocale());
        // javax.swing.text.DateFormatter is a formatter strategy object (not a UI component);
        // no Resourceful wrapper exists.  It is passed as the initial value formatter to the
        // ResourcefulJFormattedTextField factory and is reinstalled on locale changes.
        demoFormattedTextField = ResourcefulJFormattedTextField.create(
                new Resource(this, "DemoFormattedTextFieldProps"),
                new javax.swing.text.DateFormatter(initialDf));
        demoFormattedTextField.setValue(new Date());
        demoFormattedTextField.setColumns(20);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(demoFormattedTextField, gbc);
        row++;

        // --- ResourcefulJSpinner (date) ---
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(ResourcefulJLabel.create(new Resource(this, "DemoSpinnerLabelProps")), gbc);
        DateFormat spinnerDf = DateFormat.getDateInstance(DateFormat.MEDIUM, getBundleLocale());
        String spinnerPattern = ((SimpleDateFormat) spinnerDf).toPattern();
        demoSpinner = ResourcefulJSpinner.create(new Resource(this, "DemoSpinnerProps"), new SpinnerDateModel());
        // JSpinner.DateEditor is used directly (not ResourcefulJSpinnerDateEditor) because
        // this editor is replaced on every locale change in updateWidgetsForLocale() with a
        // fresh locale-specific pattern — making a Resourceful wrapper unnecessary overhead.
        demoSpinner.setEditor(new JSpinner.DateEditor(demoSpinner, spinnerPattern));
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(demoSpinner, gbc);
        row++;

        // --- ResourcefulJSlider ---
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(ResourcefulJLabel.create(new Resource(this, "DemoSliderLabelProps")), gbc);
        demoSlider = ResourcefulJSlider.create(new Resource(this, "DemoSliderProps"),
                ResourcefulJSlider.HORIZONTAL, 0, 100, 50);
        demoSlider.setMajorTickSpacing(25);
        demoSlider.setMinorTickSpacing(5);
        demoSlider.setPaintTicks(true);
        demoSlider.setPaintLabels(true);
        updateSliderLabels(getBundleLocale());
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(demoSlider, gbc);
        row++;

        // --- ResourcefulJList (day names) ---
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(ResourcefulJLabel.create(new Resource(this, "DemoListLabelProps")), gbc);
        demoListModel = new DefaultListModel<>();
        populateDemoList(getBundleLocale());
        demoList = ResourcefulJList.create(new Resource(this, "DemoListProps"), demoListModel);
        demoList.setVisibleRowCount(4);
        ResourcefulJScrollPane listScrollPane = ResourcefulJScrollPane.create(new Resource(this, "DemoListScrollPaneProps"));
        listScrollPane.setViewportView(demoList);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH;
        panel.add(listScrollPane, gbc);
        row++;

        // --- ResourcefulJTextPane ---
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(ResourcefulJLabel.create(new Resource(this, "DemoTextPaneLabelProps")), gbc);
        demoTextPane = ResourcefulJTextPane.create(new Resource(this, "DemoTextPaneProps"));
        demoTextPane.setEditable(false);
        demoTextPane.setPreferredSize(new Dimension(300, 80));
        refreshTextPaneContent(getBundleLocale());
        ResourcefulJScrollPane textPaneScrollPane = ResourcefulJScrollPane.create(new Resource(this, "DemoTextPaneScrollPaneProps"));
        textPaneScrollPane.setViewportView(demoTextPane);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH;
        panel.add(textPaneScrollPane, gbc);
        row++;

        // Invisible layout spacer to push widgets towards the top of the panel.
        // JLabel (not ResourcefulJLabel) is used because this filler has no user-visible
        // content and requires no locale-aware properties.
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    /** Populate demoList with the abbreviated day names in the given locale. */
    private void populateDemoList(Locale locale)
    {
        java.text.DateFormatSymbols symbols = java.text.DateFormatSymbols.getInstance(locale);
        String[] weekdays = symbols.getWeekdays(); // index 1=Sunday ... 7=Saturday
        demoListModel.clear();
        for (int i = 1; i <= 7; i++)
        {
            demoListModel.addElement(weekdays[i]);
        }
    }

    /** Update the demoSlider's tick-mark label table with locale-aware number formatting. */
    @SuppressWarnings("UseOfObsoleteCollectionType") // JSlider.setLabelTable() requires Dictionary; Hashtable is the only JDK-provided implementation
    private void updateSliderLabels(Locale locale)
    {
        NumberFormat nf = NumberFormat.getInstance(locale);
        Dictionary<Integer, JLabel> labels = new Hashtable<>();
        for (int i = 0; i <= 100; i += 25)
        {
            // JLabel (not ResourcefulJLabel) is used for slider tick marks because:
            // (a) the entire table is rebuilt from scratch on every locale change, and
            // (b) the label text is a dynamically-formatted number, not a bundle string.
            labels.put(i, new JLabel(nf.format(i)));
        }
        demoSlider.setLabelTable(labels);
        demoSlider.refreshLabelUIs();
    }

    /** Refresh the demoTextPane styled content to reflect the current locale. */
    private void refreshTextPaneContent(Locale locale)
    {
        StyledDocument doc = demoTextPane.getStyledDocument();
        try
        {
            doc.remove(0, doc.getLength());
            SimpleAttributeSet bold = new SimpleAttributeSet();
            StyleConstants.setBold(bold, true);
            SimpleAttributeSet normal = new SimpleAttributeSet();
            doc.insertString(0, getBundleString("DemoTextPaneHeading") + "\n", bold);
            doc.insertString(doc.getLength(),
                    formatBundleString("DemoTextPaneBodyFormat",
                            locale.getDisplayName(locale),
                            DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, locale)
                                    .format(new Date())),
                    normal);
        }
        catch (BadLocationException ex)
        {
            System.getLogger(AppFrame.class.getName()).log(
                    System.Logger.Level.WARNING, "Failed to update demo text pane", ex);
        }
    }

    private void initStatusBar()
    {
        ResourcefulJPanel statusPanel = ResourcefulJPanel.create(new Resource(this, "StatusPanelProps"));
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        fileCountLabel = ResourcefulJLabel.create(new Resource(this, "FileCountLabelProps"));
        statusPanel.add(fileCountLabel);

        sizeStatusLabel = new JLabel();
        statusPanel.add(sizeStatusLabel);

        add(statusPanel, BorderLayout.SOUTH);
    }

    // --- Navigation ---

    void navigateTo(File dir)
    {
        if (dir == null || !dir.isDirectory()) return;
        currentDirectory = dir;

        // Update history
        if (historyIndex < navigationHistory.size() - 1)
        {
            navigationHistory.subList(historyIndex + 1, navigationHistory.size()).clear();
        }
        navigationHistory.add(dir);
        historyIndex = navigationHistory.size() - 1;

        // Update UI
        pathTextField.setText(dir.getAbsolutePath());
        updateNavigationButtons();
        refreshFileTable();
        syncTreeSelection(dir);
    }

    private void navigateBack()
    {
        if (historyIndex > 0)
        {
            historyIndex--;
            currentDirectory = navigationHistory.get(historyIndex);
            pathTextField.setText(currentDirectory.getAbsolutePath());
            updateNavigationButtons();
            refreshFileTable();
            syncTreeSelection(currentDirectory);
        }
    }

    private void navigateForward()
    {
        if (historyIndex < navigationHistory.size() - 1)
        {
            historyIndex++;
            currentDirectory = navigationHistory.get(historyIndex);
            pathTextField.setText(currentDirectory.getAbsolutePath());
            updateNavigationButtons();
            refreshFileTable();
            syncTreeSelection(currentDirectory);
        }
    }

    private void navigateUp()
    {
        if (currentDirectory != null && currentDirectory.getParentFile() != null)
        {
            navigateTo(currentDirectory.getParentFile());
        }
    }

    private void updateNavigationButtons()
    {
        backButton.setEnabled(historyIndex > 0);
        forwardButton.setEnabled(historyIndex < navigationHistory.size() - 1);
        upButton.setEnabled(currentDirectory != null && currentDirectory.getParentFile() != null);
    }

    /**
     * Expands the tree to {@code dir} and updates the selection, without
     * triggering the TreeSelectionListener's navigateTo callback.
     */
    private void syncTreeSelection(File dir)
    {
        // Guard the entire method: expandPath triggers treeExpanded → nodeStructureChanged,
        // which may cause JTree's internal model handler to re-fire the current selection.
        // Without this guard those intermediate events would recursively call navigateTo.
        updatingTreeSelection = true;
        try
        {
            syncTreeSelectionImpl(dir);
        }
        finally
        {
            updatingTreeSelection = false;
        }
    }

    private void syncTreeSelectionImpl(File dir)
    {
        // Build the chain of File ancestors from the filesystem root down to dir.
        List<File> pathSegments = new ArrayList<>();
        for (File f = dir; f != null; f = f.getParentFile())
        {
            pathSegments.add(0, f);
        }

        // Locate the level-1 tree node whose userObject is the filesystem root.
        DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
        DefaultMutableTreeNode current = null;
        for (int i = 0; i < treeRoot.getChildCount(); i++)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeRoot.getChildAt(i);
            if (pathSegments.get(0).equals(child.getUserObject()))
            {
                current = child;
                break;
            }
        }
        if (current == null) return;

        // Walk down the path, expanding each level so lazy children are loaded.
        for (int segIdx = 1; segIdx < pathSegments.size(); segIdx++)
        {
            // Synchronously populate before expanding so the child search below
            // never races against the async SwingWorker.
            ensureNodePopulated(current);
            directoryTree.expandPath(new TreePath(current.getPath()));

            File segment = pathSegments.get(segIdx);
            DefaultMutableTreeNode found = null;
            for (int i = 0; i < current.getChildCount(); i++)
            {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) current.getChildAt(i);
                if (segment.equals(child.getUserObject()))
                {
                    found = child;
                    break;
                }
            }
            if (found == null) return; // directory not visible in tree (e.g. hidden)
            current = found;
        }

        TreePath treePath = new TreePath(current.getPath());
        directoryTree.setSelectionPath(treePath);
        directoryTree.scrollPathToVisible(treePath);
    }

    void refreshFileTable()
    {
        if (currentDirectory == null) return;

        // Cancel any in-flight listing for a previous directory.
        if (fileTableWorker != null && !fileTableWorker.isDone())
            fileTableWorker.cancel(true);

        // Cancel any in-flight dir-size computation.
        if (dirSizeWorker != null && !dirSizeWorker.isDone())
        {
            dirSizeWorker.cancel(true);
            sizeStatusLabel.setText("");
        }

        // Snapshot all filter/sort state on the EDT before handing off to the worker.
        final File dir = currentDirectory;
        final boolean capturedHidden = showHiddenFiles;
        final boolean capturedDirsOnly = dirsOnly;
        final String capturedFileType = fileType;
        final String capturedSortBy = sortBy;
        final Locale capturedLocale = getBundleLocale();
        final Map<File, Long> capturedSizes = new HashMap<>(dirSizeCache);

        fileTableWorker = new SwingWorker<List<File>, Void>()
        {
            @Override
            protected List<File> doInBackground()
            {
                File[] entries = dir.listFiles();
                if (entries == null) return new ArrayList<>();

                List<File> fileList = new ArrayList<>();
                for (File file : entries)
                {
                    if (isCancelled()) return fileList;
                    if (!capturedHidden && file.isHidden()) continue;
                    if (capturedDirsOnly && !file.isDirectory()) continue;
                    if (!capturedFileType.equals("all") && !file.isDirectory())
                    {
                        String lname = file.getName().toLowerCase();
                        if (capturedFileType.equals("text") && !lname.endsWith(".txt") && !lname.endsWith(".md")
                                && !lname.endsWith(".csv") && !lname.endsWith(".log")) continue;
                        if (capturedFileType.equals("image") && !lname.endsWith(".jpg") && !lname.endsWith(".jpeg")
                                && !lname.endsWith(".png") && !lname.endsWith(".gif")
                                && !lname.endsWith(".bmp")) continue;
                    }
                    fileList.add(file);
                }
                Collator col = Collator.getInstance(capturedLocale);
                Comparator<File> byName = (a, b) -> col.compare(a.getName(), b.getName());
                Comparator<File> secondary;
                switch (capturedSortBy)
                {
                    case "size":
                        secondary = Comparator.comparingLong((File f) -> capturedSizes.getOrDefault(f, f.length())).thenComparing(byName);
                        break;
                    case "date":
                        secondary = Comparator.comparingLong(File::lastModified).thenComparing(byName);
                        break;
                    default:
                        secondary = byName;
                        break;
                }
                Comparator<File> comparator = (a, b) ->
                {
                    boolean aDir = a.isDirectory(), bDir = b.isDirectory();
                    if (aDir && !bDir) return -1;
                    if (!aDir && bDir) return 1;
                    if (aDir)
                    {
                        if ("size".equals(capturedSortBy))
                        {
                            long sizeA = capturedSizes.getOrDefault(a, Long.MAX_VALUE);
                            long sizeB = capturedSizes.getOrDefault(b, Long.MAX_VALUE);
                            int cmp = Long.compare(sizeA, sizeB);
                            return cmp != 0 ? cmp : byName.compare(a, b);
                        }
                        return secondary.compare(a, b);
                    }
                    return secondary.compare(a, b);
                };
                fileList.sort(comparator);
                return fileList;
            }

            @Override
            protected void done()
            {
                if (isCancelled()) return;
                try
                {
                    List<File> fileList = get();
                    fileTableModel.setFiles(fileList);
                    fileListModel.clear();
                    for (File f : fileList) fileListModel.addElement(f);
                    fileCountLabel.setText(formatBundleString("ItemCountFormat", fileList.size()));
                    startDirSizeComputation(fileList);
                }
                catch (CancellationException ignored) { }
                catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
                catch (ExecutionException ex)
                {
                    System.getLogger(AppFrame.class.getName()).log(
                            System.Logger.Level.WARNING, "Failed to list directory contents", ex.getCause());
                }
            }
        };
        fileTableWorker.execute();
    }

    private void startDirSizeComputation(List<File> files)
    {
        List<File> dirs = new ArrayList<>();
        for (File f : files)
            if (f.isDirectory() && !dirSizeCache.containsKey(f))
                dirs.add(f);

        if (dirs.isEmpty())
        {
            sizeStatusLabel.setText("");
            return;
        }

        int total = dirs.size();

        dirSizeWorker = new SwingWorker<Void, long[]>()
        {
            @Override
            protected Void doInBackground()
            {
                for (int i = 0; i < dirs.size(); i++)
                {
                    if (isCancelled()) return null;
                    final int currentIdx = i + 1;
                    SwingUtilities.invokeLater(() -> sizeStatusLabel.setText(formatBundleString("ComputingSizesFormat", currentIdx, total)));
                    long size = computeDirSize(dirs.get(i), this::isCancelled);
                    if (!isCancelled()) publish(new long[]{i, size});
                }
                return null;
            }

            @Override
            protected void process(List<long[]> chunks)
            {
                for (long[] chunk : chunks)
                {
                    File dir = dirs.get((int) chunk[0]);
                    dirSizeCache.put(dir, chunk[1]);
                    int row = fileTableModel.indexOf(dir);
                    if (row >= 0) fileTableModel.fireTableCellUpdated(row, 1);
                }
            }

            @Override
            protected void done()
            {
                if (isCancelled()) return;
                sizeStatusLabel.setText("");
                if ("size".equals(sortBy)) refreshFileTable();
            }
        };
        dirSizeWorker.execute();
    }

    /**
     * Synchronously populates {@code node} with its subdirectory children if it still
     * holds the LOADING_MARKER placeholder. Called from the EDT during tree navigation
     * so that {@link #syncTreeSelectionImpl} can walk the full path without racing the
     * async SwingWorker. The SwingWorker's {@code done()} guard (childCount != 1 check)
     * detects the pre-population and skips its own update.
     */
    private void ensureNodePopulated(DefaultMutableTreeNode node)
    {
        if (node.getChildCount() == 1
                && LOADING_MARKER == ((DefaultMutableTreeNode) node.getChildAt(0)).getUserObject()
                && node.getUserObject() instanceof File dir)
        {
            node.removeAllChildren();
            for (File child : listSubdirectories(dir, showHiddenFiles))
            {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                childNode.add(new DefaultMutableTreeNode(LOADING_MARKER));
                node.add(childNode);
            }
            treeModel.nodeStructureChanged(node);
        }
    }

    /**
     * Returns a sorted list of subdirectories within {@code dir}.
     * Called from a SwingWorker background thread to lazily populate tree nodes.
     *
     * @param dir        the directory to list
     * @param showHidden whether hidden directories should be included
     * @return sorted list of subdirectories (never null)
     */
    private List<File> listSubdirectories(File dir, boolean showHidden)
    {
        File[] entries = dir.listFiles();
        if (entries == null) return new ArrayList<>();
        List<File> result = new ArrayList<>();
        for (File f : entries)
        {
            if (!f.isDirectory()) continue;
            if (!showHidden && f.isHidden()) continue;
            result.add(f);
        }
        Collator treeCollator = Collator.getInstance(getBundleLocale());
        result.sort((a, b) -> treeCollator.compare(a.getName(), b.getName()));
        return result;
    }

    void previewFile(File file)
    {
        if (file == null || file.isDirectory()) return;

        String name = file.getName().toLowerCase();
        boolean isHtml = name.endsWith(".html") || name.endsWith(".htm");

        try
        {
            long size = file.length();
            if (size > 65536)
            {
                previewTextArea.setText(formatBundleString("FileTooLargeFormat", formatSize(size)));
            }
            else
            {
                String content = Files.readString(file.toPath(), java.nio.charset.StandardCharsets.UTF_8);
                if (isHtml)
                {
                    previewEditorPane.setText(content);
                    ((CardLayout) previewCardPanel.getLayout()).show(previewCardPanel, "html");
                }
                else
                {
                    previewTextArea.setText(content);
                    previewTextArea.setCaretPosition(0);
                    ((CardLayout) previewCardPanel.getLayout()).show(previewCardPanel, "text");
                }
            }
        }
        catch (IOException e)
        {
            System.getLogger(AppFrame.class.getName()).log(
                    System.Logger.Level.WARNING, "Failed to preview file: " + file.getAbsolutePath(), e);
            previewTextArea.setText(formatBundleString("CannotReadFileFormat", e.getMessage()));
        }
    }

    void showProperties(File file)
    {
        if (file == null) return;

        Locale locale = getBundleLocale();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);

        fileNameLabel.setText(formatBundleText("FileNameLabelProps", file.getName()));
        fileSizeLabel.setText(formatBundleText("FileSizeLabelProps", formatSize(file.length())));
        fileTypeLabel.setText(formatBundleText("FileTypeLabelProps", getFileType(file)));
        fileModifiedLabel.setText(formatBundleText("FileModifiedLabelProps", df.format(new Date(file.lastModified()))));
        String parent = file.getParent();
        filePathLabel.setText(formatBundleText("FilePathLabelProps", parent != null ? parent : file.getAbsolutePath()));
        readOnlyCheckBox.setSelected(!file.canWrite());

        infoContentLabel.setText("<html><b>" + file.getName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") + "</b><br>"
                + formatSize(file.length()) + " | " + getFileType(file) + "</html>");
    }

    private void createNewFolder()
    {
        if (currentDirectory == null) return;
        // ResourcefulJOptionPane — button labels come from resource bundle, not UIManager
        String name = newFolderDialog.showInput(this, getBundleString("FolderNamePromptText"));
        if (name != null && !name.isEmpty())
        {
            File newDir = new File(currentDirectory, name);
            if (newDir.mkdir())
            {
                refreshFileTable();
            }
        }
    }

    private void deleteSelected()
    {
        int row = fileTable.getSelectedRow();
        if (row >= 0)
        {
            File file = fileTableModel.getFile(row);
            // ResourcefulJOptionPane — button labels come from resource bundle, not UIManager
            int confirm = deleteConfirmDialog.showConfirm(this,
                    formatBundleString("ConfirmDeleteFormat", file.getName()));
            if (confirm == 0) // 0 = first option (Yes/Oui/etc.)
            {
                if (file.delete())
                {
                    refreshFileTable();
                }
            }
        }
    }

    private void showAboutDialog()
    {
        AppDialog dialog = AppDialog.create(this);
        dialog.setBundleLocale(getBundleLocale());
        dialog.initialize();
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private String formatSize(long bytes)
    {
        checkFormatCacheLocale();
        if (sizeNumberFormat == null) sizeNumberFormat = NumberFormat.getInstance(getBundleLocale());
        if (bytes < 1024) return sizeNumberFormat.format(bytes) + getBundleString("BytesSuffix");
        if (bytes < 1024 * 1024) return sizeNumberFormat.format(bytes / 1024) + getBundleString("KilobytesSuffix");
        return sizeNumberFormat.format(bytes / (1024 * 1024)) + getBundleString("MegabytesSuffix");
    }

    private long computeDirSize(File dir)
    {
        return computeDirSize(dir, () -> false);
    }

    private long computeDirSize(File dir, BooleanSupplier isCancelled)
    {
        if (isCancelled.getAsBoolean()) return 0L;
        long[] total = {0L};
        try
        {
            Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult preVisitDirectory(Path d, BasicFileAttributes attrs)
                {
                    return isCancelled.getAsBoolean() ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    if (isCancelled.getAsBoolean()) return FileVisitResult.TERMINATE;
                    // Skip iCloud placeholder stubs (.FileName.icloud) to avoid blocking on network I/O
                    String name = file.getFileName().toString();
                    if (!(name.startsWith(".") && name.endsWith(".icloud")))
                        total[0] += attrs.size();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException ignored) {}
        return total[0];
    }

    private String getFileType(File file)
    {
        if (file.isDirectory()) return getBundleString("DirectoryTypeText");
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot >= 0)
        {
            return formatBundleString("FileTypeSuffixFormat", name.substring(dot + 1).toUpperCase());
        }
        return getBundleString("GenericFileTypeText");
    }

    // --- Inner class: FileListCellRenderer ---

    class FileListCellRenderer extends javax.swing.DefaultListCellRenderer
    {
        private static final javax.swing.filechooser.FileSystemView FSV =
                javax.swing.filechooser.FileSystemView.getFileSystemView();
        private final java.util.HashMap<File, javax.swing.Icon> iconCache = new java.util.HashMap<>();

        @Override
        public java.awt.Component getListCellRendererComponent(
                javax.swing.JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus)
        {
            File file = (File) value;
            String name = file.getName();
            if (name.isEmpty()) name = file.getAbsolutePath(); // filesystem root (e.g. "/")
            if (!showFileExtensions && !file.isDirectory())
            {
                int dot = name.lastIndexOf('.');
                if (dot > 0) name = name.substring(0, dot);
            }
            super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
            setIcon(iconCache.computeIfAbsent(file, FSV::getSystemIcon));
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setHorizontalTextPosition(CENTER);
            setVerticalTextPosition(BOTTOM);
            setIconTextGap(4);
            return this;
        }
    }

    // --- Inner class: FileTableModel ---

    class FileTableModel extends AbstractTableModel
    {
        private List<File> files = new ArrayList<>();
        private static final String[] COLUMN_KEYS = {"NameColumnText", "SizeColumnText", "TypeColumnText", "DateColumnText"};
        private Locale cachedDateLocale;
        private DateFormat cachedDateFormat;

        void setFiles(List<File> files)
        {
            this.files = files;
            fireTableDataChanged();
        }

        File getFile(int row)
        {
            return files.get(row);
        }

        int indexOf(File f) { return files.indexOf(f); }

        @Override
        public int getRowCount()
        {
            return files.size();
        }

        @Override
        public int getColumnCount()
        {
            return COLUMN_KEYS.length;
        }

        @Override
        public String getColumnName(int col)
        {
            // Read from bundle at call time — locale-aware; ResourcefulJTable calls
            // createDefaultColumnsFromModel() on locale change to force re-read.
            return getBundleString(COLUMN_KEYS[col]);
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            File file = files.get(row);
            Locale locale = getBundleLocale();
            switch (col)
            {
                case 0:
                    String name = file.getName();
                    if (!showFileExtensions && !file.isDirectory())
                    {
                        int dot = name.lastIndexOf('.');
                        if (dot > 0) name = name.substring(0, dot);
                    }
                    return name;
                case 1:
                    if (!file.isDirectory()) return formatSize(file.length());
                    Long cached = dirSizeCache.get(file);
                    return cached != null ? formatSize(cached) : "...";
                case 2:
                    return getFileType(file);
                case 3:
                    if (!locale.equals(cachedDateLocale))
                    {
                        cachedDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
                        cachedDateLocale = locale;
                    }
                    return cachedDateFormat.format(new Date(file.lastModified()));
                default:
                    return "";
            }
        }
    }
}
