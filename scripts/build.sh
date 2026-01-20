#!/bin/bash
set -e

echo "Building Noir Language Support plugin..."
./gradlew buildPlugin

echo ""
echo "Build complete! Plugin ZIP available at:"
ls -la build/distributions/*.zip 2>/dev/null || echo "  (run ./gradlew buildPlugin to generate)"
