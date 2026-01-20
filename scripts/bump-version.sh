#!/bin/bash
set -e

# Usage: ./scripts/bump-version.sh [major|minor|patch] or ./scripts/bump-version.sh <version>

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
PROPERTIES_FILE="$PROJECT_DIR/gradle.properties"

# Get current version
CURRENT_VERSION=$(grep "^pluginVersion" "$PROPERTIES_FILE" | cut -d'=' -f2 | tr -d ' ')

if [ -z "$CURRENT_VERSION" ]; then
    echo "ERROR: Could not find pluginVersion in $PROPERTIES_FILE"
    exit 1
fi

echo "Current version: $CURRENT_VERSION"

# Parse current version
IFS='.' read -r MAJOR MINOR PATCH <<< "$CURRENT_VERSION"

# Determine new version
case "${1:-patch}" in
    major)
        NEW_VERSION="$((MAJOR + 1)).0.0"
        ;;
    minor)
        NEW_VERSION="$MAJOR.$((MINOR + 1)).0"
        ;;
    patch)
        NEW_VERSION="$MAJOR.$MINOR.$((PATCH + 1))"
        ;;
    *)
        # Assume it's a specific version
        if [[ "$1" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
            NEW_VERSION="$1"
        else
            echo "Usage: $0 [major|minor|patch|<version>]"
            echo "  major  - Bump major version (X.0.0)"
            echo "  minor  - Bump minor version (x.X.0)"
            echo "  patch  - Bump patch version (x.x.X) [default]"
            echo "  <version> - Set specific version (e.g., 1.2.3)"
            exit 1
        fi
        ;;
esac

echo "New version: $NEW_VERSION"

# Update gradle.properties using temp file (works on both macOS and Linux)
TEMP_FILE=$(mktemp)
sed "s/^pluginVersion = .*/pluginVersion = $NEW_VERSION/" "$PROPERTIES_FILE" > "$TEMP_FILE"
mv "$TEMP_FILE" "$PROPERTIES_FILE"

echo ""
echo "Version bumped from $CURRENT_VERSION to $NEW_VERSION"
echo ""
echo "Next steps:"
echo "  1. Update CHANGELOG.md with release notes"
echo "  2. Commit: git commit -am 'Bump version to $NEW_VERSION'"
echo "  3. Push and create a release on GitHub"
