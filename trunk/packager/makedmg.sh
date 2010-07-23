#!/bin/sh

cd pack/macosx/
echo $APPNAME
mkisofs -r -v -J -D -allow-leading-dots -V "ConcoursJeunes Install" -o ${APPNAME}-$VERSION.dmg .
