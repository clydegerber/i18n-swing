# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.2.2] - 2026-03-15

### Fixed

- Test factory methods (`TestComponentSource.create()`, `AppWindow.create()`,
  `AppDialog.create()`) now call `setBundleLocale(Locale.ROOT)` immediately after
  construction so that `testInitialProperties` assertions pass regardless of the
  JVM default locale.
- `TestAppFrame.testAppFrameJsonBundle()` now passes `Locale.ROOT` explicitly to
  `ResourceBundle.getBundle()` before asserting English string values.

## [1.2.1] - 2026-03-15

### Changed

- `LocaleJMenuItem` now extends `ResourcefulJRadioButtonMenuItem` instead of
  `ResourcefulJMenuItem`, giving locale-selection menu items a native radio-button
  check mark that reflects the active locale
- Demo app: `AppFrame.create()` selects the best-match locale for the system default
  on startup rather than always starting at `Locale.ROOT`
- Demo app: locale menu uses a `ButtonGroup` so the selected locale is shown with a
  radio-button indicator; when more than 20 locales are listed the current locale is
  pinned at the top of the menu with a separator below it

## [1.2] - 2026-03-10

### Added

- Spanish (`es`) locale resource bundles for the demo application
  (`AppFrameBundle_es.json`, `AppDialogBundle_es.json`, `AppWindowBundle_es.json`,
  `TestComponentSourceBundle_es.json`) and the library logger (`SwingLoggerBundle_es.properties`)

### Changed

- Updated `i18n-core` dependency from 1.3.3 to 1.4.0
- Demo app: `computeDirSize()` refactored from recursive `File.listFiles()` to
  `Files.walkFileTree()` with a `SimpleFileVisitor`; iCloud placeholder stubs
  (`.FileName.icloud`) are skipped to avoid blocking on network I/O; I/O errors
  are skipped rather than aborting the walk
- Demo app: directory size computation moved to a shared `dirSizeWorker`
  (`SwingWorker`) with cancellation on navigation, replacing one-off per-cell workers
- Demo app: file sort comparator moved into the file-table background worker so
  that sort state is captured once off the EDT rather than read from instance fields
  during `getValueAt()`


## [1.1] - 2026-03-06

### Added

- `TestComponentSourceProvider` SPI interface and module-path registration for
  `TestComponentSource`, fixing module-path test failure

### Changed

- Updated `i18n-core` dependency from 1.0 to 1.3.3
- `validNames()` return type narrowed from `HashSet<String>` to `Set<String>`
  in `ComponentPropertyBundle` and all 13 subclasses
- `ModuleResourceBundleCallback` constructor made `private`; use the
  `GET_BUNDLE_CALLBACK` singleton
- `BufferedImageResourceLoader.getBufferedImageResource()`: `ImageIO.read()`
  returning `null` (no suitable image reader) now falls through to the next
  loading strategy rather than silently returning `null` to the caller
- `BufferedImageResourceLoader`: StackWalker walk uses a lazy stream that
  stops at the first matching frame instead of materialising the full stack

### Removed

- Vestigial `META-INF/services/` entries for `XxxProvider` interfaces from
  both the main library and the test module; these files were not consulted in
  either classpath mode (which uses `AssociativeResourceBundleControl`) or
  module-path mode (which uses `module-info.class` `provides` directives)

### Fixed

- `ModuleResourceBundleCallback.getResourceBundle()` no longer declares
  `throws NullPointerException` (unchecked exceptions do not belong in a
  `throws` clause)
- `LookAndFeelJMenuItem.updateText()`: replaced `/** */` block comment inside
  method body with a line comment

## [1.0] - 2026-03-06

### Added

- `LocalizableJFrame` — locale-event source extending `JFrame`; fires locale events on the EDT
- `LocalizableJDialog` — locale-event source extending `JDialog`
- `LocalizableJWindow` — locale-event source extending `JWindow`
- `ResourcefulJButton` — localizes button text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJCheckBox` — localizes check box text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJRadioButton` — localizes radio button text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJToggleButton` — localizes toggle button text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJLabel` — localizes label text, mnemonic, icon, disabled icon, and accessibility attributes
- `ResourcefulJMenu` — localizes menu text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJMenuBar` — localizes menu bar name, tooltip, font, and accessibility attributes
- `ResourcefulJMenuItem` — localizes menu item text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJCheckBoxMenuItem` — localizes check box menu item text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJRadioButtonMenuItem` — localizes radio button menu item text, mnemonic, label, icons, and accessibility attributes
- `ResourcefulJPopupMenu` — localizes popup menu name, tooltip, font, and accessibility attributes
- `ResourcefulJPopupMenuSeparator` — localizes popup menu separator name, tooltip, font, and accessibility attributes
- `ResourcefulJTextField` — localizes text field name, tooltip, font, and accessibility attributes
- `ResourcefulJTextArea` — localizes text area name, tooltip, font, and accessibility attributes
- `ResourcefulJEditorPane` — localizes editor pane name, tooltip, font, and accessibility attributes
- `ResourcefulJTextPane` — localizes text pane name, tooltip, font, and accessibility attributes
- `ResourcefulJPasswordField` — localizes password field name, tooltip, font, and accessibility attributes
- `ResourcefulJFormattedTextField` — localizes formatted text field name, tooltip, font, and accessibility attributes
- `ResourcefulJPanel` — localizes panel name, tooltip, font, and accessibility attributes
- `ResourcefulJComboBox` — localizes combo box name, tooltip, font, item values, and accessibility attributes
- `ResourcefulJList` — localizes list name, tooltip, font, and accessibility attributes
- `ResourcefulJSpinner` — localizes spinner name, tooltip, font, and accessibility attributes
- `ResourcefulJSlider` — localizes slider name, tooltip, font, and accessibility attributes
- `ResourcefulJTree` — localizes tree name, tooltip, font, and accessibility attributes
- `ResourcefulJTable` — localizes table name, tooltip, font, and accessibility attributes; calls `createDefaultColumnsFromModel()` on locale change
- `ResourcefulJTableHeader` — localizes table header name, tooltip, font, and accessibility attributes
- `ResourcefulJProgressBar` — localizes progress bar name, tooltip, font, progress string, and accessibility attributes
- `ResourcefulJScrollBar` — localizes scroll bar name, tooltip, font, and accessibility attributes
- `ResourcefulJScrollPane` — localizes scroll pane name, tooltip, font, and accessibility attributes
- `ResourcefulJSplitPane` — localizes split pane name, tooltip, font, and accessibility attributes
- `ResourcefulJTabbedPane` — localizes tabbed pane name, tooltip, font, and per-tab titles, tooltips, and icons
- `ResourcefulJInternalFrame` — localizes internal frame name, tooltip, font, title, frame icon, and accessibility attributes
- `ResourcefulJLayeredPane` — localizes layered pane name, tooltip, font, and accessibility attributes
- `ResourcefulJDesktopPane` — localizes desktop pane name, tooltip, font, and accessibility attributes
- `ResourcefulJDesktopIcon` — localizes desktop icon name, tooltip, font, and accessibility attributes
- `ResourcefulJRootPane` — localizes root pane name, tooltip, font, and accessibility attributes
- `ResourcefulJViewport` — localizes viewport name, tooltip, font, and accessibility attributes
- `ResourcefulJOptionPane` — persistent option pane component with localized title and button option labels; provides `showConfirmDialog()`, `showInputDialog()`, and `showMessageDialog()` methods
- `ResourcefulJColorChooser` — localizes color chooser name, tooltip, font, and accessibility attributes
- `ResourcefulJFileChooser` — localizes file chooser name, tooltip, font, dialog title, approve button text and tooltip, and accessibility attributes
- `ResourcefulBox` — localizes `Box` name, tooltip, font, and accessibility attributes
- `ResourcefulBoxFiller` — localizes `Box.Filler` name, tooltip, font, and accessibility attributes
- `ResourcefulJSeparator` — localizes separator name, tooltip, font, and accessibility attributes
- `ResourcefulJToolBar` — localizes tool bar name, tooltip, font, and accessibility attributes
- `ResourcefulJToolBarSeparator` — localizes tool bar separator name, tooltip, font, and accessibility attributes
- `ResourcefulJToolTip` — localizes tooltip component name, font, tip text, and accessibility attributes
- `ResourcefulJSpinnerDefaultEditor` — localizes `JSpinner.DefaultEditor` name, tooltip, font, and accessibility attributes
- `ResourcefulJSpinnerDateEditor` — localizes `JSpinner.DateEditor` name, tooltip, font, and accessibility attributes
- `ResourcefulJSpinnerNumberEditor` — localizes `JSpinner.NumberEditor` name, tooltip, font, and accessibility attributes
- `ResourcefulJSpinnerListEditor` — localizes `JSpinner.ListEditor` name, tooltip, font, and accessibility attributes
- `LocaleJMenuItem` — selects an application locale from a menu
- `LookAndFeelJMenuItem` — applies a Swing Look and Feel from a menu
- `ComponentPropertyBundle` — base `AttributeCollection` for name, font, and accessibility properties
- `JComponentPropertyBundle` — extends `ComponentPropertyBundle` with tooltip
- `ButtonPropertyBundle` — extends `ComponentPropertyBundle` with label
- `AbstractButtonPropertyBundle` — extends `JComponentPropertyBundle` with text, label, mnemonic, and 7 icon types
- `JLabelPropertyBundle` — extends `JComponentPropertyBundle` with text, mnemonic, icon, and disabled icon
- `JComboBoxPropertyBundle` — extends `JComponentPropertyBundle` with item values (string array)
- `JProgressBarPropertyBundle` — extends `JComponentPropertyBundle` with progress string
- `JInternalFramePropertyBundle` — extends `JComponentPropertyBundle` with title and frame icon
- `JTabbedPaneTabPropertyBundle` — per-tab `AttributeCollection` for title, tooltip, and icon
- `JOptionPanePropertyBundle` — extends `JComponentPropertyBundle` with title and button options (string array)
- `JFileChooserPropertyBundle` — extends `JComponentPropertyBundle` with dialog title, approve button text, and approve button tooltip
- `JToolTipPropertyBundle` — extends `JComponentPropertyBundle` with tip text
- `WindowPropertyBundle` — extends `ComponentPropertyBundle` with icon images
- `FramePropertyBundle` — extends `WindowPropertyBundle` with title
- `SwingResourcefulDelegate` — centralizes Swing-specific locale orchestration and EDT dispatch
- Classpath and module-path test profiles
- Release profile with javadoc and source JAR generation
