# Contributing to i18n-swing

Thank you for your interest in contributing! This guide covers how to build,
test, and submit changes.

## Prerequisites

- **Java 25** or later
- **Maven** (included with Apache NetBeans, or install separately)
- **i18n-core 1.3.1** installed to local Maven repository (`mvn install` from the i18n project)

## Building

```sh
mvn clean package
```

## Running Tests

**Classpath mode** (default):

```sh
mvn test
```

**Module mode**:

```sh
mvn test -Ptest-modulepath
```

**Release build** (includes javadoc verification):

```sh
mvn -Prelease clean package javadoc:jar
```

All three must pass before submitting a pull request.

## Code Style

The project follows these conventions:

- **Allman brace style** — opening brace on its own line for all declarations and control flow:

  ```java
  if (condition)
  {
      doSomething();
  }
  ```

  JSON resource bundle files follow the same convention — object values have `{` on its own line;
  scalar-only arrays are kept inline:

  ```json
  {
      "myButton":
      {
          "type": "dev.javai18n.swing.AbstractButtonPropertyBundle",
          "Options": ["OK", "Cancel"]
      }
  }
  ```

- **Apache License 2.0 header** on every source file
- **Javadoc on all public and protected members** — `doclint all` must produce zero warnings
- **Yoda-style null checks** — `null == x` rather than `x == null`
- **No wildcard imports** — always use explicit imports
- **4-space indentation, no tabs**

## Adding a New Resourceful Component

1. Extend the target Swing class and implement `Resourceful` and `LocaleEventListener`
2. Add a `SwingResourcefulDelegate` field; wire `this::setLocale` and `this::updateLocaleSpecificValues`
3. Implement a `static create(Resource)` factory (plus any alternate factories the Swing class provides)
4. Override `updateLocaleSpecificValues()` to read from the appropriate `PropertyBundle` type
5. Add unit tests in `src/test/java/dev/javai18n/swing/test/` with both `testInitialProperties` and `testLocaleChange` test methods
6. Add bundle entries (English and French at minimum) to `TestComponentSourceBundle.json` and `TestComponentSourceBundle_fr.json`

## Submitting Changes

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Ensure tests pass in **both** classpath and module mode
5. Ensure javadoc builds with **zero** warnings
6. Open a pull request

## Reporting Issues

Please use [GitHub Issues](../../issues) to report bugs or request features.

## License

All contributions are licensed under the
[Apache License 2.0](LICENSE).
