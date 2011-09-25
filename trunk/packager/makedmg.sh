#!/bin/sh

cd pack/macosx/
ln -s /Applications .
mkisofs -r -v -J -D -allow-leading-dots -V "ArcCompetition Install" -o ${APPNAME}-$VERSION.dmg .
