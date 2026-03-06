# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

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
