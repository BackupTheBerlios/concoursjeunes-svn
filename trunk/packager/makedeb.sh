#!/bin/sh

# se rend dans le repertoire contenant le tar.gz
cd pack/linux/
mkdir deb
cd deb

# construit le .orig necessaire à la creation du deb
cp ../ArcCompetition-$VERSION.tar.gz arccompetition_$VERSION.orig.tar.gz

# crée le repertoire de version décompressé
mkdir arccompetition-$VERSION
# décompresse le contenu du .orig dedans
cp arccompetition_$VERSION.orig.tar.gz arccompetition-$VERSION/
cd arccompetition-$VERSION
tar zxvf arccompetition_$VERSION.orig.tar.gz
rm arccompetition_$VERSION.orig.tar.gz
#copie le debian
mkdir debian
#cp ../../../../packager/linux/debian/compat debian/
echo 7 > debian/compat
cp ../../../../packager/linux/debian/control debian/
cp ../../../../packager/linux/debian/copyright debian/
cp ../../../../packager/linux/debian/dirs debian/
cp ../../../../packager/linux/debian/postinst debian/
cp ../../../../packager/linux/debian/rules debian/
cp -f ../../../../pack/changelog.txt debian/changelog

#construit le paquet
#dpkg-buildpackage -kE88997BA -rfakeroot
#dpkg-buildpackage -rfakeroot
debuild -us -uc
