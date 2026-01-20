# Noir JetBrains Plugin

![Build](https://github.com/ZKLSOL/JetBrains-Noir-Syntax-Highlight-Plugin/workflows/Build/badge.svg)

Language support for the [Noir](https://noir-lang.org/) zero-knowledge programming language in JetBrains IDEs.

<!-- Plugin description -->
Language support for the Noir zero-knowledge programming language.

### Features
- Native syntax highlighting for .nr files
- LSP integration with `nargo lsp` for full IDE intelligence
- Code completion, diagnostics, go-to-definition, hover, and more
- Live templates for common Noir constructs
- Gutter icons for test functions
- Macro expansion viewer
- Standard library browser
- Comment toggling and bracket matching
<!-- Plugin description end -->

## Features

### Syntax Highlighting
Native syntax highlighting for Noir source files:
- Keywords (`fn`, `struct`, `if`, `else`, `for`, etc.)
- Types (`u8`, `u32`, `Field`, `bool`, etc.)
- Strings (regular, raw `r#""#`, f-strings `f""`)
- Comments (line `//` and block `/* */`)
- Numeric literals (decimal, hex)
- Attributes (`#[...]`)

### LSP Integration
The plugin connects to `nargo lsp` to provide:
- **Code Completion** - Context-aware suggestions
- **Diagnostics** - Real-time error and warning highlighting
- **Go to Definition** - Navigate to symbol definitions
- **Hover Information** - Type info and documentation on hover
- **Find References** - Find all usages of a symbol
- **Signature Help** - Function parameter hints
- **Code Actions** - Quick fixes and refactoring suggestions

### Live Templates
Type these prefixes and press Tab to expand:

| Prefix | Expands to |
|--------|------------|
| `fn` | Function declaration |
| `fnmain` | Main function |
| `struct` | Struct declaration |
| `let` | Variable declaration |
| `for` | For loop |
| `forin` | For-in loop |
| `if` | If statement |
| `elseif` | Else-if statement |
| `else` | Else block |
| `mod` | Module declaration |
| `use` | Use statement |
| `letfor` | Array comprehension |
| `letforin` | Array comprehension with index |

### Gutter Icons
- Test tube icon appears next to `#[test]` functions

### Actions
Available in **Tools → Noir** menu:
- **Restart Language Server** - Restart the LSP connection
- **Expand Macros** - Run `nargo expand` and view result in scratch file
- **Browse Standard Library** - Open Noir stdlib source

## Requirements

- **JetBrains IDE** version 2025.2 or later (IntelliJ IDEA, CLion, RustRover, WebStorm, etc.)
- **[Nargo](https://noir-lang.org/docs/getting_started/installation/)** installed for LSP features

## Installation

### From JetBrains Marketplace (Recommended)
1. Open your JetBrains IDE
2. Go to **Settings/Preferences → Plugins → Marketplace**
3. Search for "Noir"
4. Click **Install**

### Manual Installation
1. Download the latest release from [GitHub Releases](https://github.com/ZKLSOL/JetBrains-Noir-Syntax-Highlight-Plugin/releases)
2. Go to **Settings/Preferences → Plugins → ⚙️ → Install Plugin from Disk...**
3. Select the downloaded `.zip` file

## Configuration

### Settings Location
**Settings/Preferences → Languages & Frameworks → Noir**

### Available Settings
| Setting | Description | Default |
|---------|-------------|---------|
| Nargo path | Path to nargo executable | Auto-detect from PATH |
| Enable LSP | Enable/disable language server | Enabled |
| Lightweight mode | Disable completions, signature help, code actions | Disabled |

### Project-Level Override
You can override the nargo path per-project. Project settings take precedence over application settings.

## Building from Source

### Prerequisites
- JDK 17 or later
- Gradle 8.0+ (wrapper included)

### Build Commands

```bash
# Build the plugin
./gradlew buildPlugin

# The plugin zip will be at:
# build/distributions/JetBrains-Noir-Syntax-Highlight-Plugin-<version>.zip
```

### Testing Locally

```bash
# Run a sandbox IDE with the plugin installed
./gradlew runIde

# Run tests
./gradlew test

# Check compatibility with different IDE versions
./gradlew verifyPlugin
```

### Development Workflow

1. Clone the repository
2. Open in IntelliJ IDEA (the IDE will recognize it as a Gradle project)
3. Run `./gradlew runIde` to test changes in a sandbox environment
4. Make changes and the sandbox IDE will reload automatically

## Project Structure

```
src/main/
├── kotlin/cash/turbine/noir/
│   ├── NoirLanguage.kt          # Language definition
│   ├── NoirFileType.kt          # File type for .nr files
│   ├── NoirFile.kt              # PSI file for Noir
│   ├── NoirLexer.kt             # Syntax highlighting lexer
│   ├── NoirTokenTypes.kt        # Token element types
│   ├── NoirParser.kt            # Simple pass-through parser
│   ├── NoirParserDefinition.kt  # Parser definition
│   ├── NoirPsiElement.kt        # Base PSI element
│   ├── NoirSyntaxHighlighter.kt # Syntax highlighter
│   ├── NoirSyntaxHighlighterFactory.kt
│   ├── NoirIcons.kt             # Plugin icons
│   ├── NoirCommenter.kt         # Comment toggling
│   ├── NoirBraceMatcher.kt      # Bracket matching
│   ├── NoirTemplateContextType.kt
│   ├── NoirTestLineMarkerProvider.kt  # Test gutter icons
│   ├── lsp/
│   │   ├── NoirLspServerDescriptor.kt
│   │   └── NoirLspServerSupportProvider.kt
│   ├── settings/
│   │   ├── NoirSettings.kt
│   │   ├── NoirProjectSettings.kt
│   │   └── NoirSettingsConfigurable.kt
│   ├── stdlib/
│   │   ├── NoirStdlibFileSystem.kt
│   │   └── NoirStdlibVirtualFile.kt
│   └── actions/
│       ├── RestartLspAction.kt
│       ├── ExpandMacrosAction.kt
│       └── BrowseStdlibAction.kt
├── resources/
│   ├── META-INF/plugin.xml      # Plugin configuration
│   ├── liveTemplates/Noir.xml   # Live templates
│   └── icons/                   # SVG icons
└── test/
    └── kotlin/cash/turbine/noir/
        ├── NoirFileTypeTest.kt  # File type tests
        ├── NoirLexerTest.kt     # Lexer tests
        └── NoirSettingsTest.kt  # Settings tests
```

## Troubleshooting

### LSP not working
1. Ensure `nargo` is installed and in your PATH
2. Check **Settings → Languages & Frameworks → Noir** for the nargo path
3. Try **Tools → Noir → Restart Language Server**
4. Check the IDE log for errors: **Help → Show Log in Finder/Explorer**

### No syntax highlighting
1. Ensure you're using the latest version of the plugin
2. Try reopening the `.nr` file
3. Check that the file has the `.nr` extension

## Contributing

Contributions are welcome! Please see the [DEV.md](DEV.md) file for implementation details.

## License

MIT License - see [LICENSE](LICENSE) for details.

## Links

- [Noir Language](https://noir-lang.org/)
- [Noir Documentation](https://noir-lang.org/docs/)
- [Nargo Installation](https://noir-lang.org/docs/getting_started/installation/)
- [JetBrains Plugin Development](https://plugins.jetbrains.com/docs/intellij/)

---
Based on the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template).
