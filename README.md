# i18n-swing

Swing bindings for the [i18n-core](https://github.com/clydegerber/i18n) internationalization framework.

This library provides locale-aware Swing components that automatically update
their text, icons, fonts, mnemonics, tooltips, and accessibility attributes
when the application locale changes.

## Requirements

- Java 17 or later
- [i18n-core](https://github.com/clydegerber/i18n) 1.4.0 or later (pulled in transitively)

## Installation

### Maven

```xml
<dependency>
    <groupId>dev.javai18n</groupId>
    <artifactId>i18n-swing</artifactId>
    <version>1.2</version>
</dependency>
```

### Module Declaration

```java
module my.module
{
    requires dev.javai18n.swing;
}
```

## Components

### Localizable top-level containers

These classes serve as the locale-event source for all Resourceful components they own.
They implement `Localizable` and dispatch locale events on the EDT when `setBundleLocale()` is called.

| Class | Extends | Notes |
|---|---|---|
| `LocalizableJFrame` | `JFrame` | Standard application frame; bundle key `"windowProperties"` uses `FramePropertyBundle` |
| `LocalizableJDialog` | `JDialog` | Modal or non-modal dialog; bundle key `"windowProperties"` uses `FramePropertyBundle` |
| `LocalizableJWindow` | `JWindow` | Undecorated window; bundle key `"windowProperties"` uses `WindowPropertyBundle` |

### Resourceful components

All Resourceful components implement `Resourceful` and `LocaleEventListener`.
Each receives an `updateLocaleSpecificValues()` call on the EDT whenever its source fires a locale change.

The base set of localized properties for every Resourceful component is:
**name, tooltip, font, accessible name, accessible description** (from `JComponentPropertyBundle`).
Additional properties are noted in the table below.

**Buttons and toggles**

| Class | Extends | Additional Localized Properties |
|---|---|---|
| `ResourcefulJButton` | `JButton` | text, label, mnemonic, 7 icon types |
| `ResourcefulJCheckBox` | `JCheckBox` | text, label, mnemonic, 7 icon types |
| `ResourcefulJRadioButton` | `JRadioButton` | text, label, mnemonic, 7 icon types |
| `ResourcefulJToggleButton` | `JToggleButton` | text, label, mnemonic, 7 icon types |

**Menus**

| Class | Extends | Additional Localized Properties |
|---|---|---|
| `ResourcefulJMenu` | `JMenu` | text, label, mnemonic, 7 icon types |
| `ResourcefulJMenuBar` | `JMenuBar` | — |
| `ResourcefulJMenuItem` | `JMenuItem` | text, label, mnemonic, 7 icon types |
| `ResourcefulJCheckBoxMenuItem` | `JCheckBoxMenuItem` | text, label, mnemonic, 7 icon types |
| `ResourcefulJRadioButtonMenuItem` | `JRadioButtonMenuItem` | text, label, mnemonic, 7 icon types |
| `ResourcefulJPopupMenu` | `JPopupMenu` | — |
| `ResourcefulJPopupMenuSeparator` | `JPopupMenu.Separator` | — |

**Text components**

| Class | Extends | Additional Localized Properties |
|---|---|---|
| `ResourcefulJTextField` | `JTextField` | — |
| `ResourcefulJTextArea` | `JTextArea` | — |
| `ResourcefulJEditorPane` | `JEditorPane` | — |
| `ResourcefulJTextPane` | `JTextPane` | — |
| `ResourcefulJPasswordField` | `JPasswordField` | — |
| `ResourcefulJFormattedTextField` | `JFormattedTextField` | — |

**Data and selection**

| Class | Extends | Additional Localized Properties |
|---|---|---|
| `ResourcefulJComboBox` | `JComboBox` | item values (string array) |
| `ResourcefulJList` | `JList` | — |
| `ResourcefulJSpinner` | `JSpinner` | — |
| `ResourcefulJSlider` | `JSlider` | — |
| `ResourcefulJTree` | `JTree` | — |
| `ResourcefulJTable` | `JTable` | — |
| `ResourcefulJTableHeader` | `JTableHeader` | — |
| `ResourcefulJProgressBar` | `JProgressBar` | progress string |
| `ResourcefulJScrollBar` | `JScrollBar` | — |
| `ResourcefulJColorChooser` | `JColorChooser` | — |
| `ResourcefulJFileChooser` | `JFileChooser` | dialog title, approve button text, approve button tooltip |

**Panes and containers**

| Class | Extends | Additional Localized Properties |
|---|---|---|
| `ResourcefulJPanel` | `JPanel` | — |
| `ResourcefulJScrollPane` | `JScrollPane` | — |
| `ResourcefulJSplitPane` | `JSplitPane` | — |
| `ResourcefulJTabbedPane` | `JTabbedPane` | tab titles, tab tooltips, tab icons (per-tab via `JTabbedPaneTabPropertyBundle`) |
| `ResourcefulJInternalFrame` | `JInternalFrame` | title, frame icon |
| `ResourcefulJLayeredPane` | `JLayeredPane` | — |
| `ResourcefulJDesktopPane` | `JDesktopPane` | — |
| `ResourcefulJDesktopIcon` | `JDesktopIcon` | — |
| `ResourcefulJRootPane` | `JRootPane` | — |
| `ResourcefulJViewport` | `JViewport` | — |
| `ResourcefulJOptionPane` | `JOptionPane` | dialog title, button option labels |

**Layout helpers and decorators**

| Class | Extends | Additional Localized Properties |
|---|---|---|
| `ResourcefulBox` | `Box` | — |
| `ResourcefulBoxFiller` | `Box.Filler` | — |
| `ResourcefulJLabel` | `JLabel` | text, mnemonic, icon, disabled icon |
| `ResourcefulJSeparator` | `JSeparator` | — |
| `ResourcefulJToolBar` | `JToolBar` | — |
| `ResourcefulJToolBarSeparator` | `JToolBar.Separator` | — |
| `ResourcefulJToolTip` | `JToolTip` | tip text |

**Spinner editors**

| Class | Extends | Additional Localized Properties |
|---|---|---|
| `ResourcefulJSpinnerDefaultEditor` | `JSpinner.DefaultEditor` | — |
| `ResourcefulJSpinnerDateEditor` | `JSpinner.DateEditor` | — |
| `ResourcefulJSpinnerNumberEditor` | `JSpinner.NumberEditor` | — |
| `ResourcefulJSpinnerListEditor` | `JSpinner.ListEditor` | — |

### Utility menu items

| Class | Extends | Description |
|---|---|---|
| `LocaleJMenuItem` | `ResourcefulJMenuItem` | Displays a locale name; sets the application locale when selected |
| `LookAndFeelJMenuItem` | `ResourcefulJMenuItem` | Displays a Look and Feel name; applies it when selected |

## Property Bundles

Property bundles are typed `AttributeCollection` implementations that carry the localized
values read from a JSON or XML resource file. No code generation or compilation step is needed
to add new bundle entries.

| Bundle | Extends | Fields |
|---|---|---|
| `ComponentPropertyBundle` | — | name, font, accessible name, accessible description |
| `JComponentPropertyBundle` | `ComponentPropertyBundle` | + tooltip |
| `ButtonPropertyBundle` | `ComponentPropertyBundle` | + label |
| `AbstractButtonPropertyBundle` | `JComponentPropertyBundle` | + text, label, mnemonic, icon, pressed icon, selected icon, disabled icon, disabled selected icon, rollover icon, rollover selected icon |
| `JLabelPropertyBundle` | `JComponentPropertyBundle` | + text, mnemonic, icon, disabled icon |
| `JComboBoxPropertyBundle` | `JComponentPropertyBundle` | + values (string array) |
| `JProgressBarPropertyBundle` | `JComponentPropertyBundle` | + progress string |
| `JInternalFramePropertyBundle` | `JComponentPropertyBundle` | + title, frame icon |
| `JTabbedPaneTabPropertyBundle` | — | tab title, tab tooltip, tab icon (one entry per tab) |
| `JOptionPanePropertyBundle` | `JComponentPropertyBundle` | + title, options (string array) |
| `JFileChooserPropertyBundle` | `JComponentPropertyBundle` | + dialog title, approve button text, approve button tooltip |
| `JToolTipPropertyBundle` | `JComponentPropertyBundle` | + tip text |
| `WindowPropertyBundle` | `ComponentPropertyBundle` | + icon images (list) |
| `FramePropertyBundle` | `WindowPropertyBundle` | + title |

## Quick Start

### 1. Create a Localizable frame subclass

```java
public class MyFrame extends LocalizableJFrame
{
    static
    {
        MyModuleRegistrar.ensureRegistered();
    }

    @Override
    public Locale[] getAvailableLocales()
    {
        return new Locale[]{ Locale.ROOT, Locale.FRANCE };
    }
}
```

### 2. Define a resource bundle

**MyFrameBundle.json:**

```json
{
    "okButton":
    {
        "type": "dev.javai18n.swing.AbstractButtonPropertyBundle",
        "Text": "OK",
        "Mnemonic": 79
    }
}
```

**MyFrameBundle_fr.json:**

```json
{
    "okButton":
    {
        "type": "dev.javai18n.swing.AbstractButtonPropertyBundle",
        "Text": "D'accord",
        "Mnemonic": 68
    }
}
```

### 3. Create a Resourceful component

```java
MyFrame myFrame = MyFrame.create();
Resource okResource = new Resource(myFrame, "okButton");
ResourcefulJButton okButton = ResourcefulJButton.create(okResource);
```

The button displays the correct text, mnemonic, and other properties for the current locale.
When `myFrame.setBundleLocale(locale)` is called, every attached Resourceful component
updates itself on the Event Dispatch Thread.

## Sample Application

A complete file explorer example is provided in the test sources to demonstrate the library end to end.

**Main class:** `dev.javai18n.swing.test.TestSwingApp`
**Frame class:** `dev.javai18n.swing.test.AppFrame`

Launch it from an IDE or via Maven:

```bash
mvn test-compile exec:java -Dexec.classpathScope=test \
    -Dexec.mainClass=dev.javai18n.swing.test.TestSwingApp
```

The application starts with the system Look and Feel and opens at the user's home directory.

### What it demonstrates

- **Locale switching** — Preferences > Locale menu lists the locales supported by the application
  logger by default, or all JVM locales when "All JVM Locales" is selected. Choosing a locale
  updates every label, tooltip, column header, button text, and dialog message simultaneously on
  the EDT.
- **Look and Feel switching** — Preferences > Look and Feel menu lists all installed Look and Feels;
  selecting one applies it immediately without restarting.
- **File system navigation** — a directory tree (`ResourcefulJTree`) on the left and a file table
  (`ResourcefulJTable`) on the right, connected by Back, Forward, Up, and Home toolbar buttons
  (`ResourcefulJButton`) and a path text field (`ResourcefulJTextField`).
- **Sortable file table** — sort by name, size, or date via View > Sort By radio menu items
  (`ResourcefulJRadioButtonMenuItem`); show or hide hidden files and file extensions via
  View check box menu items (`ResourcefulJCheckBoxMenuItem`).
- **List and detail views** — toggle buttons (`ResourcefulJToggleButton`) switch between a table
  view and a list view (`ResourcefulJList`).
- **Preview tab** — displays a selected text file as plain text (`ResourcefulJTextArea`) or as
  rendered HTML (`ResourcefulJEditorPane`), presented in a tabbed pane (`ResourcefulJTabbedPane`).
- **Properties panel** — an internal frame (`ResourcefulJInternalFrame`) shows the selected file's
  name, size, type, and modification date, all formatted for the current locale.
- **Context menu** — right-clicking the file table opens a popup menu (`ResourcefulJPopupMenu`)
  with localized actions.
- **Localized dialogs** — the New Folder and Delete Confirmation actions use
  `ResourcefulJOptionPane` instances whose title and button labels come from the resource bundle.
- **Widgets tab** — exercises the remaining component types: `ResourcefulJPasswordField`,
  `ResourcefulJFormattedTextField` (date/time), `ResourcefulJSpinner` (date),
  `ResourcefulJSlider` (with locale-formatted tick labels), `ResourcefulJList` (day-of-week names
  from `DateFormatSymbols`), and `ResourcefulJTextPane` (styled text).

All localized strings live in `AppFrameBundle.json` and its locale variants
(`AppFrameBundle_en.json`, `AppFrameBundle_fr.json`, etc.).

## Building

```bash
mvn clean package
```

To build with sources JAR, javadoc JAR, and GPG signing for release:

```bash
mvn -Prelease clean package
```

## Testing

To execute unit tests on the classpath (default):

```bash
mvn clean test
```

To execute unit tests under JPMS:

```bash
mvn clean test -Ptest-modulepath
```

## License

This project is licensed under the
[Apache License, Version 2.0](LICENSE).
