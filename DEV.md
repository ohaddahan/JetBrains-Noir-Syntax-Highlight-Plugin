# Noir JetBrains Plugin - Developer Guide

This document describes the implementation details of the Noir JetBrains plugin.

## Architecture Overview

The plugin follows the standard JetBrains plugin architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                    JetBrains IDE                            │
├─────────────────────────────────────────────────────────────┤
│  Plugin Components                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Language   │  │   Syntax     │  │   Settings   │      │
│  │  Definition  │  │ Highlighting │  │     UI       │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │     LSP      │  │    Live      │  │   Actions    │      │
│  │  Integration │  │  Templates   │  │              │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Gutter     │  │   Stdlib     │  │    Icons     │      │
│  │    Icons     │  │   Viewer     │  │              │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
├─────────────────────────────────────────────────────────────┤
│                    nargo lsp (external)                     │
└─────────────────────────────────────────────────────────────┘
```

## Key Files and Their Purposes

### Language Definition

#### `NoirLanguage.kt`
```kotlin
object NoirLanguage : Language("Noir")
```
- Singleton object defining the Noir language
- Used as identifier throughout the plugin
- Registered implicitly via `NoirFileType`

#### `NoirFileType.kt`
```kotlin
object NoirFileType : LanguageFileType(NoirLanguage)
```
- Associates `.nr` file extension with Noir language
- Provides file icon via `NoirIcons.FILE`
- Registered in `plugin.xml`:
```xml
<fileType name="Noir" implementationClass="cash.turbine.noir.NoirFileType"
          fieldName="INSTANCE" language="Noir" extensions="nr"/>
```

### Syntax Highlighting

The plugin uses a native JetBrains lexer and syntax highlighter for reliable highlighting that works across all IDE versions without external dependencies.

#### `NoirLexer.kt`
Custom lexer that tokenizes Noir source code:
- Keywords (`fn`, `struct`, `if`, `else`, `for`, `loop`, `while`, `match`, etc.)
- Types (`u8`, `u32`, `Field`, `bool`, etc.)
- Comments (line `//` and block `/* */` with nesting support)
- Strings (regular, raw `r#""#`, f-strings `f""`)
- Numeric literals (decimal, hex)
- Attributes (`#[...]`)
- Boolean literals (`true`, `false`)
- Operators and braces

#### `NoirTokenTypes.kt`
Defines token element types for the lexer output:
- `KEYWORD`, `TYPE`, `IDENTIFIER`, `STRING`, `NUMBER`, `BOOLEAN`
- `LINE_COMMENT`, `BLOCK_COMMENT`
- `OPERATOR`, `BRACE`, `ATTRIBUTE`
- `WHITESPACE`, `BAD_CHARACTER`

#### `NoirSyntaxHighlighter.kt`
Maps token types to IDE color scheme attributes using `DefaultLanguageHighlighterColors`:
- Keywords → `KEYWORD`
- Types → `CLASS_NAME`
- Strings → `STRING`
- Numbers → `NUMBER`
- Comments → `LINE_COMMENT`
- Attributes → `METADATA`

#### `NoirSyntaxHighlighterFactory.kt`
Factory that provides the syntax highlighter for Noir files.

#### `NoirParserDefinition.kt`
Basic parser definition required for language integration. Uses a simple pass-through parser since full parsing is handled by the LSP.

#### `NoirCommenter.kt`
```kotlin
class NoirCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "//"
    override fun getBlockCommentPrefix(): String = "/*"
    override fun getBlockCommentSuffix(): String = "*/"
}
```
- Enables Cmd/Ctrl+/ for line comments
- Enables Cmd/Ctrl+Shift+/ for block comments

#### `NoirBraceMatcher.kt`
```kotlin
class NoirBraceMatcher : PairedBraceMatcher
```
- Provides bracket matching for `{}`, `[]`, `()`

### LSP Integration

The plugin uses JetBrains' native LSP API (available in 2025.2+).

#### `lsp/NoirLspServerSupportProvider.kt`
```kotlin
class NoirLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(project, file, serverStarter) {
        if (file.extension == "nr") {
            serverStarter.ensureServerStarted(NoirLspServerDescriptor(project))
        }
    }
}
```
- Entry point for LSP integration
- Checks if LSP is enabled in settings
- Starts the LSP server when a `.nr` file is opened

#### `lsp/NoirLspServerDescriptor.kt`
```kotlin
class NoirLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Noir") {
    override val lspCustomization: LspCustomization = object : LspCustomization() {
        // Use default customizations - nargo lsp provides all features
    }

    override fun createCommandLine(): GeneralCommandLine {
        return GeneralCommandLine(findNargoPath(), "lsp")
    }
}
```
- Configures how to start `nargo lsp`
- Uses the new `lspCustomization` API for feature configuration
- Implements nargo PATH auto-detection:
  1. Check project settings override
  2. Check application settings
  3. Search system PATH for `nargo` executable
- Uses stdio transport for LSP communication

### Settings

#### `settings/NoirSettings.kt`
```kotlin
@Service(Service.Level.APP)
@State(name = "NoirSettings", storages = [Storage("NoirSettings.xml")])
class NoirSettings : PersistentStateComponent<NoirSettings> {
    var nargoPath: String = ""
    var enableLsp: Boolean = true
    var lightweightMode: Boolean = false
}
```
- Application-level (global) settings
- Persisted in `NoirSettings.xml`
- Accessed via `NoirSettings.getInstance()`

#### `settings/NoirProjectSettings.kt`
```kotlin
@Service(Service.Level.PROJECT)
@State(name = "NoirProjectSettings", storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
class NoirProjectSettings : PersistentStateComponent<NoirProjectSettings> {
    var nargoPathOverride: String = ""
}
```
- Project-level settings (override global)
- Stored in `.idea/workspace.xml`
- `getEffectiveNargoPath()` returns project override or falls back to global

#### `settings/NoirSettingsConfigurable.kt`
```kotlin
class NoirSettingsConfigurable : Configurable {
    override fun getDisplayName(): String = "Noir"
    override fun createComponent(): JComponent { ... }
}
```
- Settings UI panel
- Located at Settings → Languages & Frameworks → Noir
- Uses JetBrains UI DSL for layout

### Live Templates

#### `liveTemplates/Noir.xml`
```xml
<templateSet group="Noir">
    <template name="fn" value="fn $NAME$($PARAMS$) {&#10;    $END$&#10;}" ...>
        <variable name="NAME" defaultValue="&quot;function_name&quot;" .../>
    </template>
    ...
</templateSet>
```
- 13 templates for common Noir constructs
- Variables use `$NAME$` syntax
- `$END$` marks cursor position after expansion

#### `NoirTemplateContextType.kt`
```kotlin
class NoirTemplateContextType : TemplateContextType("Noir") {
    override fun isInContext(context: TemplateActionContext): Boolean {
        return context.file.name.endsWith(".nr")
    }
}
```
- Defines when templates are available
- Templates only show in `.nr` files

### Gutter Icons

#### `NoirTestLineMarkerProvider.kt`
```kotlin
class NoirTestLineMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        // Detect #[test] attributes
        // Return line marker with test icon
    }
}
```
- Provides gutter icons for test functions
- Detects `#[test]` attributes in Noir files
- Shows test icon in the gutter next to test functions
- Registered as `codeInsight.lineMarkerProvider`

### Stdlib Viewer

#### `stdlib/NoirStdlibFileSystem.kt`
```kotlin
class NoirStdlibFileSystem : VirtualFileSystem() {
    override fun getProtocol(): String = "noir-std"
    override fun findFileByPath(path: String): VirtualFile? { ... }
}
```
- Virtual file system for browsing Noir standard library
- Implements `VirtualFileSystem` with `noir-std` protocol
- Auto-detects stdlib location from nargo installation
- Read-only access to stdlib source files

#### `stdlib/NoirStdlibVirtualFile.kt`
Virtual file implementation for stdlib files.

### Actions

#### `actions/RestartLspAction.kt`
```kotlin
class RestartLspAction : AnAction("Restart Noir Language Server") {
    override fun actionPerformed(e: AnActionEvent) {
        LspServerManager.getInstance(project).stopAndRestartIfNeeded(
            NoirLspServerSupportProvider::class.java
        )
    }
}
```
- Restarts the LSP server
- Useful when nargo is updated or server crashes

#### `actions/ExpandMacrosAction.kt`
```kotlin
class ExpandMacrosAction : AnAction("Expand Macros") {
    override fun actionPerformed(e: AnActionEvent) {
        // Run `nargo expand` in background
        // Open result in scratch file
    }
}
```
- Runs `nargo expand` command
- Shows expanded code in a read-only scratch file
- Uses `ScratchRootType.getInstance().createScratchFile()`

#### `actions/BrowseStdlibAction.kt`
```kotlin
class BrowseStdlibAction : AnAction("Browse Standard Library") {
    override fun actionPerformed(e: AnActionEvent) {
        // Find stdlib location
        // Open lib.nr in editor
    }
}
```
- Opens the Noir standard library in the editor
- Auto-detects stdlib location from nargo installation

### Icons

#### `NoirIcons.kt`
```kotlin
object NoirIcons {
    val FILE: Icon = IconLoader.getIcon("/icons/noir.svg", NoirIcons::class.java)
    val TEST: Icon = IconLoader.getIcon("/icons/test.svg", NoirIcons::class.java)
}
```

#### `icons/noir.svg`
- 16x16 SVG icon for `.nr` files
- Dark background with red "N" letter

#### `icons/test.svg`
- Test tube icon for test function gutter markers

### Plugin Configuration

#### `META-INF/plugin.xml`
Main plugin descriptor:
```xml
<idea-plugin>
    <id>cash.turbine.noir</id>
    <name>Noir</name>

    <depends>com.intellij.modules.platform</depends>

    <extensions defaultExtensionNs="com.intellij">
        <!-- File type, syntax highlighter, parser, commenter -->
        <!-- Settings, LSP, templates, gutter icons, stdlib -->
    </extensions>

    <actions>
        <!-- Tools menu actions -->
    </actions>
</idea-plugin>
```

## Build Configuration

### `gradle.properties`
```properties
pluginGroup = cash.turbine.noir
pluginName = Noir
pluginVersion = 0.1.0
pluginSinceBuild = 252          # IntelliJ 2025.2
platformVersion = 2025.2
```

### `build.gradle.kts`
Key configuration:
```kotlin
kotlin {
    jvmToolchain(17)  # Must match target IDE's Java version
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }
    }
}
```

## Development Tips

### Running the Plugin
```bash
./gradlew runIde
```
This launches a sandbox IntelliJ IDEA with the plugin installed.

### Running Tests
```bash
./gradlew test
```
Runs all unit tests including lexer, file type, and settings tests.

### Debugging
1. Create a "Plugin" run configuration in IntelliJ
2. Set breakpoints in your code
3. Run in debug mode

### Checking Logs
- IDE logs: Help → Show Log in Finder/Explorer
- LSP logs: Look for "Noir" in the log

### Common Issues

**Java version mismatch**: If you see `UnsupportedClassVersionError`, ensure `jvmToolchain` matches the target IDE's Java version (17 for 2023.2).

**LSP not starting**: Check that `nargo` is in PATH or configured in settings. Look for errors in IDE logs.

**Syntax highlighting not working**: Ensure the file has `.nr` extension. Try reopening the file or restarting the IDE.

## Testing

### Test Files
- `NoirFileTypeTest.kt` - Tests file type recognition and associations
- `NoirLexerTest.kt` - Tests lexer tokenization for all token types
- `NoirSettingsTest.kt` - Tests settings persistence and project overrides

### Running Specific Tests
```bash
./gradlew test --tests "cash.turbine.noir.NoirLexerTest"
```

## Future Improvements

### Potential Features
- Run test action from gutter icon
- Nargo.toml support (project configuration)
- Code folding for functions, structs, modules
- Structure view for file outline
- Rename refactoring support
- Custom color scheme options

## References

- [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/)
- [LSP API Documentation](https://plugins.jetbrains.com/docs/intellij/language-server-protocol.html)
- [Noir Language](https://noir-lang.org/)
- [VSCode Noir Extension](https://github.com/noir-lang/vscode-noir) (reference implementation)
