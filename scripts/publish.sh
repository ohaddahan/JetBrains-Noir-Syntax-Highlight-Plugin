#!/bin/bash
set -e

echo "=========================================="
echo "JetBrains Marketplace Publishing"
echo "=========================================="
echo ""

# Check for required environment variables
MISSING_VARS=0

if [ -z "$PUBLISH_TOKEN" ]; then
    echo "ERROR: PUBLISH_TOKEN is not set"
    echo "  Get your token from: https://plugins.jetbrains.com/author/me/tokens"
    MISSING_VARS=1
fi

if [ -z "$CERTIFICATE_CHAIN" ]; then
    echo "WARNING: CERTIFICATE_CHAIN is not set (signing will be skipped)"
    echo "  See: https://plugins.jetbrains.com/docs/intellij/plugin-signing.html"
fi

if [ -z "$PRIVATE_KEY" ]; then
    echo "WARNING: PRIVATE_KEY is not set (signing will be skipped)"
fi

if [ -z "$PRIVATE_KEY_PASSWORD" ]; then
    echo "WARNING: PRIVATE_KEY_PASSWORD is not set (signing will be skipped)"
fi

if [ $MISSING_VARS -eq 1 ]; then
    echo ""
    echo "Required environment variables:"
    echo "  export PUBLISH_TOKEN='your-jetbrains-marketplace-token'"
    echo ""
    echo "Optional (for signed plugins):"
    echo "  export CERTIFICATE_CHAIN='-----BEGIN CERTIFICATE-----...'"
    echo "  export PRIVATE_KEY='-----BEGIN RSA PRIVATE KEY-----...'"
    echo "  export PRIVATE_KEY_PASSWORD='your-key-password'"
    echo ""
    exit 1
fi

echo ""
echo "Building and publishing plugin..."
echo ""

# Build the plugin first
./gradlew buildPlugin

# Publish to marketplace
./gradlew publishPlugin

echo ""
echo "=========================================="
echo "Plugin published successfully!"
echo "=========================================="
echo ""
echo "View your plugin at:"
echo "  https://plugins.jetbrains.com/plugin/YOUR_PLUGIN_ID"
