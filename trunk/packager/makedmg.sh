#!/bin/sh

cd pack/macosx/
echo $APPNAME
mkisofs -r -v -J -D -V "ConcoursJeunes Install" -o ${APPNAME}-$VERSION.dmg .
