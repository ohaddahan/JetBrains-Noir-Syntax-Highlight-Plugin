# Noir JetBrains Plugin - TODO

## Status: v0.1.0 Complete

The plugin is feature-complete and ready for JetBrains Marketplace submission.

## Completed Features

### Core
- [x] File type recognition for `.nr` files
- [x] Language registration (NoirLanguage)
- [x] Plugin icons (noir.svg, test.svg)
- [x] Comment toggling (`//` and `/* */`)
- [x] Bracket matching

### Syntax Highlighting
- [x] Native lexer (NoirLexer.kt)
- [x] Token types and syntax highlighter
- [x] Keywords, types, strings, comments, numbers, attributes
- [x] Boolean literals
- [x] Nested block comments

### LSP Integration
- [x] LSP client connecting to `nargo lsp`
- [x] Code completion
- [x] Diagnostics (errors/warnings)
- [x] Go to definition
- [x] Hover information
- [x] Find references
- [x] Signature help
- [x] Code actions
- [x] Auto-detection of nargo from PATH
- [x] Silent failure handling

### Settings
- [x] Application-level settings (global nargo path, enable LSP, lightweight mode)
- [x] Project-level settings (nargo path override)
- [x] Settings UI at Settings → Languages & Frameworks → Noir

### Live Templates
- [x] `fn`, `fnmain` - Function declarations
- [x] `struct` - Struct declaration
- [x] `let` - Variable declaration
- [x] `for`, `forin` - For loops
- [x] `if`, `elseif`, `else` - Conditionals
- [x] `mod`, `use` - Module declarations
- [x] `letfor`, `letforin` - Array comprehensions

### Actions
- [x] Restart Language Server (Tools → Noir → Restart Language Server)
- [x] Expand Macros (Tools → Noir → Expand Macros)
- [x] Browse Standard Library (Tools → Noir → Browse Standard Library)

### Gutter Icons
- [x] Test indicator icons for `#[test]` functions

### Stdlib Viewer
- [x] Virtual file system for `noir-std://` URIs
- [x] Auto-detection of stdlib location from nargo installation
- [x] Browse Standard Library action

### Testing
- [x] Unit tests for file type recognition
- [x] Unit tests for lexer tokenization
- [x] Unit tests for settings persistence

### Documentation & CI/CD
- [x] README.md with full documentation
- [x] DEV.md with implementation details
- [x] CHANGELOG.md
- [x] LICENSE (MIT)
- [x] GitHub Actions workflows (build, release)

## Remaining Tasks

### Marketplace Submission
- [ ] Submit to JetBrains Marketplace
- [ ] Add screenshots to marketplace listing
- [ ] Set up plugin signing (optional, for verified publisher badge)

## Future Enhancements (Post v0.1.0)

### Potential Features
- [ ] Run test action from gutter icon
- [ ] Nargo.toml support (project configuration)
- [ ] Code folding for functions, structs, modules
- [ ] Structure view for file outline
- [ ] Find usages across project
- [ ] Rename refactoring support
- [ ] Custom color scheme options
