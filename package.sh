#!/bin/bash

# package.sh - Package the application for distribution

echo "Packaging the application..."

# Create a distribution directory
DIST_DIR="dist"
APP_NAME="automated-timetable-system"
VERSION="1.0"

# Clean previous distribution
rm -rf $DIST_DIR
mkdir -p $DIST_DIR

# Build the application
./build.sh

# Copy necessary files to distribution directory
cp timetable.jar $DIST_DIR/
cp -r lib $DIST_DIR/
cp README.md $DIST_DIR/
cp LICENSE $DIST_DIR/
cp BUILDING.md $DIST_DIR/

# Create a run script for the distribution
echo "#!/bin/bash" > $DIST_DIR/run.sh
echo "java -jar timetable.jar" >> $DIST_DIR/run.sh
chmod +x $DIST_DIR/run.sh

# Create a zip file of the distribution
ZIP_NAME="$APP_NAME-$VERSION.zip"
cd $DIST_DIR
zip -r ../$ZIP_NAME .
cd ..

echo "Packaging complete! Created $ZIP_NAME"