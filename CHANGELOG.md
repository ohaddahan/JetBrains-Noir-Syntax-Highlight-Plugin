<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Noir JetBrains Plugin Changelog

## [Unreleased]

## [0.1.0] - 2024-01-13

### Added

#### Syntax Highlighting
- Native syntax highlighting for Noir (.nr) files
- Keywords: `fn`, `struct`, `if`, `else`, `for`, `loop`, `while`, `match`, etc.
- Types: `u8`, `u16`, `u32`, `u64`, `i8`, `i16`, `i32`, `i64`, `Field`, `bool`, `str`
- Strings: regular strings, raw strings (`r#""#`), f-strings (`f""`)
- Comments: line comments (`//`) and nested block comments (`/* */`)
- Numeric literals: decimal and hexadecimal
- Attributes: `#[test]`, `#[derive(...)]`, etc.
- Boolean literals: `true`, `false`

#### LSP Integration
- Full integration with `nargo lsp` language server
- Code completion with context-aware suggestions
- Real-time diagnostics (errors and warnings)
- Go to definition navigation
- Hover information with type details
- Find references across files
- Signature help for function parameters
- Code actions for quick fixes

#### Live Templates
- `fn` - Function declaration
- `fnmain` - Main function with body
- `struct` - Struct declaration
- `let` - Variable declaration
- `for` - For loop with range
- `forin` - For-in loop over collection
- `if` - If statement
- `elseif` - Else-if branch
- `else` - Else block
- `mod` - Module declaration
- `use` - Use/import statement
- `letfor` - Array comprehension
- `letforin` - Array comprehension with index

#### Settings
- Application-level settings for global configuration
- Project-level settings with override capability
- Auto-detection of nargo from system PATH
- Enable/disable LSP toggle
- Lightweight mode option (disables completions, signature help, code actions)

#### Actions (Tools Menu)
- Restart Language Server - Restart the nargo lsp connection
- Expand Macros - Run `nargo expand` and view results in scratch file
- Browse Standard Library - Open Noir stdlib source files

#### Gutter Icons
- Test tube icon for `#[test]` functions

#### Stdlib Viewer
- Virtual file system for browsing standard library
- Auto-detection of stdlib location from nargo installation

#### Other Features
- File type recognition for `.nr` extension
- Custom file icon
- Comment toggling (Cmd/Ctrl + /)
- Block comment toggling (Cmd/Ctrl + Shift + /)
- Bracket matching

### Technical Details
- Minimum IDE version: 2023.2
- Built with Kotlin and Gradle
- Uses JetBrains native LSP API
- No external dependencies required for syntax highlighting

### Notes
- Requires [nargo](https://noir-lang.org/docs/getting_started/installation/) installed for LSP features
- LSP features require nargo to be in PATH or configured in settings
