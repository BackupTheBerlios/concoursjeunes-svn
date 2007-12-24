#!/bin/sh

# se rend dans le repertoire contenant le tar.gz
cd pack/linux/SOURCES

# construit le .orig necessaire à la creation du deb
cp ConcoursJeunes.tar.gz concoursjeunes_$VERSION.orig.tar.gz

# crée le repertoire de version décompressé
mkdir concoursjeunes-$VERSION
# décompresse le contenu du .orig dedans
cp concoursjeunes_$VERSION.orig.tar.gz concoursjeunes-$VERSION/
cd concoursjeunes-$VERSION
tar zxvf concoursjeunes_$VERSION.orig.tar.gz
rm concoursjeunes_$VERSION.orig.tar.gz
#copie le debian
cp -r ../../../../packager/linux/debian .
cp -f ../../../../packager/changelog.txt debian/changelog

#construit le paquet
dpkg-buildpackage -kE88997BA -rfakeroot
