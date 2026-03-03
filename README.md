# i18n-swing

Swing bindings for the [i18n-core](https://github.com/clydegerber/i18n) internationalization framework.

This library provides locale-aware Swing components that automatically update
their text, icons, fonts, mnemonics, tooltips, and accessibility attributes
when the application locale changes.

## Components

| Class | Extends | Localized Properties |
|---|---|---|
| `ResourcefulJButton` | `JButton` | name, tooltip, font, text, mnemonic, accessible name/description, icons (normal, pressed, selected, disabled, disabled selected, rollover, rollover selected) |
| `ResourcefulJLabel` | `JLabel` | name, tooltip, font, text, mnemonic, icons (normal, disabled) |
| `ResourcefulJMenu` | `JMenu` | name, tooltip, font, text, mnemonic, accessible name/description |
| `ResourcefulJMenuBar` | `JMenuBar` | name, tooltip, font, accessible name/description |
| `ResourcefulJMenuItem` | `JMenuItem` | name, tooltip, font, text, mnemonic, accessible name/description |
| `ResourcefulJPanel` | `JPanel` | name, tooltip, font, accessible name/description |
| `ResourcefulJComboBox` | `JComboBox` | name, tooltip, font, accessible name/description |
| `LocaleJMenuItem` | `ResourcefulJMenuItem` | Displays a locale name and sets the application locale when selected |
| `LookAndFeelJMenuItem` | `ResourcefulJMenuItem` | Displays a Look and Feel name and applies it when selected |

## Requirements

- Java 16 or later
- [i18n-core](https://github.com/clydegerber/i18n) (provided as a transitive dependency)

## Installation

### Maven

```xml
<dependency>
    <groupId>dev.javai18n</groupId>
    <artifactId>i18n-swing</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Module Declaration

```java
module my.module
{
    requires dev.javai18n.swing;
}
```

## Quick Start

### 1. Define a Localizable source

```java
public class MyFrame extends LocalizableImpl
{
    static
    {
        GetResourceBundleRegistrar
            .registerGetResourceBundleCallback(callback);
    }
}
```

### 2. Create a resource bundle with component properties

**MyFrameBundle.json:**
```json
{
    "okButton":
    {
        "type": "dev.javai18n.swing.AbstractButtonPropertyBundle",
        "text": "OK",
        "mnemonic": 79
    }
}
```

**MyFrameBundle_fr.json:**
```json
{
    "okButton":
    {
        "type": "dev.javai18n.swing.AbstractButtonPropertyBundle",
        "text": "D'accord",
        "mnemonic": 68
    }
}
```

### 3. Create a Resourceful component

```java
Resource okResource = new Resource(myFrame, "okButton");
ResourcefulJButton okButton = ResourcefulJButton.create(okResource);
```

The button automatically displays the correct text, mnemonic, and other
properties for the current locale. When `myFrame.setBundleLocale()` is
called, the button updates itself on the Event Dispatch Thread.

## Property Bundles

Components use typed property bundles to define their localized attributes:

- **`JComponentPropertyBundle`** — name, tooltip, font, accessible name/description
- **`AbstractButtonPropertyBundle`** — extends `JComponentPropertyBundle` with text, label, mnemonic, and icons

These bundles implement `AttributeCollection` and can be defined in JSON
or XML resource files without any code or compilation.

## Building

```bash
mvn clean package
```

To build with sources JAR, javadoc JAR, and GPG signing for release:

```bash
mvn -Prelease clean package
```

## Testing

To execute unit tests under JPMS:

```bash
mvn clean test -Ptest-modulepath
```

To execute unit tests on the classpath:

```bash
mvn clean test
```

## License

This project is licensed under the
[Apache License, Version 2.0](LICENSE).
