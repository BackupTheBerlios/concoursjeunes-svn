#!/bin/sh

cd pack/macosx/
echo $APPNAME
mkisofs -r -v -J -V "ConcoursJeunes Install" -o ${APPNAME}-$VERSION.dmg .